package co.kr.mvisample.ui.main.presentation

import androidx.lifecycle.viewModelScope
import co.kr.mvisample.domain.usecase.GetUserUseCase
import co.kr.mvisample.domain.usecase.PlusNumberUseCase
import co.kr.mvisample.mvi.BaseViewModel
import co.kr.mvisample.ui.main.model.MainAction
import co.kr.mvisample.ui.main.model.MainEvent
import co.kr.mvisample.ui.main.model.MainUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val plusNumberUseCase: PlusNumberUseCase
): BaseViewModel<MainAction, MainUiState, MainEvent>(
    initialState = MainUiState()
) {

    init {
        handleAction(MainAction.GetUser)
    }

    override fun handleAction(action: MainAction) {
        when (action) {
            MainAction.GetUser -> handleGetUser()
            MainAction.OnClickPlusAgeButton -> handleOnClickPlusAgeButton()
            MainAction.OnClickPlusHeightButton -> handleOnClickPlusHeightButton()
            MainAction.OnClickPlusWeightButton -> handleOnClickPlusWeightButton()
            MainAction.OnClickLoadingButton -> handleOnClickLoadingButton()
            MainAction.OnClickErrorButton -> handleOnClickErrorButton()
            MainAction.OnClickNextButton -> handleOnClickNextButton()
        }
    }

    private fun handleGetUser() {
        viewModelScope.launch {
            getUserUseCase()
                .withLoading {
                    updateUiState { uiState ->
                        uiState.copy(isLoading = it, isEnabledButton = !it)
                    }
                }.collect {
                    updateUiState { uiState ->
                        uiState.copy(
                            name = it.name,
                            age = it.age,
                            height = it.height,
                            weight = it.weight
                        )
                    }
                }
        }
    }

    private fun handleOnClickPlusAgeButton() {
        viewModelScope.launch {
            plusNumberUseCase()
                .withLoading {
                    updateUiState { uiState ->
                        uiState.copy(isLoading = it, isEnabledButton = !it)
                    }
                }.collect {
                    updateUiState { uiState ->
                        uiState.copy(age = uiState.age + 1)
                    }
                }
        }
    }

    private fun handleOnClickPlusHeightButton() {
        viewModelScope.launch {
            plusNumberUseCase()
                .withLoading {
                    updateUiState { uiState ->
                        uiState.copy(isLoading = it, isEnabledButton = !it)
                    }
                }.collect {
                    updateUiState { uiState ->
                        uiState.copy(height = uiState.height + 1)
                    }
                }
        }
    }

    private fun handleOnClickPlusWeightButton() {
        viewModelScope.launch {
            plusNumberUseCase()
                .withLoading {
                    updateUiState { uiState ->
                        uiState.copy(isLoading = it, isEnabledButton = !it)
                    }
                }.collect {
                    updateUiState { uiState ->
                        uiState.copy(weight = uiState.weight + 1)
                    }
                }
        }
    }

    private fun handleOnClickLoadingButton() {
        viewModelScope.launch {
            flow {
                emit(true)
                delay(2000)
                emit(false)
            }.collect {
                updateUiState { uiState ->
                    uiState.copy(isLoading = it, isEnabledButton = !it)
                }
            }
        }
    }

    private fun handleOnClickErrorButton() {
        viewModelScope.launch {
            flow {
                emit(true)
                delay(2000)
                emit(false)
            }.collect {
                updateUiState { uiState ->
                    uiState.copy(isLoading = it, isEnabledButton = !it)
                }
            }
        }
    }

    private fun handleOnClickNextButton() {
        sendEvent(MainEvent.NavigateToNextPage)
    }
}