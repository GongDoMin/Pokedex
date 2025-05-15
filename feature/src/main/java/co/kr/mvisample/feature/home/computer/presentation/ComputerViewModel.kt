package co.kr.mvisample.feature.home.computer.presentation

import co.kr.mvisample.data.repository.PokemonRepository
import co.kr.mvisample.feature.base.BaseViewModel
import co.kr.mvisample.feature.base.UiState
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
) : BaseViewModel<ComputerAction, UiState<ComputerUiState>, ComputerEvent>(
    initialState = UiState(content = ComputerUiState())
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
                updateContent(
                    uiState.value.content.copy(
                        pokemonIcons = pokemonIcons.map { it.toFeature() }
                    )
                )
            }
        }
    }

    private fun handleOnPokemonIconClick(pokemonIcon: PokemonIconModel) {
        with (uiState.value.content) {
            when {
                selectedPokemonIcon == null -> {
                    updateContent(
                        copy(
                            selectedPokemonIcon = pokemonIcon
                        )
                    )
                }
                selectedPokemonIcon.id == pokemonIcon.id -> {
                    updateContent(
                        copy(
                            selectedPokemonIcon = null
                        )
                    )
                }
                else -> {
                    launch {
                        pokemonRepository.swapPokemonOrder(
                            firstId = selectedPokemonIcon.id,
                            secondId = pokemonIcon.id
                        ).resultCollect<Unit, ComputerUiState>(
                            onSuccess = {
                                copy(
                                    selectedPokemonIcon = null
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}