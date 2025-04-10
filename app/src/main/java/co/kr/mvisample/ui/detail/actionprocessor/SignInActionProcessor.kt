package co.kr.mvisample.ui.detail.actionprocessor

import co.kr.mvisample.mvi.ActionProcessor
import co.kr.mvisample.ui.detail.model.DetailAction
import co.kr.mvisample.ui.detail.model.DetailEvent
import co.kr.mvisample.ui.detail.model.DetailUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SignInActionProcessor @Inject constructor(

): ActionProcessor<DetailAction, DetailUiState, DetailEvent> {
    override fun invoke(action: DetailAction, currentUiState: DetailUiState): Flow<Pair<DetailUiState?, DetailEvent?>> = flow {
        when (action) {
            DetailAction.OnClickSecondButton -> handleOnClickSecondButton(currentUiState)
            else -> {}
        }
    }

    private suspend fun FlowCollector<Pair<DetailUiState?, DetailEvent?>>.handleOnClickSecondButton(currentUiState: DetailUiState) {
        emit(currentUiState.copy(text = "OnClickSecondButton") to null)
    }
}