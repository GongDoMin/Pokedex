package co.kr.mvisample.feature.home.pokedex.model

import co.kr.mvisample.data.model.Pokemon

data class PokemonModel(
    val id: Int = 0,
    val name: String = "",
    val imageUrl: String = "",
    val isDiscovered: Boolean = false,
    val isCaught: Boolean = false
) {
    fun formatNumber(): String = "%03d".format(id)
}

fun Pokemon.toFeature() : PokemonModel =
    PokemonModel(
        id = id,
        name = name,
        imageUrl = imgUrl,
        isDiscovered = isDiscovered,
        isCaught = isCaught
    )