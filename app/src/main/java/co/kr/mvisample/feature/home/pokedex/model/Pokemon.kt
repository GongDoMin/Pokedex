package co.kr.mvisample.feature.home.pokedex.model

import co.kr.mvisample.data.model.Pokemon

data class PokemonModel(
    val number: Int = 0,
    val name: String = "",
    val imageUrl: String = "",
    val isDiscovered: Boolean = false
) {
    fun formatNumber(): String = "No.%04d".format(number)
}

fun Pokemon.toFeature() : PokemonModel =
    PokemonModel(
        number = number,
        name = name,
        imageUrl = imageUrl,
        isDiscovered = isDiscovered
    )