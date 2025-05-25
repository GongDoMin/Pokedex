package co.kr.mvisample.common.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import co.kr.mvisample.common.base.DialogAction

@Composable
fun OverlayWithLoadingAndDialog(
    isLoading: Boolean,
    isError: Boolean,
    errorTitle: String,
    errorContent: String,
    onSendAction: (DialogAction.BasicDialogAction) -> Unit,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        if (isLoading) LoadingIndicator()

        if (isError) PokemonDialog(
            title = errorTitle,
            content = errorContent,
            onSendAction = onSendAction
        )

        content()
    }
}
