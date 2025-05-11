package co.kr.mvisample.feature.detail.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import co.kr.mvisample.core.navigation.PokemonRoutes
import co.kr.mvisample.data.model.PokemonDetail
import co.kr.mvisample.data.repository.PokemonRepository
import co.kr.mvisample.feature.base.BaseViewModel
import co.kr.mvisample.feature.base.UiState
import co.kr.mvisample.feature.detail.model.DetailAction
import co.kr.mvisample.feature.detail.model.DetailEvent
import co.kr.mvisample.feature.detail.model.DetailUiState
import co.kr.mvisample.feature.detail.model.PokemonDetailModel
import co.kr.mvisample.feature.detail.model.toFeature
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val pokemonRepository: PokemonRepository,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<DetailAction, UiState<DetailUiState>, DetailEvent>(
    initialState = UiState(
        content = DetailUiState(
            pokemonDetail = savedStateHandle.toRoute<PokemonRoutes.PokemonDetail>().let {
                PokemonDetailModel(
                    id = it.id,
                    name = it.name,
                    isDiscovered = it.isDiscovered
                )
            }
        )
    )
) {

    init {
        handleAction(
            uiState.value.content.pokemonDetail.let {
                DetailAction.FetchPokemonDetail(
                    id = it.id,
                    name = it.name
                )
            }
        )
    }

    override fun handleAction(action: DetailAction) {
        when (action) {
            is DetailAction.FetchPokemonDetail -> handleFetchPokemonDetail(action.id, action.name)
            DetailAction.OnBackClick -> handleOnBackClick()
        }
    }

    private fun handleFetchPokemonDetail(id: Int, name: String) {
        launch {
            pokemonRepository.fetchPokemonDetail(id, name)
                .resultCollect<PokemonDetail, DetailUiState>(
                    onSuccess = { pokemonDetail ->
                        copy(
                            pokemonDetail = this.pokemonDetail.copy(
                                weight = pokemonDetail.weight,
                                height = pokemonDetail.height,
                                types = pokemonDetail.types.map { it.toFeature() }
                            )
                        )
                    },
                    onError = {
                        it.printStackTrace()
                    },
                    onLoading = { pokemonDetail ->
                        pokemonDetail?.let {
                            copy(
                                pokemonDetail = this.pokemonDetail.copy(
                                    imageUrl = pokemonDetail.imgUrl
                                )
                            )
                        } ?: this
                    }
                )
        }
    }

    private fun handleOnBackClick() {
        launch {
            sendEvent(DetailEvent.OnNavigateToBack)
        }
    }
}