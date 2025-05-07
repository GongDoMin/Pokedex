package co.kr.mvisample.feature.home.pokedex.presentation

import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import co.kr.mvisample.data.repository.PokemonRepository
import co.kr.mvisample.feature.base.BaseViewModel
import co.kr.mvisample.feature.base.UiState
import co.kr.mvisample.feature.home.pokedex.model.PokedexAction
import co.kr.mvisample.feature.home.pokedex.model.PokedexEvent
import co.kr.mvisample.feature.home.pokedex.model.PokedexUiState
import co.kr.mvisample.feature.home.pokedex.model.toFeature
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class PokedexViewModel @Inject constructor(
    pokemonRepository: PokemonRepository
) : BaseViewModel<PokedexAction, UiState<PokedexUiState>, PokedexEvent>(
    initialState = UiState(content = PokedexUiState())
) {
    val pokemons = pokemonRepository.fetchPokemons()
        .map {
            it.map {
                it.toFeature()
            }
        }.cachedIn(viewModelScope)

    override fun handleAction(action: PokedexAction) {
        when (action) {
            is PokedexAction.OnClickPokemon -> handleOnClickPokemon(action)
            PokedexAction.OnClickOptionButton -> handleOnClickOptionButton()
        }
    }

    private fun handleOnClickPokemon(action: PokedexAction.OnClickPokemon) {
        updateUiState {
            it.copy(
                content = it.content.copy(
                    selectedPokemon = action.pokemon
                )
            )
        }
    }

    private fun handleOnClickOptionButton() {
        val selectedPokemon = uiState.value.content.selectedPokemon
        when {
            selectedPokemon == null -> {
                updateErrorState(
                    isError = true,
                    errorTitle = "선택된 포켓몬이 없습니다.",
                    errorContent = "포켓몬을 선택한 후 다시 시도해주세요."
                )
            }
            else -> {
                sendEvent(PokedexEvent.OnNavigateToDetail(selectedPokemon.name, selectedPokemon.isDiscovered))
            }
        }
    }
}