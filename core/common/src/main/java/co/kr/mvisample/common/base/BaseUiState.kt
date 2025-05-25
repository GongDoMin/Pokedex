package co.kr.mvisample.common.base

sealed interface UiStateMarker<Content>

data class UiState<Content>(
    val loading: Loading = Loading(),
    val error: Error = Error(),
    val content: Content
): UiStateMarker<Content>

data class Loading(
    val isLoading: Boolean = false
)

data class Error(
    val isError: Boolean = false,
    val errorTitle: String = "",
    val errorContent: String = ""
)