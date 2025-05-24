package co.kr.mvisample.navigation

import kotlinx.serialization.Serializable

sealed interface HomeRoutes {
    @Serializable data object Pokedex: HomeRoutes
    @Serializable data object Computer: HomeRoutes
}