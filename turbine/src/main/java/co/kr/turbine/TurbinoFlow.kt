package co.kr.turbine

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

internal fun <T> Flow<T>.collectTurbine(
    coroutineScope: CoroutineScope
) : Turbine<T> {
    val channel = Channel<T>(Channel.UNLIMITED)
    val job = coroutineScope.launch(
        context = Dispatchers.Unconfined,
        start = CoroutineStart.UNDISPATCHED
    ) {
        try {
            collect { channel.trySend(it) }
            channel.close()
        } catch (e: Exception) {
            channel.close(e)
        }
    }

    return TurbineImpl(
        channel = channel,
        job = job
    )
}
