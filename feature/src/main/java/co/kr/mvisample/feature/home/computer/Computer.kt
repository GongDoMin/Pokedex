package co.kr.mvisample.feature.home.computer

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.kr.mvisample.core.theme.PokemonTheme
import co.kr.mvisample.feature.components.OverlayWithLoadingAndDialog
import co.kr.mvisample.feature.components.pokemonCard
import co.kr.mvisample.feature.home.computer.model.PokemonIconModel
import co.kr.mvisample.feature.home.computer.presentation.ComputerViewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest

@Composable
fun ComputerScreen(
    computerViewModel: ComputerViewModel = hiltViewModel()
) {
    val uiState by computerViewModel.uiState.collectAsStateWithLifecycle()

    OverlayWithLoadingAndDialog(
        isLoading = uiState.loading.isLoading,
        isError = uiState.error.isError,
        errorTitle = uiState.error.errorTitle,
        errorContent = uiState.error.errorContent,
        onSendAction = computerViewModel::handleBasicDialogAction
    ) {
        PokemonIconGrid(
            pokemons = uiState.content.pokemonIcons
        )
    }
}

@Composable
fun PokemonIconGrid(
    pokemons: List<PokemonIconModel>,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Layout(
        modifier = modifier
            .fillMaxSize()
            .background(PokemonTheme.colors.backgroundRed)
            .padding(8.dp)
            .pokemonCard()
            .padding(8.dp),
        content = {
            pokemons.forEach {
                val painter = rememberAsyncImagePainter(
                    model = ImageRequest.Builder(context)
                        .data(it.iconUrl)
                        .crossfade(true)
                        .build()
                )
                Image(
                    painter = painter,
                    contentDescription = null,
                    modifier = Modifier
                        .aspectRatio(1f)
                )
            }
        }
    ) { measurables, constraints ->
        val maxWidth = constraints.maxWidth
        val itemSize = maxWidth / 6
        val placeables = measurables.map {
            it.measure(Constraints.fixed(itemSize, itemSize))
        }

        layout(maxWidth, constraints.maxHeight) {
            var xPosition = 0
            var yPosition = 0

            placeables.forEach { placeable ->
                if (xPosition + itemSize > maxWidth) {
                    xPosition = 0
                    yPosition += itemSize
                }
                placeable.placeRelative(x = xPosition, y = yPosition)
                xPosition += itemSize
            }
        }
    }
}