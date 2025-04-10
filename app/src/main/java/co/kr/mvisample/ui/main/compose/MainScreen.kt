package co.kr.mvisample.ui.main.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.kr.mvisample.ui.main.presentation.MainViewModel
import co.kr.mvisample.ui.main.model.MainEvent

@Composable
fun MainScreen(
    onNavigateToNextPage: () -> Unit,
    viewModel: MainViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(lifecycleOwner) {
        viewModel.event.collect { event ->
            when (event) {
                MainEvent.NavigateToNextPage -> onNavigateToNextPage()
            }
        }
    }

    MainContent(
        uiState = uiState,
        sendAction = viewModel::handleAction
    )
}