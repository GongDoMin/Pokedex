package co.kr.mvisample.feature.home.pokedex

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import co.kr.mvisample.core.theme.PokemonTheme
import co.kr.mvisample.core.theme.PreviewPokemonTheme
import co.kr.mvisample.core.utils.LaunchedEventEffect
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
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import kotlinx.coroutines.flow.flowOf

@Composable
fun PokedexScreen(
    onNavigateToPokemonDetail: (id: Int, name: String, isDiscovered: Boolean) -> Unit,
    modifier: Modifier = Modifier,
    pokedexViewModel: PokedexViewModel = hiltViewModel(),
) {
    val uiState by pokedexViewModel.uiState.collectAsStateWithLifecycle()
    val pokemons = pokedexViewModel.pokemons.collectAsLazyPagingItems()

    LaunchedEventEffect(
        event = pokedexViewModel.event,
        collector = {
            when (it) {
                is PokedexEvent.OnNavigateToDetail -> { onNavigateToPokemonDetail(it.id, it.name, it.isDiscovered) }
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
            pokemons = pokemons,
            onSendAction = pokedexViewModel::handleAction,
            selectedPokemon = uiState.content.selectedPokemon
        )
    }
}

@Composable
private fun PokedexContent(
    pokemons: LazyPagingItems<PokemonModel>,
    onSendAction: (PokedexAction) -> Unit,
    modifier: Modifier = Modifier,
    selectedPokemon: PokemonModel? = null
) {
    Row(
        modifier = modifier
            .fillMaxSize()
            .background(PokemonTheme.colors.backgroundRed)
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
            PokedexActionButtons(
                isDiscovered = selectedPokemon?.isDiscovered == true,
                onCatchClick = {},
                onDiscoverClick = {},
                onDetailClick = { onSendAction(PokedexAction.OnClickOptionButton) }
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
                count = pokemons.itemCount,
                key = pokemons.itemKey { it.id }
            ) {
                pokemons[it]?.let { pokemonModel ->
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
fun PokedexActionButtons(
    isDiscovered: Boolean,
    onCatchClick: () -> Unit,
    onDiscoverClick: () -> Unit,
    onDetailClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column (
        modifier = modifier
            .fillMaxWidth()
            .pokemonCard()
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (isDiscovered) {
            PokedexActionButton(
                text = "포획하기",
                onClick = onCatchClick
            )
        }
        if (!isDiscovered) {
            PokedexActionButton(
                text = "발견하기",
                onClick = onDiscoverClick
            )
        }
        PokedexActionButton(
            text = "상세보기",
            onClick = onDetailClick
        )
    }
}

@Composable
private fun PokedexActionButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: TextStyle = PokemonTheme.typography.titleMedium,
    color: Color = PokemonTheme.colors.basicText
) {
    Text(
        text = text,
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        style = style,
        color = color
    )
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun PokemonName(
    pokemon: PokemonModel,
    onClickPokemon: (PokemonModel) -> Unit,
    isSelected: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = if (isSelected) 1.dp else 0.dp,
                color = if (isSelected) Color.White else Color.Transparent
            )
            .clickable { onClickPokemon(pokemon) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (pokemon.isDiscovered) {
            Icon(
                imageVector = Icons.Default.Done,
                contentDescription = null,
                tint = PokemonTheme.colors.basicText,
                modifier = Modifier
                    .size(16.dp)
            )
            WidthSpacer(4.dp)
        } else {
            WidthSpacer(20.dp)
        }
        Text(
            modifier = Modifier.sharedElement(key = "name" + pokemon.id),
            text = if (pokemon.isDiscovered) pokemon.name else "???",
            style = PokemonTheme.typography.titleLarge,
            color = PokemonTheme.colors.basicText
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PokedexPreview() {
    val pokemons =
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

    val selectedPokemon = PokemonModel(
        id = 1,
        name = "Bulbasaur",
        imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/1.png",
        isDiscovered = true
    )

    PreviewPokemonTheme {
        PokedexContent(
            pokemons = pokemons,
            onSendAction = {},
            selectedPokemon = selectedPokemon
        )
    }
}