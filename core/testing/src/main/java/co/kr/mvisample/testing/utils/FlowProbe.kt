package co.kr.mvisample.testing.utils

import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ChannelResult
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout

suspend fun <T> Flow<T>.testFlowUntil(
    trigger: suspend () -> Unit,
    predicate: suspend (T) -> Boolean,
) = coroutineScope {
    val channel = Channel<T>(Channel.UNLIMITED)
    val job = launch(start = CoroutineStart.UNDISPATCHED) {
        collect { channel.trySend(it) }
    }

    val flowProbe = FlowProbeImpl(channel, job)

    trigger()

    while (true) {
        val item = flowProbe.awaitItem()
        if (predicate(item)) {
            flowProbe.cancel()
            flowProbe.ensureAllEventsConsumed()
            break
        }
    }
}


interface FlowProbe <T> {
    suspend fun cancel()
    suspend fun awaitItem(): T
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

    override suspend fun awaitItem(): T = channel.awaitItem()

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

internal suspend fun <T> ReceiveChannel<T>.awaitEvent() : Event<T> =
    withTimeout(2_000L) {
        receiveCatching().toEvent()!!
    }

internal suspend fun <T> ReceiveChannel<T>.awaitItem(): T =
    when (val result = awaitEvent()) {
        is Event.Item -> result.value
        else -> unexpectedEvent(result, "Event.Item")
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
