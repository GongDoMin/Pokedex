package co.kr.turbino

import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.channels.ChannelResult
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.withTimeout
import kotlin.time.Duration

internal suspend fun <T> ReceiveChannel<T>.awaitEvent(
    timeout: Duration? = null
) : Event<T> {
    val finalTimeout = timeout ?: contextEventTimeout()

    return try {
        withTimeout(finalTimeout) {
            receiveCatching().toEvent()!!
        }
    } catch (e: TimeoutCancellationException) {
        Event.Error(e)
    } catch (e: Throwable) {
        Event.Error(e)
    }
}

internal suspend fun <T> ReceiveChannel<T>.awaitItem() : T =
    when (val result = awaitEvent()) {
        is Event.Item -> result.value
        else -> unexpectedEvent(result, Event.Item::class.simpleName.toString())
    }

internal suspend fun <T> ReceiveChannel<T>.awaitComplete() =
    when (val result = awaitEvent()) {
        is Event.Complete -> Unit
        else -> unexpectedEvent(result, Event.Complete::class.simpleName.toString())
    }

internal suspend fun <T> ReceiveChannel<T>.awaitLastItem(expected: T) : T {
    val value: T
    val timeout = contextCompleteTimeout()

    while (true) {
        val item = awaitItem()
        if (item == expected) {
            val event = awaitEvent(timeout)
            if (event is Event.Error && event.throwable is TimeoutCancellationException) {
                value = item
                break
            } else {
                unexpectedEvent(event, "Event should be empty")
            }
        }
    }

    return value
}

private fun unexpectedEvent(event: Event<*>?, expected: String): Nothing {
    val eventAsString = event?.toString() ?: "no event"
    throw AssertionError("Expected $expected but was $eventAsString")
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