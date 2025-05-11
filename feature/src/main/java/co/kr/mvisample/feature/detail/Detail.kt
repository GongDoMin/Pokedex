package co.kr.mvisample.feature.detail

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.kr.mvisample.core.theme.PokemonTheme
import co.kr.mvisample.core.utils.LaunchedEventEffect
import co.kr.mvisample.feature.components.OverlayWithLoadingAndDialog
import co.kr.mvisample.feature.components.pokemonCard
import co.kr.mvisample.feature.components.sharedElement
import co.kr.mvisample.feature.detail.model.DetailAction
import co.kr.mvisample.feature.detail.model.DetailEvent
import co.kr.mvisample.feature.detail.model.PokemonDetailModel
import co.kr.mvisample.feature.detail.presentation.DetailViewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest

@Composable
fun DetailScreen(
    onNavigateToBack: () -> Unit,
    viewModel: DetailViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEventEffect(
        event = viewModel.event,
        collector = {
            when (it) {
                DetailEvent.OnNavigateToBack -> onNavigateToBack()
            }
        }
    )

    OverlayWithLoadingAndDialog(
        isLoading = uiState.loading.isLoading,
        isError = uiState.error.isError,
        errorTitle = uiState.error.errorTitle,
        errorContent = uiState.error.errorContent,
        onSendAction = viewModel::handleBasicDialogAction
    ) {
        Column {
            DetailHeader(
                pokemonDetail = uiState.content.pokemonDetail,
                onBackClick = { viewModel.handleAction(DetailAction.OnBackClick) }
            )

            DetailContent(
                modifier = Modifier,
                pokemonDetail = uiState.content.pokemonDetail
            )
        }
    }
}

@Composable
fun DetailHeader(
    pokemonDetail: PokemonDetailModel,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    SubcomposeLayout(
        modifier = modifier
            .fillMaxWidth()
            .background(PokemonTheme.colors.backgroundRed)
            .padding(16.dp)
    ) { constraints ->
        val textPlaceable = subcompose("text") {
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = if (pokemonDetail.isDiscovered) pokemonDetail.name else "???",
                style = PokemonTheme.typography.titleLarge,
                color = Color.White,
                textAlign = TextAlign.Center
            )
        }.map { it.measure(constraints) }.first()

        val iconSize = 24.dp.roundToPx()
        val iconPlaceable = subcompose("icon") {
            Image(
                modifier = Modifier
                    .clickable(
                        onClick = onBackClick
                    ),
                imageVector = Icons.Default.Close,
                contentDescription = null,
                colorFilter = ColorFilter.tint(Color.White)
            )
        }.map { it.measure(Constraints.fixed(width = iconSize, height = iconSize)) }.first()

        val textHeight = textPlaceable.height

        layout(constraints.maxWidth, textHeight.coerceAtLeast(16)) {
            iconPlaceable.placeRelative(0, 0)
            textPlaceable.placeRelative(0, 0)
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun DetailContent(
    pokemonDetail: PokemonDetailModel,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(PokemonTheme.colors.backgroundRed)
            .padding(16.dp)
            .pokemonCard()
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        PokemonImage(
            pokemonDetail = pokemonDetail
        )
        PokemonInfoRow(
            modifier = Modifier
                .sharedElement(
                    key = "number" + pokemonDetail.id
                ),
            label = "Number",
            value = pokemonDetail.formatNumber()
        )
        PokemonInfoRow(
            modifier = Modifier
                .sharedElement(
                    key = "name" + pokemonDetail.id
                ),
            label = "Name",
            value = if (pokemonDetail.isDiscovered) pokemonDetail.name else "???"
        )
        PokemonInfoRow(
            label = "Height",
            value = pokemonDetail.formatInfo(pokemonDetail.height.toString(), "M")
        )
        PokemonInfoRow(
            label = "Weight",
            value = pokemonDetail.formatInfo(pokemonDetail.weight.toString(), "KG")
        )
        PokemonInfoRow(
            label = "Type",
            value = pokemonDetail.types.joinToString("\n") { if (pokemonDetail.isDiscovered) it.name else "???" },
            verticalAlignment = Alignment.Top
        )
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun PokemonImage(
    pokemonDetail: PokemonDetailModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(context)
            .data(pokemonDetail.imageUrl)
            .build()
    )

    Box(
        modifier = modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.TopCenter
    ) {
        Image(
            modifier = Modifier
                .aspectRatio(1f)
                .background(Color.White)
                .sharedElement(
                    key = "image" + pokemonDetail.id
                ),
            painter = painter,
            contentDescription =  null,
            colorFilter = if (pokemonDetail.isDiscovered) null else ColorFilter.tint(PokemonTheme.colors.backgroundBlack)
        )
    }
}

@Composable
fun PokemonInfoRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = verticalAlignment
    ) {
        Text(
            modifier = Modifier
                .weight(1f),
            text = label,
            style = PokemonTheme.typography.titleLarge,
            color = PokemonTheme.colors.basicText
        )
        Text(
            modifier = Modifier
                .weight(2f),
            text = value,
            style = PokemonTheme.typography.titleLarge,
            color = PokemonTheme.colors.basicText
        )
    }
}