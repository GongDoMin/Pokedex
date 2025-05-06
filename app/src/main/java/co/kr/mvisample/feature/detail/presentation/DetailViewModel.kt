package co.kr.mvisample.feature.detail.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import co.kr.mvisample.data.repository.PokemonRepository
import co.kr.mvisample.feature.base.BaseViewModel
import co.kr.mvisample.feature.base.UiState
import co.kr.mvisample.feature.detail.model.DetailAction
import co.kr.mvisample.feature.detail.model.DetailEvent
import co.kr.mvisample.feature.detail.model.DetailUiState
import co.kr.mvisample.feature.detail.model.PokemonDetailModel
import co.kr.mvisample.feature.detail.model.toFeature
import co.kr.mvisample.navigation.PokemonRoutes
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val pokemonRepository: PokemonRepository,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<DetailAction, UiState<DetailUiState>, DetailEvent>(
    initialState = UiState(
        content = DetailUiState(
            pokemonDetail = PokemonDetailModel(
                isDiscovered = savedStateHandle.toRoute<PokemonRoutes.PokemonDetail>().isDiscovered
            )
        )
    )
) {
    private val routes = savedStateHandle.toRoute<PokemonRoutes.PokemonDetail>()

    init {
        handleAction(DetailAction.SystemAction.FetchPokemonDetail(routes.pokemonName))
    }

    override fun handleAction(action: DetailAction) {
        when (action) {
            is DetailAction.SystemAction.FetchPokemonDetail -> handleFetchPokemonDetail(action.pokemonName)
        }
    }

    private fun handleFetchPokemonDetail(pokemonName: String) {
        launch {
            pokemonRepository.fetchPokemonDetail(pokemonName)
                .resultCollect(
                    onSuccess = { pokemonDetail ->
                        val feature = pokemonDetail.toFeature()
                        updateUiState {
                            val currentDetail = it.content.pokemonDetail
                            val updatedDetail = currentDetail.copy(
                                id = feature.id,
                                imgUrl = feature.imgUrl,
                                name = feature.name,
                                weight = feature.weight,
                                height = feature.height,
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
}