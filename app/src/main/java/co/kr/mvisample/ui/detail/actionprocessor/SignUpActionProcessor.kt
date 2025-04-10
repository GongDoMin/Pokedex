package co.kr.mvisample.ui.detail.actionprocessor

import co.kr.mvisample.mvi.ActionProcessor
import co.kr.mvisample.ui.detail.model.DetailAction
import co.kr.mvisample.ui.detail.model.DetailEvent
import co.kr.mvisample.ui.detail.model.DetailUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SignUpActionProcessor @Inject constructor(

): ActionProcessor<DetailAction, DetailUiState, DetailEvent> {
    override fun invoke(action: DetailAction, currentUiState: DetailUiState): Flow<Pair<DetailUiState?, DetailEvent?>> = flow {
        when (action) {
            DetailAction.OnClickFirstButton -> handleOnClickFirstButton(currentUiState)
            else -> {}
        }
    }

    private suspend fun FlowCollector<Pair<DetailUiState?, DetailEvent?>>.handleOnClickFirstButton(currentUiState: DetailUiState) {
        emit(currentUiState.copy(text = "OnClickFirstButton") to null)
    }
}