package co.kr.mvisample.navigation

import kotlinx.serialization.Serializable

sealed interface PokemonRoutes {
    @Serializable data object Home : PokemonRoutes
    @Serializable data class PokemonDetail(val pokemonName: String, val isDiscovered: Boolean) : PokemonRoutes
}