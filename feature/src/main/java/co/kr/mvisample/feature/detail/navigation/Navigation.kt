package co.kr.mvisample.feature.detail.navigation

import kotlinx.serialization.Serializable

@Serializable data class PokemonDetail(val id: Int, val name: String, val isDiscovered: Boolean)