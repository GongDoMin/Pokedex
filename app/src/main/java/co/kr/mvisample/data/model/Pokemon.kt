package co.kr.mvisample.data.model

import co.kr.mvisample.remote.model.PokemonResponse

data class Pokemon(
    val number: Int = 0,
    val name: String = "",
    val imageUrl: String = "",
    val isDiscovered: Boolean = false
)

fun PokemonResponse.toData(): Pokemon =
    Pokemon(
        number = getNumber(),
        name = name,
        imageUrl = getImageUrl(),
        isDiscovered = listOf(true, false).random()
    )
