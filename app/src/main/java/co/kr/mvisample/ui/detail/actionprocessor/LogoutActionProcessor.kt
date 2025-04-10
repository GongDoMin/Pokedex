package co.kr.mvisample.ui.detail.actionprocessor

import co.kr.mvisample.mvi.ActionProcessor
import co.kr.mvisample.ui.detail.model.DetailAction
import co.kr.mvisample.ui.detail.model.DetailEvent
import co.kr.mvisample.ui.detail.model.DetailUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LogoutActionProcessor @Inject constructor(

): ActionProcessor<DetailAction, DetailUiState, DetailEvent> {
    override fun invoke(action: DetailAction, currentUiState: DetailUiState): Flow<Pair<DetailUiState?, DetailEvent?>> = flow {
        when (action) {
            DetailAction.OnClickThirdButton -> handleOnClickThirdButton()
            is DetailAction.OnClickSpecialButton -> handleOnClickSpecialButton(currentUiState, action.email)
            else -> {}
        }
    }

    private suspend fun FlowCollector<Pair<DetailUiState?, DetailEvent?>>.handleOnClickThirdButton() {
        emit(null to DetailEvent.NavigateToFirstScreen)
    }

    private suspend fun FlowCollector<Pair<DetailUiState?, DetailEvent?>>.handleOnClickSpecialButton(currentUiState: DetailUiState, email: String) {
        emit(currentUiState.copy(specialText = "OnClickSpecialButton $email") to null)
    }
}