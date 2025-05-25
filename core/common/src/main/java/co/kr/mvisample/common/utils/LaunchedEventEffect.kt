package co.kr.mvisample.common.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector

@Composable
fun <Event>LaunchedEventEffect(
    event: Flow<Event>,
    collector: FlowCollector<Event>,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    state: Lifecycle.State = Lifecycle.State.STARTED,
) {
    LaunchedEffect(lifecycleOwner) {
        lifecycleOwner.repeatOnLifecycle(state = state) {
            event.collect(collector)
        }
    }
}