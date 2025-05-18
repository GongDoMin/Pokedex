package co.kr.mvisample.feature.home.computer

import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.kr.mvisample.core.theme.PokemonTheme
import co.kr.mvisample.feature.R
import co.kr.mvisample.feature.components.OverlayWithLoadingAndDialog
import co.kr.mvisample.feature.components.pokemonCard
import co.kr.mvisample.feature.home.computer.model.ComputerAction
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
            pokemons = uiState.content.pokemonIcons,
            selectedPokemon = uiState.content.selectedPokemonIcon,
            onClickPokemon = { computerViewModel.handleAction(ComputerAction.OnPokemonIconClick(it)) }
        )
    }
}

@Composable
private fun PokemonIconGrid(
    pokemons: List<PokemonIconModel>,
    selectedPokemon: PokemonIconModel?,
    onClickPokemon: (PokemonIconModel) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val density = LocalDensity.current

    val infinityTransition = rememberInfiniteTransition()

    val scaleX by infinityTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0.95f,
        animationSpec = InfiniteRepeatableSpec(
            animation = tween(durationMillis = 300),
            repeatMode = RepeatMode.Reverse
        )
    )

    val scaleY by infinityTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.1f,
        animationSpec = InfiniteRepeatableSpec(
            animation = tween(durationMillis = 300),
            repeatMode = RepeatMode.Reverse
        )
    )

    LazyVerticalGrid(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp)
            .pokemonCard()
            .padding(8.dp),
        columns = GridCells.Fixed(6)
    ) {
        items(
            count = pokemons.size,
            key = { pokemons[it].id }
        ) {
            val pokemon = pokemons[it]

            val offset by animateIntOffsetAsState(
                targetValue = if (pokemon.id == selectedPokemon?.id) IntOffset(0, with (density) { 8.dp.roundToPx().times(-1) }) else IntOffset(0, 0),
                animationSpec = tween(durationMillis = 300)
            )

            Image(
                modifier = Modifier
                    .aspectRatio(1f)
                    .clickable { onClickPokemon(pokemon) }
                    .offset { offset }
                    .graphicsLayer {
                        this.scaleX = scaleX
                        this.scaleY = scaleY
                    }.animateItem(),
                painter = rememberAsyncImagePainter(
                    model = ImageRequest.Builder(context)
                        .data(pokemon.iconUrl)
                        .apply {
                            if (LocalInspectionMode.current) {
                                placeholder(R.drawable.img_charizard_icon)
                            }
                        }
                        .build()
                ),
                contentDescription = null
            )
        }
    }
}

@Preview
@Composable
private fun PokemonIconGridPreview() {
    val pokemons = remember {
        listOf(
            PokemonIconModel(
                id = 1,
                iconUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-vii/icons/6.png",
                order = 0
            ),
            PokemonIconModel(
                id = 2,
                iconUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-vii/icons/6.png",
                order = 1
            ),
            PokemonIconModel(
                id = 3,
                iconUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-vii/icons/6.png",
                order = 2
            ),
            PokemonIconModel(
                id = 4,
                iconUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-vii/icons/6.png",
                order = 3
            ),
        )
    }

    var selectedPokemon: PokemonIconModel? by remember {
        mutableStateOf(null)
    }

    PokemonTheme {
        PokemonIconGrid(
            pokemons = pokemons,
            selectedPokemon = selectedPokemon,
            onClickPokemon = { selectedPokemon = it }
        )
    }
}