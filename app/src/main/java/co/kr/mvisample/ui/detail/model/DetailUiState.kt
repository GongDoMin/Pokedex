package co.kr.mvisample.ui.detail.model

import androidx.compose.runtime.Immutable

@Immutable
data class DetailUiState(
    val text: String = "",
    val specialText: String = ""
)