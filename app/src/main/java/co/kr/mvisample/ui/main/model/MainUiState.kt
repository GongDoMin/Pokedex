package co.kr.mvisample.ui.main.model

import androidx.compose.runtime.Immutable

@Immutable
data class MainUiState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val name: String = "",
    val age: Int = 0,
    val height: Int = 0,
    val weight: Int = 0,
    val isEnabledButton: Boolean = true
)