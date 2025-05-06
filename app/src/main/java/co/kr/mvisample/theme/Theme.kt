package co.kr.mvisample.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle

@Composable
fun PokemonTheme(
    colors: PokemonColors = PokemonColors(),
    typography: PokemonTypography = PokemonTypography(),
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalPokemonColors provides colors,
        LocalPokemonTypography provides typography,
        content = content
    )
}

object PokemonTheme {
    val colors: PokemonColors
        @Composable
        get() = LocalPokemonColors.current

    val typography: PokemonTypography
        @Composable
        get() = LocalPokemonTypography.current
}

private val LocalPokemonColors = staticCompositionLocalOf<PokemonColors> {
    error("No PokemonColors provided")
}

private val LocalPokemonTypography = staticCompositionLocalOf<PokemonTypography> {
    error("No PokemonTypography provided")
}

@Immutable
data class PokemonColors(
    val backgroundBlack: Color = BackgroundBlack,
    val backgroundRed: Color = BackgroundRed,
    val backgroundGreen: Color = BackgroundGreen,
    val border: Color = Border,
    val basicText: Color = BasicText
)

@Immutable
data class PokemonTypography(
    val displayLarge: TextStyle = Typography.displayLarge,
    val titleLarge: TextStyle = Typography.titleLarge,
    val titleMedium: TextStyle = Typography.titleMedium,
    val bodyLarge: TextStyle = Typography.bodyLarge,
    val bodyMedium: TextStyle = Typography.bodyMedium,
    val labelLarge: TextStyle = Typography.labelLarge
)