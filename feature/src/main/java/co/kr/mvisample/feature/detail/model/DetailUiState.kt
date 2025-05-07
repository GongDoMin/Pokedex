package co.kr.mvisample.feature.detail.model

import androidx.compose.runtime.Immutable

@Immutable
data class DetailUiState(
    val pokemonDetail: PokemonDetailModel = PokemonDetailModel(),
)