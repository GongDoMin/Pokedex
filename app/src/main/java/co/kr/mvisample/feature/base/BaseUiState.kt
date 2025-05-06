package co.kr.mvisample.feature.base

sealed interface UiStateMarker

data class UiState<Content>(
    val loading: Loading = Loading(),
    val error: Error = Error(),
    val content: Content
): UiStateMarker

data class CombinedUiState<Content1, Content2>(
    val loading: Loading = Loading(),
    val error: Error = Error(),
    val content1: Content1,
    val content2: Content2
): UiStateMarker

data class Loading(
    val isLoading: Boolean = false
)

data class Error(
    val isError: Boolean = false,
    val errorTitle: String = "",
    val errorContent: String = ""
)