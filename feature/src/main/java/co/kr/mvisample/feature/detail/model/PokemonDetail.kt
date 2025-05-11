package co.kr.mvisample.feature.detail.model

import co.kr.mvisample.data.model.PokemonDetail
import co.kr.mvisample.data.model.Type

data class PokemonDetailModel(
    val id: Int = 0,
    val imageUrl: String = "",
    val name: String = "",
    val weight: Float = 0f,
    val height: Float = 0f,
    val isDiscovered: Boolean = false,
    val types: List<TypeModel> = emptyList()
) {
    fun formatNumber(): String = "%03d".format(id)

    fun formatInfo(value: String, unit: String): String {
        return if (isDiscovered) "$value $unit" else "??? $unit"
    }
}

data class TypeModel(
    val name: String = "",
)

fun Type.toFeature(): TypeModel =
    TypeModel(
        name = name
    )

fun PokemonDetail.toFeature(): PokemonDetailModel =
    PokemonDetailModel(
        id = id,
        imageUrl = imgUrl,
        name = name,
        weight = weight,
        height = height,
        types = types.map { it.toFeature() }
    )