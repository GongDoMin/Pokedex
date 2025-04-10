package co.kr.mvisample.ui.detail.model

sealed interface DetailEvent {
    data object NavigateToFirstScreen: DetailEvent
}