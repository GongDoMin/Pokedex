package co.kr.mvisample.ui.detail.presentation

import co.kr.mvisample.mvi.BaseViewModelWithActionProcessor
import co.kr.mvisample.mvi.ActionProcessor
import co.kr.mvisample.ui.detail.di.LogoutActionAnnotation
import co.kr.mvisample.ui.detail.di.SignInActionAnnotation
import co.kr.mvisample.ui.detail.di.SignUpActionAnnotation
import co.kr.mvisample.ui.detail.model.DetailAction
import co.kr.mvisample.ui.detail.model.DetailEvent
import co.kr.mvisample.ui.detail.model.DetailUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    @SignUpActionAnnotation signUpActionProcessor: ActionProcessor<DetailAction, DetailUiState, DetailEvent>,
    @SignInActionAnnotation signInActionProcessor: ActionProcessor<DetailAction, DetailUiState, DetailEvent>,
    @LogoutActionAnnotation logoutActionProcessor: ActionProcessor<DetailAction, DetailUiState, DetailEvent>
): BaseViewModelWithActionProcessor<DetailAction, DetailUiState, DetailEvent>(
    initialState = DetailUiState(),
    actionProcessors = listOf(signUpActionProcessor, signInActionProcessor, logoutActionProcessor)
) {
    private val email = "dev@google.com"

    fun onClickSpecialButton() {
        handleAction(DetailAction.OnClickSpecialButton(email))
    }
}