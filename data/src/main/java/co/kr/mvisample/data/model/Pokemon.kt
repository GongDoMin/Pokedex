package co.kr.mvisample.data.model

import co.kr.mvisample.local.model.PokemonEntity
import co.kr.mvisample.remote.model.PokemonResponse
import co.kr.mvisample.remote.utils.getImageUrl

data class Pokemon(
    val id: Int = 0,
    val name: String = "",
    val imgUrl: String = "",
    val isDiscovered: Boolean = false,
    val isCaught: Boolean = false
)

fun PokemonResponse.toData(): Pokemon {
    val id = getId()
    return Pokemon(
        id = id,
        name = name,
        imgUrl = getImageUrl(id),
        isDiscovered = false,
        isCaught = false
    )
}

fun Pokemon.toEntity(): PokemonEntity =
    PokemonEntity(
        id = id,
        name = name,
        imgUrl = imgUrl,
        isDiscovered = isDiscovered,
        isCaught = isCaught
    )

fun PokemonEntity.toData(): Pokemon =
    Pokemon(
        id = id,
        name = name,
        imgUrl = imgUrl,
        isDiscovered = isDiscovered,
        isCaught = isCaught
    )