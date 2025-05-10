package co.kr.mvisample.data.model

import co.kr.mvisample.local.model.PokemonEntity
import co.kr.mvisample.remote.model.PokemonResponse
import co.kr.mvisample.remote.utils.getImageUrl

data class Pokemon(
    val id: Int = 0,
    val name: String = "",
    val imageUrl: String = "",
    val isDiscovered: Boolean = false
)

fun PokemonResponse.toData(): Pokemon {
    val id = getId()
    return Pokemon(
        id = id,
        name = name,
        imageUrl = getImageUrl(id),
        isDiscovered = listOf(true, false).random()
    )
}

fun Pokemon.toEntity(): PokemonEntity =
    PokemonEntity(
        id = id,
        name = name,
        imgUrl = imageUrl,
        isDiscovered = false,
        isCaught = false
    )

fun PokemonEntity.toData(): Pokemon =
    Pokemon(
        id = id,
        name = name,
        imageUrl = imgUrl,
        isDiscovered = false
    )