package co.kr.mvisample.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PokemonDetailResponse(
    @SerialName("id") val id: Int = 0,
    @SerialName("name") val name: String = "",
    @SerialName("weight") val weight: Int = 0,
    @SerialName("height") val height: Int = 0,
    @SerialName("types") val types: List<TypeWithSlotResponse> = emptyList(),
    @SerialName("abilities") val abilities: List<PokemonAbilityResponse> = emptyList(),
) {
    fun getImageUrl() : String =
        "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/" +
                "pokemon/other/official-artwork/$id.png"
}

@Serializable
data class TypeWithSlotResponse(
    @SerialName(value = "slot") val slot: Int = 0,
    @SerialName(value = "type") val type: TypeResponse = TypeResponse(),
)

@Serializable
data class TypeResponse(
    @SerialName(value = "name") val name: String = "",
    @SerialName(value = "url") val url: String = ""
)

@Serializable
data class PokemonAbilityResponse(
    @SerialName("ability") val ability: AbilityResponse = AbilityResponse(),
    @SerialName("is_hidden") val isHidden: Boolean = false,
    @SerialName("slot") val slot: Int = 0
)

@Serializable
data class AbilityResponse(
    @SerialName("name") val name: String = "",
    @SerialName("url") val url: String = ""
)