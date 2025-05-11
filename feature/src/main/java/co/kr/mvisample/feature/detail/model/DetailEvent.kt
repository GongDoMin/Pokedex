package co.kr.mvisample.feature.detail.model

sealed interface DetailEvent {
    data object OnNavigateToBack : DetailEvent
}