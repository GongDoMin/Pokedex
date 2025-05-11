package co.kr.mvisample.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PokemonDetailResponse(
    @SerialName("id") val id: Int = 0,
    @SerialName("name") val name: String = "",
    @SerialName("weight") val weight: Int = 0,
    @SerialName("height") val height: Int = 0,
    @SerialName("types") val types: List<TypeWithSlotResponse> = emptyList()
)

@Serializable
data class TypeWithSlotResponse(
    @SerialName("slot") val slot: Int = 0,
    @SerialName("type") val type: TypeResponse = TypeResponse(),
)

@Serializable
data class TypeResponse(
    @SerialName("name") val name: String = "",
    @SerialName("url") val url: String = ""
)