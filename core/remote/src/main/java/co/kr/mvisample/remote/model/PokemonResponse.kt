package co.kr.mvisample.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PokemonResponseWithPaging(
    @SerialName("count") val count: Int = 0,
    @SerialName("next") val next: String = "",
    @SerialName("previous") val previous: String = "",
    @SerialName("results") val results: List<PokemonResponse> = emptyList(),
)

@Serializable
data class PokemonResponse(
    @SerialName("name") val name: String = "",
    @SerialName("url") val url: String = ""
) {
    fun getId() : Int = url.split("/".toRegex()).dropLast(1).last().toInt()
}