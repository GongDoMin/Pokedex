package co.kr.mvisample.feature.home.computer.presentation

import co.kr.mvisample.common.base.BaseViewModel
import co.kr.mvisample.data.repository.PokemonRepository
import co.kr.mvisample.feature.home.computer.model.ComputerAction
import co.kr.mvisample.feature.home.computer.model.ComputerEvent
import co.kr.mvisample.feature.home.computer.model.ComputerUiState
import co.kr.mvisample.feature.home.computer.model.PokemonIconModel
import co.kr.mvisample.feature.home.computer.model.toFeature
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ComputerViewModel @Inject constructor(
    private val pokemonRepository: PokemonRepository
) : BaseViewModel<ComputerAction, ComputerUiState, ComputerEvent>(
    initialState = ComputerUiState()
) {
    init {
        handleAction(ComputerAction.FetchPokemonIcons)
    }

    override fun handleAction(action: ComputerAction) {
        when (action) {
            ComputerAction.FetchPokemonIcons -> handleFetchPokemonIcons()
            is ComputerAction.OnPokemonIconClick -> handleOnPokemonIconClick(action.pokemonIcon)
        }
    }

    private fun handleFetchPokemonIcons() {
        launch {
            pokemonRepository.fetchPokemonIcons().collect { pokemonIcons ->
                updateContent {
                    copy(
                        pokemonIcons = pokemonIcons.map { it.toFeature() }
                    )
                }
            }
        }
    }

    private fun handleOnPokemonIconClick(pokemonIcon: PokemonIconModel) {
        val content = uiState.value.content
        val selected = content.selectedPokemonIcon

        when {
            selected == null -> {
                updateContent {
                    copy(selectedPokemonIcon = pokemonIcon)
                }
            }

            selected.id == pokemonIcon.id -> {
                updateContent {
                    copy(selectedPokemonIcon = null)
                }
            }

            content.hasBothIcons(selected.id, pokemonIcon.id) -> {
                launch {
                    pokemonRepository.swapPokemonOrder(
                        firstId = selected.id,
                        secondId = pokemonIcon.id
                    ).resultCollect(
                        onSuccess = {
                            copy(selectedPokemonIcon = null)
                        }
                    )
                }
            }

            else -> {
                updateContent {
                    copy(selectedPokemonIcon = null)
                }
            }
        }
    }

    private fun ComputerUiState.hasBothIcons(firstId: Int, secondId: Int): Boolean {
        return pokemonIcons.any { it.id == firstId } && pokemonIcons.any { it.id == secondId }
    }
}