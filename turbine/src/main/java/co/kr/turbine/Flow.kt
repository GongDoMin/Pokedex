package co.kr.turbine

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.time.Duration

private suspend fun turbineScope(
    eventTimeout: Duration?,
    completeTimeout: Duration?,
    block: suspend CoroutineScope.() -> Unit
) {
    val coroutineContext = buildList {
        eventTimeout?.let { add(TurbineEventTimeoutElement(it)) }
        completeTimeout?.let { add(TurbineCompleteTimeoutElement(it)) }
    }.fold(EmptyCoroutineContext, CoroutineContext::plus)

    withContext(coroutineContext, block)
}

suspend fun <T> Flow<T>.testTurbine(
    eventTimeout: Duration? = null,
    completeTimeout: Duration? = null,
    validate: suspend Turbine<T>.() -> Unit,
) {
    turbineScope(eventTimeout, completeTimeout) {
        collectTurbine(this).apply {
            this.validate()
            cancel()
            ensureAllEventsConsumed()
        }
    }
}
