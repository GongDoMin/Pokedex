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

    fun getImageUrl() : String {
        val number = getId()
        return "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/" +
                "pokemon/other/official-artwork/$number.png"
    }
}