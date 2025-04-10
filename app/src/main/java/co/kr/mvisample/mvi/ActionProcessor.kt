package co.kr.mvisample.mvi

import kotlinx.coroutines.flow.Flow

/**
 * Action ( ex: ButtonClick, CheckAutoLogin ) 을 받아서
 *
 * UiState, Event 그리고 UiState 와 Event 를 동시에 방출할 수 있는 Flow
 */
interface ActionProcessor<Action, UiState, Event> {
    operator fun invoke(action: Action, currentUiState: UiState): Flow<Pair<UiState?, Event?>>
}
