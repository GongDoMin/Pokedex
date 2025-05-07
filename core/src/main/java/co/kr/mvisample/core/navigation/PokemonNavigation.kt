package co.kr.mvisample.core.navigation

import kotlinx.serialization.Serializable

sealed interface PokemonRoutes {
    @Serializable data object Home : PokemonRoutes
    @Serializable data class PokemonDetail(val pokemonName: String, val isDiscovered: Boolean) : PokemonRoutes
}