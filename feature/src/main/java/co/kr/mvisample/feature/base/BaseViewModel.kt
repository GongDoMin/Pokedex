package co.kr.mvisample.feature.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.kr.mvisample.data.result.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class BaseViewModel<Action, Content, Event>(
    initialState: Content
): ViewModel() {
    private val _uiState = MutableStateFlow(UiState(content = initialState))
    val uiState = _uiState.asStateFlow()

    private val _event = Channel<Event>(capacity = Channel.BUFFERED)
    val event = _event.receiveAsFlow()

    protected fun updateUiState(function: (UiState<Content>) -> UiState<Content>) = _uiState.update(function)

    protected suspend fun sendEvent(event: Event) = _event.send(event)

    abstract fun handleAction(action: Action)

    /**
     * 대부분 버튼이 하나인 경우, 버튼 클릭 시 별도의 분기 없이 단순히 다이얼로그를 닫는(dismiss) 동작만 수행하므로
     * error 상태만 초기화하는 기본 구현을 제공합니다.
     */
    open fun handleBasicDialogAction(action: DialogAction.BasicDialogAction) {
        updateErrorState(isError = false)
    }

    protected fun launch(
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
        coroutineName: CoroutineName = CoroutineName("BaseViewModel"),
        start: CoroutineStart = CoroutineStart.DEFAULT,
        block: suspend CoroutineScope.() -> Unit
    ) {
        viewModelScope.launch(
            context = dispatcher + coroutineName,
            start = start,
            block = block
        )
    }

    protected suspend fun <DataModel> Flow<Result<DataModel>>.resultCollect(
        onSuccess: Content.(DataModel) -> Content,
        onLoading: Content.(DataModel?) -> Content = { uiState.value.content },
        onError: (Throwable) -> Unit = {}
    ) {
        collect { result ->
            when (result) {
                is Result.Loading -> {
                    updateLoadingState(true)
                    updateContent { onLoading(result.data) }
                }
                is Result.Error -> {
                    updateLoadingState(false)
                    onError(result.throwable)
                }
                is Result.Success -> {
                    updateLoadingState(false)
                    updateContent { onSuccess(result.data) }
                }
            }
        }
    }

    protected fun updateContent(content: Content.() -> Content) {
        updateUiState { state ->
            state.copy(
                content = content(state.content)
            )
        }
    }

    protected open fun updateErrorState(
        isError: Boolean,
        errorTitle: String = "",
        errorContent: String = ""
    ) {
        updateUiState { state ->
            state.copy(
                error = state.error.copy(
                    isError = isError,
                    errorTitle = errorTitle,
                    errorContent = errorContent
                )
            )
        }
    }

    protected open fun updateLoadingState(isLoading: Boolean) {
        updateUiState { state ->
            state.copy(
                loading = state.loading.copy(
                    isLoading = isLoading
                )
            )
        }
    }
}