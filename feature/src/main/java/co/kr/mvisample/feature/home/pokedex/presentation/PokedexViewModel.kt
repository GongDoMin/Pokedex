package co.kr.mvisample.feature.home.pokedex.presentation

import androidx.lifecycle.viewModelScope
import androidx.paging.map
import co.kr.mvisample.common.base.BaseViewModel
import co.kr.mvisample.data.repository.PokemonRepository
import co.kr.mvisample.feature.home.pokedex.model.PokedexAction
import co.kr.mvisample.feature.home.pokedex.model.PokedexEvent
import co.kr.mvisample.feature.home.pokedex.model.PokedexUiState
import co.kr.mvisample.feature.home.pokedex.model.PokemonModel
import co.kr.mvisample.feature.home.pokedex.model.toFeature
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class PokedexViewModel @Inject constructor(
    private val pokemonRepository: PokemonRepository
) : BaseViewModel<PokedexAction, PokedexUiState, PokedexEvent>(
    initialState = PokedexUiState()
) {
    val pokemons = pokemonRepository.fetchPokemons(viewModelScope)
        .map {
            it.map {
                it.toFeature()
            }
        }

    override fun handleAction(action: PokedexAction) {
        when (action) {
            is PokedexAction.OnPokemonClick -> handleOnClickPokemon(action)
            PokedexAction.ShowPokemonDetail -> handleShowPokemonDetail()
            PokedexAction.AttemptCatchPokemon -> handleAttemptCatchPokemon()
            PokedexAction.ReleasePokemon -> handleReleasePokemon()
            PokedexAction.MarkPokemonAsDiscovered -> handleMarkPokemonAsDiscovered()
        }
    }

    private fun handleOnClickPokemon(action: PokedexAction.OnPokemonClick) {
        updateContent {
            copy(
                selectedPokemon = action.pokemon
            )
        }
    }

    private fun handleShowPokemonDetail() {
        launch {
            withSelectedPokemon(
                selectedPokemon = uiState.value.content.selectedPokemon,
                onPresent = { pokemon ->
                    sendEvent(
                        PokedexEvent.OnNavigateToDetail(
                            id = pokemon.id,
                            name = pokemon.name,
                            isDiscovered = pokemon.isDiscovered
                        )
                    )
                }
            )
        }
    }

    private fun handleMarkPokemonAsDiscovered() {
        launch {
            withSelectedPokemon(
                selectedPokemon = uiState.value.content.selectedPokemon,
                onPresent = { pokemon ->
                    pokemonRepository.markAsDiscovered(pokemon.id)
                        .resultCollect(
                            onSuccess = { _ ->
                                copy(
                                    selectedPokemon = selectedPokemon?.copy(
                                        isDiscovered = true
                                    )
                                )
                            }
                        )
                }
            )
        }
    }

    private fun handleAttemptCatchPokemon() {
        launch {
            withSelectedPokemon(
                selectedPokemon = uiState.value.content.selectedPokemon,
                onPresent = { pokemon ->
                    pokemonRepository.markAsCaught(pokemon.id)
                        .resultCollect(
                            onSuccess = { _ ->
                                copy(
                                    selectedPokemon = selectedPokemon?.copy(
                                        isCaught = true
                                    )
                                )
                            }
                        )
                }
            )
        }
    }

    private fun handleReleasePokemon() {
        launch {
            withSelectedPokemon(
                selectedPokemon = uiState.value.content.selectedPokemon,
                onPresent = { pokemon ->
                    pokemonRepository.markAsRelease(pokemon.id)
                        .resultCollect(
                            onSuccess = { _ ->
                                copy(
                                    selectedPokemon = selectedPokemon?.copy(
                                        isCaught = false
                                    )
                                )
                            }
                        )
                }
            )
        }
    }

    private suspend fun withSelectedPokemon(
        selectedPokemon: PokemonModel?,
        onPresent: suspend (selectedPokemon: PokemonModel) -> Unit,
        onMissing: () -> Unit = {
            updateErrorState(
                isError = true,
                errorTitle = "선택된 포켓몬이 없습니다.",
                errorContent = "포켓몬을 선택한 후 다시 시도해주세요."
            )
        }
    ) {
        when {
            selectedPokemon == null -> onMissing()
            else -> onPresent(selectedPokemon)
        }
    }
}