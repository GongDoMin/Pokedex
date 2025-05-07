package co.kr.mvisample.feature.detail

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.kr.mvisample.feature.components.HeightSpacer
import co.kr.mvisample.feature.components.OverlayWithLoadingAndDialog
import co.kr.mvisample.feature.components.pokemonCard
import co.kr.mvisample.feature.components.sharedElement
import co.kr.mvisample.feature.detail.model.PokemonDetailModel
import co.kr.mvisample.feature.detail.presentation.DetailViewModel
import co.kr.mvisample.core.theme.PokemonTheme
import co.kr.mvisample.core.utils.LaunchedEventEffect
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest

@Composable
fun DetailScreen(
    viewModel: DetailViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEventEffect(
        event = viewModel.event,
        collector = {
            when (it) {
                else -> {}
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
        DetailContent(
            modifier = Modifier,
            pokemonDetail = uiState.content.pokemonDetail
        )
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun DetailContent(
    pokemonDetail: PokemonDetailModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(context)
            .data(pokemonDetail.imgUrl)
            .build()
    )
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(PokemonTheme.colors.backgroundRed)
            .padding(16.dp)
            .pokemonCard()
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
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
                colorFilter = if (pokemonDetail.isDiscovered) null else ColorFilter.tint(
                    PokemonTheme.colors.backgroundBlack)
            )
            HeightSpacer(4.dp)
            PokemonInfoRow(
                modifier = Modifier
                    .sharedElement(
                        key = "name" + pokemonDetail.id
                    ),
                label = "이름",
                value = pokemonDetail.name
            )
            PokemonInfoRow(
                modifier = Modifier
                    .sharedElement(
                        key = "number" + pokemonDetail.id
                    ),
                label = "번호",
                value = pokemonDetail.formatNumber()
            )
            PokemonInfoRow(
                label = "키",
                value = pokemonDetail.formatInfo(pokemonDetail.height.toString(), "M")
            )
            PokemonInfoRow(
                label = "몸무게",
                value = pokemonDetail.formatInfo(pokemonDetail.weight.toString(), "KG")
            )
        }
    }
}

@Composable
fun PokemonInfoRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
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