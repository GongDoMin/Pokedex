package co.kr.mvisample.mvi

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update

/**
 * 비즈니스 로직을 ViewModel 하나로 관리할 수 있을 때 사용하는 BaseViewModel
 */
abstract class BaseViewModel<Action, UiState, Event>(
    initialState: UiState
): ViewModel() {
    private val _uiState = MutableStateFlow(initialState)
    val uiState = _uiState.asStateFlow()

    private val _event = Channel<Event>(capacity = Channel.BUFFERED)
    val event = _event.receiveAsFlow()

    protected fun updateUiState(function: (UiState) -> UiState) = _uiState.update(function)

    protected fun sendEvent(event: Event) = _event.trySend(event)

    abstract fun handleAction(action: Action)

    fun <T> Flow<T>.withLoading(loading: (Boolean) -> Unit): Flow<T> =
        this
            .onStart { loading(true) }
            .onCompletion { loading(false) }
}