package co.kr.mvisample.ui.main.model

sealed interface MainAction {
    data object GetUser: MainAction

    data object OnClickPlusAgeButton: MainAction
    data object OnClickPlusWeightButton: MainAction
    data object OnClickPlusHeightButton: MainAction
    data object OnClickLoadingButton: MainAction
    data object OnClickErrorButton: MainAction
    data object OnClickNextButton: MainAction
}