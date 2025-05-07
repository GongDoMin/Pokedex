package co.kr.mvisample.feature.home.pokedex

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.offset
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import co.kr.mvisample.feature.components.HeightSpacer
import co.kr.mvisample.feature.components.OverlayWithLoadingAndDialog
import co.kr.mvisample.feature.components.WeightSpacer
import co.kr.mvisample.feature.components.WidthSpacer
import co.kr.mvisample.feature.components.pokemonCard
import co.kr.mvisample.feature.components.sharedElement
import co.kr.mvisample.feature.home.pokedex.model.PokedexAction
import co.kr.mvisample.feature.home.pokedex.model.PokedexEvent
import co.kr.mvisample.feature.home.pokedex.model.PokemonModel
import co.kr.mvisample.feature.home.pokedex.presentation.PokedexViewModel
import co.kr.mvisample.theme.PokemonTheme
import co.kr.mvisample.utils.LaunchedEventEffect
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import kotlinx.coroutines.flow.flowOf

@Composable
fun PokedexScreen(
    onNavigateToPokemonDetail: (pokemonName: String, isDiscovered: Boolean) -> Unit,
    modifier: Modifier = Modifier,
    pokedexViewModel: PokedexViewModel = hiltViewModel(),
) {
    val uiState by pokedexViewModel.uiState.collectAsStateWithLifecycle()
    val pokemons = pokedexViewModel.pokemons.collectAsLazyPagingItems()

    LaunchedEventEffect(
        event = pokedexViewModel.event,
        collector = {
            when (it) {
                is PokedexEvent.OnNavigateToDetail -> { onNavigateToPokemonDetail(it.pokemonName, it.isDiscovered) }
            }
        }
    )

    OverlayWithLoadingAndDialog(
        isLoading = uiState.loading.isLoading,
        isError = uiState.error.isError,
        errorTitle = uiState.error.errorTitle,
        errorContent = uiState.error.errorContent,
        onSendAction = pokedexViewModel::handleBasicDialogAction
    ) {
        PokedexContent(
            modifier = modifier,
            pokemonCount = pokemons.itemCount,
            pokemonKeys = pokemons.itemKey { it.id },
            pokemon = { pokemons[it] },
            onSendAction = pokedexViewModel::handleAction,
            selectedPokemon = uiState.content.selectedPokemon
        )
    }
}

@Composable
private fun PokedexContent(
    pokemonCount: Int,
    pokemonKeys: (index: Int) -> Any,
    pokemon: (index: Int) -> PokemonModel?,
    onSendAction: (PokedexAction) -> Unit,
    modifier: Modifier = Modifier,
    selectedPokemon: PokemonModel? = null
) {
    Row(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PokemonProfile(
                pokemon = selectedPokemon
            )
            WeightSpacer()
            OptionButton(
                onClickButton = {
                    onSendAction(PokedexAction.OnClickOptionButton)
                }
            )
        }
        WidthSpacer(16.dp)
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .pokemonCard()
                .padding(8.dp)
        ) {
            items(
                count = pokemonCount,
                key = pokemonKeys
            ) {
                pokemon(it)?.let { pokemonModel ->
                    PokemonName(
                        pokemon = pokemonModel,
                        onClickPokemon = { onSendAction(PokedexAction.OnClickPokemon(it)) },
                        isSelected = selectedPokemon?.id == pokemonModel.id
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun PokemonProfile(
    pokemon: PokemonModel?,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val selectedItemPainter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(context)
            .data(pokemon?.imageUrl)
            .build()
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .pokemonCard(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier
                .aspectRatio(1f)
                .padding(8.dp)
                .background(PokemonTheme.colors.backgroundGreen)
                .sharedElement(
                    key = "image" + pokemon?.id
                ),
            painter = selectedItemPainter,
            contentDescription = null,
            colorFilter = if (pokemon?.isDiscovered == true) null else ColorFilter.tint(PokemonTheme.colors.backgroundBlack)
        )
        HeightSpacer(4.dp)
        Text(
            modifier = Modifier
                .sharedElement(
                    key = "number" + pokemon?.id
                ),
            text = pokemon?.formatNumber() ?: "",
            style = PokemonTheme.typography.titleLarge,
            color = PokemonTheme.colors.basicText
        )
    }
}

@Composable
fun OptionButton(
    onClickButton: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .pokemonCard(shape = RoundedCornerShape(16.dp))
            .clickable {
                onClickButton()
            },
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "SELECT",
            style = PokemonTheme.typography.titleMedium,
            color = PokemonTheme.colors.basicText
        )
        Icon(
            imageVector = Icons.Default.PlayArrow,
            contentDescription = null,
            tint = PokemonTheme.colors.basicText
        )
        Text(
            text = "상 세",
            style = PokemonTheme.typography.titleMedium,
            color = PokemonTheme.colors.basicText
        )
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun PokemonName(
    pokemon: PokemonModel,
    onClickPokemon: (PokemonModel) -> Unit,
    isSelected: Boolean,
    modifier: Modifier = Modifier
) {
    SubcomposeLayout(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = if (isSelected) 1.dp else 0.dp,
                color = if (isSelected) Color.White else Color.Transparent
            )
            .clickable { onClickPokemon(pokemon) }
    ) { constraints ->
        val iconPlaceables = subcompose("icon") {
            if (pokemon.isDiscovered) {
                Image(
                    imageVector = Icons.Default.Done,
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(PokemonTheme.colors.basicText)
                )
            } else {
                Spacer(modifier = Modifier)
            }
        }.map { it.measure(Constraints.fixed(16.dp.roundToPx(), 16.dp.roundToPx())) }

        val spacerWidth = 4.dp.roundToPx()

        val textPlaceables = subcompose("text") {
            Text(
                modifier = Modifier
                    .sharedElement(
                        key = "name" + pokemon.id
                    ),
                text = pokemon.name,
                style = PokemonTheme.typography.titleLarge,
                color = PokemonTheme.colors.basicText
            )
        }.map { it.measure(constraints.offset(horizontal = -(16.dp.roundToPx() + spacerWidth))) }

        val height = maxOf(
            iconPlaceables.firstOrNull()?.height ?: 0,
            textPlaceables.firstOrNull()?.height ?: 0
        )

        layout(width = constraints.maxWidth, height = height) {
            var xPosition = 0

            iconPlaceables.forEach { placeable ->
                placeable.placeRelative(x = xPosition, y = (height - placeable.height) / 2)
                xPosition += placeable.width
            }

            xPosition += spacerWidth

            textPlaceables.forEach { placeable ->
                placeable.placeRelative(x = xPosition, y = (height - placeable.height) / 2)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PokedexPreview() {
    val lazyPagingItems =
        remember {
            flowOf(
                PagingData.from(
                    listOf(
                        PokemonModel(
                            id = 1,
                            name = "Bulbasaur",
                            imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/1.png",
                            isDiscovered = true
                        ),
                        PokemonModel(
                            id = 2,
                            name = "Bulbasaur",
                            imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/1.png",
                            isDiscovered = true
                        ),
                        PokemonModel(
                            id = 3,
                            name = "Bulbasaur",
                            imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/1.png",
                            isDiscovered = true
                        ),
                        PokemonModel(
                            id = 4,
                            name = "Bulbasaur",
                            imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/1.png",
                            isDiscovered = true
                        ),
                        PokemonModel(
                            id = 5,
                            name = "Bulbasaur",
                            imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/1.png",
                            isDiscovered = false
                        ),
                        PokemonModel(
                            id = 6,
                            name = "Bulbasaur",
                            imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/1.png",
                            isDiscovered = false
                        ),
                        PokemonModel(
                            id = 7,
                            name = "Bulbasaur",
                            imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/1.png",
                            isDiscovered = false
                        ),
                        PokemonModel(
                            id = 8,
                            name = "Bulbasaur",
                            imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/1.png",
                            isDiscovered = false
                        ),
                        PokemonModel(
                            id = 9,
                            name = "Bulbasaur",
                            imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/1.png",
                            isDiscovered = false
                        ),
                        PokemonModel(
                            id = 10,
                            name = "Bulbasaur",
                            imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/1.png",
                            isDiscovered = true
                        ),
                        PokemonModel(
                            id = 11,
                            name = "Bulbasaur",
                            imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/1.png",
                            isDiscovered = false
                        ),
                        PokemonModel(
                            id = 12,
                            name = "Bulbasaur",
                            imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/1.png",
                            isDiscovered = true
                        )
                    )
                )
            )
        }.collectAsLazyPagingItems()

    val selectedItem = PokemonModel(
        id = 1,
        name = "Bulbasaur",
        imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/1.png",
        isDiscovered = true
    )

    PokemonTheme {
        PokedexContent(
            pokemonCount = lazyPagingItems.itemCount,
            pokemonKeys = lazyPagingItems.itemKey { it.id },
            pokemon = { lazyPagingItems[it] },
            onSendAction = {},
            selectedPokemon = selectedItem
        )
    }
}