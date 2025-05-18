package co.kr.mvisample.feature.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import co.kr.mvisample.core.theme.PokemonTheme

@Composable
fun Modifier.pokemonCard(
    backgroundColor: Color = PokemonTheme.colors.backgroundBlack,
    border: BorderStroke? = BorderStroke(2.dp, PokemonTheme.colors.border),
    shape: Shape = RectangleShape
): Modifier =
    this
        .then(
            border?.let {
                Modifier.border(it, shape)
            } ?: Modifier
        )
        .clip(shape)
        .background(color = backgroundColor)