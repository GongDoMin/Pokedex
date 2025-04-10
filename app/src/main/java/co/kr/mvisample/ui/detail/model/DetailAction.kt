package co.kr.mvisample.ui.detail.model

sealed interface DetailAction {
    data object OnClickFirstButton: DetailAction
    data object OnClickSecondButton: DetailAction
    data object OnClickThirdButton: DetailAction
    data class OnClickSpecialButton(val email: String): DetailAction
}