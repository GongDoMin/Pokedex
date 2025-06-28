package co.kr.mvisample.feature.home.computer.model

import androidx.compose.runtime.Immutable
import co.kr.mvisample.data.model.PokemonIcon

@Immutable
data class PokemonIconModel(
    val id: Int = 0,
    val name: String = "",
    val iconUrl: String = "",
    val order: Int = 0
)

fun PokemonIcon.toFeature() =
    PokemonIconModel(
        id = id,
        name = name,
        iconUrl = iconUrl,
        order = order
    )