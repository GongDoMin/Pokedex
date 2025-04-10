package co.kr.mvisample.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * 비즈니스 로직을 ViewModel 하나로 관리하기 힘들 때 사용하는 BaseViewModel
 */
open class BaseViewModelWithActionProcessor<Action,UiState, Event>(
    initialState: UiState,
    private val actionProcessors: List<ActionProcessor<Action, UiState, Event>>
): ViewModel() {
    private val _uiState = MutableStateFlow(initialState)
    val uiState = _uiState.asStateFlow()

    private val _event = Channel<Event>(capacity = Channel.BUFFERED)
    val event = _event.receiveAsFlow()

    open fun handleAction(action: Action) {
        viewModelScope.launch {
            actionProcessors.map { actionProcessor -> actionProcessor(action, uiState.value) }
                .merge()
                .collect { (uiState, event) ->
                    uiState?.let { _uiState.update { uiState } }
                    event?.let { _event.trySend(event) }
                }
        }
    }
}

