package co.kr.mvisample.feature.home.computer.model

import androidx.compose.runtime.Immutable

@Immutable
data class ComputerUiState(
    val pokemonIcons: List<PokemonIconModel> = emptyList()
)