package co.kr.mvisample.feature.detail.model

import co.kr.mvisample.data.model.Ability
import co.kr.mvisample.data.model.PokemonAbility
import co.kr.mvisample.data.model.PokemonDetail
import co.kr.mvisample.data.model.Type

data class PokemonDetailModel(
    val id: Int = 0,
    val imgUrl: String = "",
    val name: String = "",
    val weight: Float = 0f,
    val height: Float = 0f,
    val isDiscovered: Boolean = false,
    val types: List<TypeModel> = emptyList(),
    val abilities: List<PokemonAbilityModel> = emptyList()
) {
    fun formatNumber(): String = "%04d".format(id)

    fun formatInfo(value: String, unit: String): String {
        return if (isDiscovered) "$value $unit" else "??? $unit"
    }
}

data class TypeModel(
    val name: String = "",
)

data class PokemonAbilityModel(
    val ability: AbilityModel = AbilityModel(),
    val isHidden: Boolean = false,
)

data class AbilityModel(
    val name: String = "",
    val url: String = ""
)

fun Ability.toFeature(): AbilityModel =
    AbilityModel(
        name = name
    )

fun PokemonAbility.toFeature(): PokemonAbilityModel =
    PokemonAbilityModel(
        ability = ability.toFeature(),
        isHidden = isHidden
    )

fun Type.toFeature(): TypeModel =
    TypeModel(
        name = name
    )

fun PokemonDetail.toFeature(): PokemonDetailModel =
    PokemonDetailModel(
        id = id,
        imgUrl = imgUrl,
        name = name,
        weight = weight,
        height = height,
        types = types.map { it.toFeature() },
        abilities = abilities.map { it.toFeature() }
    )