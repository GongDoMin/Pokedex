package co.kr.mvisample.feature.home.pokedex

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import co.kr.mvisample.core.utils.LaunchedEventEffect
import co.kr.mvisample.design.PokemonTheme
import co.kr.mvisample.design.PreviewPokemonTheme
import co.kr.mvisample.feature.R
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
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PokemonImage(pokemon = selectedPokemon)
            WeightSpacer()
            PokedexActionButtons(
                isDiscovered = selectedPokemon?.isDiscovered == true,
                isCaught = selectedPokemon?.isCaught == true,
                onReleaseClick = { onSendAction(PokedexAction.ReleasePokemon) },
                onCatchClick = { onSendAction(PokedexAction.AttemptCatchPokemon) },
                onDiscoverClick = { onSendAction(PokedexAction.MarkPokemonAsDiscovered) },
                onDetailClick = { onSendAction(PokedexAction.ShowPokemonDetail) }
            )
        }
        WidthSpacer(16.dp)
        LazyColumn(
            modifier = Modifier
                .weight(2f)
                .fillMaxHeight()
                .pokemonCard()
                .padding(8.dp)
                .semantics {
                    contentDescription = "pokemonList"
                }
        ) {
            items(
                count = pokemons.itemCount,
                key = pokemons.itemKey { it.id }
            ) {
                pokemons[it]?.let { pokemonModel ->
                    PokemonListItem(
                        pokemon = pokemonModel,
                        onClickPokemon = { onSendAction(PokedexAction.OnPokemonClick(it)) },
                        isSelected = selectedPokemon?.id == pokemonModel.id
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun PokemonImage(
    pokemon: PokemonModel?,
    modifier: Modifier = Modifier
) {
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
            painter = rememberAsyncImagePainter(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(pokemon?.imageUrl)
                    .apply {
                        if (LocalInspectionMode.current) {
                            placeholder(R.drawable.img_charizard)
                        }
                    }
                    .build()
            ),
            contentDescription = "pokemonImage",
            colorFilter = if (pokemon?.isDiscovered == true) null else ColorFilter.tint(PokemonTheme.colors.backgroundBlack)
        )
    }
}

@Composable
private fun PokedexActionButtons(
    isDiscovered: Boolean,
    isCaught: Boolean,
    onCatchClick: () -> Unit,
    onReleaseClick: () -> Unit,
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
            if (isCaught) {
                PokedexActionButton(
                    text = "놓아주기",
                    onClick = onReleaseClick
                )
            } else {
                PokedexActionButton(
                    text = "포획하기",
                    onClick = onCatchClick
                )
            }
        } else {
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
private fun PokemonListItem(
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
            Pokeball(isCaught = pokemon.isCaught)
            WidthSpacer(4.dp)
        } else {
            WidthSpacer(20.dp)
        }
        Text(
            modifier = Modifier
                .sharedElement(
                    key = "number" + pokemon.id
                ),
            text = pokemon.formatNumber(),
            style = PokemonTheme.typography.titleLarge,
            color = PokemonTheme.colors.basicText
        )
        WidthSpacer(4.dp)
        Text(
            modifier = Modifier.sharedElement(key = "name" + pokemon.id),
            text = if (pokemon.isDiscovered) pokemon.name else "???",
            style = PokemonTheme.typography.titleLarge,
            color = PokemonTheme.colors.basicText
        )
    }
}

@Composable
private fun Pokeball(
    isCaught: Boolean,
    modifier: Modifier = Modifier,
    caughtColorTop: Color = PokemonTheme.colors.pokeballTop,
    caughtColorBottom: Color = PokemonTheme.colors.pokeballBottom,
    uncaughtColorTop: Color = Color(0xFF888888),
    uncaughtColorBottom: Color = Color(0xFFB0B0B0),
    outlineColor: Color = PokemonTheme.colors.basicText,
    centerCircleColor: Color = Color.White
) {
    val animatedTopColor by animateColorAsState(
        targetValue = if (isCaught) caughtColorTop else uncaughtColorTop,
        animationSpec = tween(durationMillis = 300),
        label = "topColorAnimation"
    )

    val animatedBottomColor by animateColorAsState(
        targetValue = if (isCaught) caughtColorBottom else uncaughtColorBottom,
        animationSpec = tween(durationMillis = 300),
        label = "bottomColorAnimation"
    )

    Canvas(
        modifier = modifier
            .size(16.dp)
            .border(1.dp, color = outlineColor, shape = CircleShape)
            .semantics {
                contentDescription = "pokeball_$isCaught"
            }
    ) {
        val diameter = size.minDimension
        val radius = diameter / 2
        val center = Offset(size.width / 2, size.height / 2)

        drawArc(
            color = animatedTopColor,
            startAngle = 190f,
            sweepAngle = 160f,
            useCenter = true,
            topLeft = Offset(0f, 0f),
            size = Size(size.width, size.height)
        )

        drawArc(
            color = animatedBottomColor,
            startAngle = -10f,
            sweepAngle = 200f,
            useCenter = true,
            topLeft = Offset(0f, 0f),
            size = Size(size.width, size.height)
        )

        drawCircle(
            color = outlineColor,
            radius = radius * 0.3f,
            center = center
        )

        drawCircle(
            color = centerCircleColor,
            radius = radius * 0.15f,
            center = center
        )
    }
}

@Preview
@Composable
private fun PokeballPreview() {
    PokemonTheme {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Pokeball(isCaught = true)
            Pokeball(isCaught = false)
        }
    }
}

@Preview
@Composable
private fun PokemonListItemPreview() {
    PreviewPokemonTheme {
        Column {
            PokemonListItem(
                pokemon = PokemonModel(
                    id = 1,
                    name = "Charizard",
                    isDiscovered = false,
                ),
                onClickPokemon = {},
                isSelected = true
            )
            PokemonListItem(
                pokemon = PokemonModel(
                    id = 2,
                    name = "Charizard",
                    isDiscovered = true,
                    isCaught = false
                ),
                onClickPokemon = {},
                isSelected = false
            )
            PokemonListItem(
                pokemon = PokemonModel(
                    id = 3,
                    name = "Charizard",
                    isDiscovered = true,
                    isCaught = true
                ),
                onClickPokemon = {},
                isSelected = false
            )
        }
    }
}

@Preview
@Composable
private fun PokemonImagePreview() {
    PreviewPokemonTheme {
        PokemonImage(
            pokemon = PokemonModel(
                id = 1,
                name = "Charizard",
                imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-ii/gold/transparent/6.png",
                isDiscovered = true,
                isCaught = true
            )
        )
    }
}

@Preview
@Composable
private fun PokedexActionButtonsPreview() {
    PokemonTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            PokedexActionButtons(
                isDiscovered = false,
                isCaught = false,
                onCatchClick = {},
                onReleaseClick = {},
                onDiscoverClick = {},
                onDetailClick = {}
            )
            PokedexActionButtons(
                isDiscovered = true,
                isCaught = true,
                onCatchClick = {},
                onReleaseClick = {},
                onDiscoverClick = {},
                onDetailClick = {}
            )
            PokedexActionButtons(
                isDiscovered = true,
                isCaught = false,
                onCatchClick = {},
                onReleaseClick = {},
                onDiscoverClick = {},
                onDetailClick = {}
            )
        }
    }
}

@Preview
@Composable
private fun PokedexPreview() {
    val pokemons = remember {
        flowOf(
            PagingData.from(
                listOf(
                    PokemonModel(
                        id = 1,
                        name = "Charizard",
                        imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-ii/gold/transparent/6.png",
                        isDiscovered = true,
                        isCaught = true
                    ),
                    PokemonModel(
                        id = 2,
                        name = "Charizard",
                        imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-ii/gold/transparent/6.png",
                        isDiscovered = true
                    ),
                    PokemonModel(
                        id = 3,
                        name = "Charizard",
                        imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-ii/gold/transparent/6.png",
                        isDiscovered = true
                    ),
                    PokemonModel(
                        id = 4,
                        name = "Charizard",
                        imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-ii/gold/transparent/6.png",
                        isDiscovered = true
                    ),
                    PokemonModel(
                        id = 5,
                        name = "Charizard",
                        imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-ii/gold/transparent/6.png",
                    ),
                    PokemonModel(
                        id = 6,
                        name = "Charizard",
                        imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-ii/gold/transparent/6.png",
                    ),
                    PokemonModel(
                        id = 7,
                        name = "Charizard",
                        imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-ii/gold/transparent/6.png",
                    ),
                    PokemonModel(
                        id = 8,
                        name = "Charizard",
                        imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-ii/gold/transparent/6.png",
                    ),
                )
            )
        )
    }.collectAsLazyPagingItems()

    var selectedPokemon: PokemonModel? by remember {
        mutableStateOf(
            PokemonModel(
                id = 1,
                name = "Charizard",
                imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-ii/gold/transparent/6.png",
                isDiscovered = true,
                isCaught = true
            )
        )
    }

    PreviewPokemonTheme {
        PokedexContent(
            pokemons = pokemons,
            onSendAction = {
                when (it) {
                    PokedexAction.AttemptCatchPokemon -> {}
                    PokedexAction.MarkPokemonAsDiscovered -> {}
                    is PokedexAction.OnPokemonClick -> selectedPokemon = it.pokemon
                    PokedexAction.ReleasePokemon -> {}
                    PokedexAction.ShowPokemonDetail -> {}
                }
            },
            selectedPokemon = selectedPokemon
        )
    }
}