package co.kr.mvisample.feature.detail.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import co.kr.mvisample.core.navigation.PokemonRoutes
import co.kr.mvisample.data.repository.PokemonRepository
import co.kr.mvisample.feature.base.BaseViewModel
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
) : BaseViewModel<DetailAction, DetailUiState, DetailEvent>(
    initialState = DetailUiState(
        pokemonDetail = PokemonDetailModel(
            id = savedStateHandle.get<Int>(ID) ?: 0,
            name = savedStateHandle.get<String>(NAME) ?: "",
            isDiscovered = savedStateHandle.get<Boolean>(IS_DISCOVERED) ?: false
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
                    onSuccess = { pokemonDetail ->
                        copy(
                            pokemonDetail = this.pokemonDetail.copy(
                                weight = pokemonDetail.weight,
                                height = pokemonDetail.height,
                                types = pokemonDetail.types.map { it.toFeature() }
                            )
                        )
                    },
                    onLoading = { pokemonDetail ->
                        copy(
                            pokemonDetail = this.pokemonDetail.copy(
                                imageUrl = pokemonDetail?.imgUrl ?: ""
                            )
                        )
                    }
                )
        }
    }

    private fun handleOnBackClick() {
        launch {
            sendEvent(DetailEvent.OnNavigateToBack)
        }
    }

    companion object {
        const val ID = "id"
        const val NAME = "name"
        const val IS_DISCOVERED = "isDiscovered"
    }
}