package co.kr.mvisample.data.model

import co.kr.mvisample.local.model.PokemonLocalEntity

data class PokemonIcon(
    val id: Int = 0,
    val iconUrl: String = "",
    val order: Int = 0
)

fun PokemonLocalEntity.toData() =
    PokemonIcon(
        id = id,
        iconUrl = iconUrl,
        order = order ?: 0
    )