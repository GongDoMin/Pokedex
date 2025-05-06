package co.kr.mvisample.feature.home.pokedex.model

import androidx.compose.runtime.Immutable

@Immutable
data class PokedexUiState(
    val selectedPokemon: PokemonModel? = null,
)

