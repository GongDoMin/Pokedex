package co.kr.mvisample.ui.detail.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import co.kr.mvisample.ui.detail.model.DetailEvent
import co.kr.mvisample.ui.detail.presentation.DetailViewModel

@Composable
fun DetailScreen(
    viewModel: DetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(Unit) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.event.collect { event ->
                when (event) {
                    DetailEvent.NavigateToFirstScreen -> println("you should go to first screen")
                }
            }
        }
    }

    DetailContent(
        uiState = uiState,
        sendAction = viewModel::handleAction,
        onClickSpecialButton = viewModel::onClickSpecialButton
    )
}