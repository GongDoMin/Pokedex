package co.kr.mvisample.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun OverlayWithLoadingAndDialog(
    isLoading: Boolean,
    isError: Boolean,
    errorTitle: String,
    errorContent: String,
    onDismissDialog: () -> Unit,
    onClickPositiveButton: () -> Unit,
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
            onDismissRequest = onDismissDialog,
            onClickPositiveButton = onClickPositiveButton
        )

        content()
    }
}
