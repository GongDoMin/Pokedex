package co.kr.turbino

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.time.Duration

private suspend fun turbinoScope(
    eventTimeout: Duration?,
    completeTimeout: Duration?,
    block: suspend CoroutineScope.() -> Unit
) {
    val coroutineContext = buildList {
        eventTimeout?.let { add(TurbinoEventTimeoutElement(it)) }
        completeTimeout?.let { add(TurbinoCompleteTimeoutElement(it)) }
    }.fold(EmptyCoroutineContext, CoroutineContext::plus)

    withContext(coroutineContext, block)
}

suspend fun <T> Flow<T>.testTurbino(
    eventTimeout: Duration? = null,
    completeTimeout: Duration? = null,
    validate: suspend Turbino<T>.() -> Unit,
) {
    turbinoScope(eventTimeout, completeTimeout) {
        collectTurbino(this).apply {
            this.validate()
            cancel()
            ensureAllEventsConsumed()
        }
    }
}
