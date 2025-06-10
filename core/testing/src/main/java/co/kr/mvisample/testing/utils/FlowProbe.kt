package co.kr.mvisample.testing.utils

import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Job
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ChannelResult
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout

suspend fun <T> Flow<T>.flowTest(
    validate: suspend FlowProbe<T>.() -> Unit
) = coroutineScope {
    val channel = Channel<T>(Channel.UNLIMITED)
    val job = launch(start = CoroutineStart.UNDISPATCHED) {
        collect { channel.trySend(it) }
    }

    val flowProbe = FlowProbeImpl(channel, job)

    validate(flowProbe)
    flowProbe.cancel()
    flowProbe.ensureAllEventsConsumed()
}


interface FlowProbe <T> {
    suspend fun cancel()
    suspend fun awaitEvent(): Event<T>
    suspend fun awaitItem(): T
    suspend fun awaitLastItem(expected: T): T
    fun ensureAllEventsConsumed()
}

class FlowProbeImpl <T> (
    channel: Channel<T>,
    private val job: Job
) : FlowProbe<T> {
    private val channel = object : Channel<T> by channel {}

    override suspend fun cancel() {
        channel.close()
        job.cancel()
    }

    override suspend fun awaitEvent(): Event<T> = channel.awaitEvent()

    override suspend fun awaitItem(): T = channel.awaitItem()

    override suspend fun awaitLastItem(expected: T): T = channel.awaitLastItem(expected)

    override fun ensureAllEventsConsumed() {
        val unconsumed = mutableListOf<Event<T>>()
        while (true) {
            val event = channel.tryReceive().toEvent() ?: break
            if (event is Event.Complete) break
            unconsumed += event
        }

        if (unconsumed.isNotEmpty()) throw Error("Unconsumed events: $unconsumed")
    }

}

internal suspend fun <T> ReceiveChannel<T>.awaitEvent(
    withTimeout: Long = 2_000L
) : Event<T> =
    try {
        withTimeout(withTimeout) {
            receiveCatching().toEvent()!!
        }
    } catch (e: TimeoutCancellationException) {
        Event.Error(e)
    }

internal suspend fun <T> ReceiveChannel<T>.awaitItem(): T =
    when (val result = awaitEvent()) {
        is Event.Item -> result.value
        else -> unexpectedEvent(result, "Event.Item")
    }

internal suspend fun <T> ReceiveChannel<T>.awaitLastItem(expected: T): T {
    val value: T

    while (true) {
        val item = awaitItem()
        if (item == expected) {
            val event = awaitEvent(withTimeout = 500L)
            if (event is Event.Error && event.throwable is TimeoutCancellationException) {
                value = item
                break
            } else {
                throw IllegalStateException("$expected is not the last item")
            }
        }
    }

    return value
}

private fun unexpectedEvent(event: Event<*>?, expected: String): Nothing {
    val eventAsString = event?.toString() ?: "no event"
    throw IllegalStateException("Expected $expected but was $eventAsString")
}

internal fun <T> ChannelResult<T>.toEvent(): Event<T>? {
    val cause = exceptionOrNull()
    return when {
        isSuccess -> Event.Item(getOrThrow())
        cause != null -> Event.Error(cause)
        isClosed -> Event.Complete
        else -> null
    }
}

sealed interface Event<out T> {
    data object Complete : Event<Nothing>
    data class Error(val throwable: Throwable) : Event<Nothing>
    data class Item<T>(val value: T) : Event<T>
}