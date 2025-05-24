package co.kr.mvisample.data.model

import co.kr.mvisample.remote.model.TypeWithSlotResponse

data class PokemonDetail(
    val id: Int = 0,
    val imgUrl: String = "",
    val name: String = "",
    val weight: Float = 0f,
    val height: Float = 0f,
    val types: List<Type> = emptyList(),
    val cry: String = ""
)

data class Type(
    val name: String = ""
)

fun TypeWithSlotResponse.toData(): Type =
    Type(
        name = type.name,
    )
