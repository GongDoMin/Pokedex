package co.kr.mvisample.data.model

import co.kr.mvisample.remote.model.AbilityResponse
import co.kr.mvisample.remote.model.PokemonAbilityResponse
import co.kr.mvisample.remote.model.TypeWithSlotResponse

data class PokemonDetail(
    val id: Int = 0,
    val imgUrl: String = "",
    val name: String = "",
    val weight: Float = 0f,
    val height: Float = 0f,
    val types: List<Type> = emptyList(),
    val abilities: List<PokemonAbility> = emptyList()
)

data class Type(
    val name: String = ""
)

data class PokemonAbility(
    val ability: Ability = Ability(),
    val isHidden: Boolean = false,
)

data class Ability(
    val name: String = ""
)

fun AbilityResponse.toData(): Ability =
    Ability(
        name = name
    )

fun PokemonAbilityResponse.toData(): PokemonAbility =
    PokemonAbility(
        ability = ability.toData(),
        isHidden = isHidden
    )

fun TypeWithSlotResponse.toData(): Type =
    Type(
        name = type.name,
    )
