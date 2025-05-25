package co.kr.mvisample.common.base

sealed interface DialogAction {
    sealed interface BasicDialogAction: DialogAction {
        data object OnDismissDialog : DialogAction, BasicDialogAction
        data object OnClickPositiveButton : DialogAction, BasicDialogAction
    }
}