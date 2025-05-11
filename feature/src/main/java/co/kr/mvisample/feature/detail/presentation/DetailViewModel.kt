package co.kr.mvisample.feature.detail.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import co.kr.mvisample.core.navigation.PokemonRoutes
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
                .resultCollect(
                    onLoading = { pokemonDetail ->
                        pokemonDetail?.let {
                            updateUiState {
                                val currentDetail = it.content.pokemonDetail
                                val updatedDetail = currentDetail.copy(
                                    id = pokemonDetail.id,
                                    name = pokemonDetail.name,
                                    imageUrl = pokemonDetail.imgUrl
                                )
                                val updatedContent = it.content.copy(
                                    pokemonDetail = updatedDetail
                                )
                                it.copy(content = updatedContent)
                            }
                        }
                    },
                    onSuccess = { pokemonDetail ->
                        val feature = pokemonDetail.toFeature()
                        updateUiState {
                            val currentDetail = it.content.pokemonDetail
                            val updatedDetail = currentDetail.copy(
                                weight = feature.weight,
                                height = feature.height,
                                types = feature.types
                            )
                            val updatedContent = it.content.copy(
                                pokemonDetail = updatedDetail
                            )
                            it.copy(content = updatedContent)
                        }
                    },
                    onError = {
                        it.printStackTrace()
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