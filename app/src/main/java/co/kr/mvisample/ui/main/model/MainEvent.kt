package co.kr.mvisample.ui.main.model

sealed interface MainEvent {
    data object NavigateToNextPage: MainEvent
}