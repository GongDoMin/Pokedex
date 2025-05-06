package co.kr.mvisample.data.model

import co.kr.mvisample.remote.model.PokemonResponse

data class Pokemon(
    val id: Int = 0,
    val name: String = "",
    val imageUrl: String = "",
    val isDiscovered: Boolean = false
)

fun PokemonResponse.toData(): Pokemon =
    Pokemon(
        id = getId(),
        name = name,
        imageUrl = getImageUrl(),
        isDiscovered = listOf(true, false).random()
    )
