package co.kr.turbino

import kotlinx.coroutines.currentCoroutineContext
import kotlin.coroutines.CoroutineContext
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

private val DEFAULT_EVENT_TIMEOUT: Duration = 2000.milliseconds
private val DEFAULT_COMPLETE_TIMEOUT: Duration = 500.milliseconds

internal class TurbinoEventTimeoutElement(
    val eventTimeout: Duration
) : CoroutineContext.Element {
    companion object Key : CoroutineContext.Key<TurbinoEventTimeoutElement>

    override val key: CoroutineContext.Key<*> = Key
}

internal class TurbinoCompleteTimeoutElement(
    val completeTimeout: Duration
) : CoroutineContext.Element {
    companion object Key : CoroutineContext.Key<TurbinoCompleteTimeoutElement>

    override val key: CoroutineContext.Key<*> = Key
}

internal suspend fun contextEventTimeout(): Duration {
    return currentCoroutineContext()[TurbinoEventTimeoutElement.Key]?.eventTimeout ?: DEFAULT_EVENT_TIMEOUT
}

internal suspend fun contextCompleteTimeout(): Duration {
    return currentCoroutineContext()[TurbinoCompleteTimeoutElement.Key]?.completeTimeout ?: DEFAULT_COMPLETE_TIMEOUT
}