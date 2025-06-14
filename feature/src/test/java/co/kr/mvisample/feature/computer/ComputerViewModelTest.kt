package co.kr.mvisample.feature.computer

import co.kr.mvisample.common.base.UiState
import co.kr.mvisample.feature.home.computer.model.ComputerAction
import co.kr.mvisample.feature.home.computer.model.ComputerUiState
import co.kr.mvisample.feature.home.computer.model.PokemonIconModel
import co.kr.mvisample.feature.home.computer.presentation.ComputerViewModel
import co.kr.mvisample.testing.data.FakePokemonRepositoryUnitTest
import co.kr.turbino.testTurbino
import io.kotest.core.spec.style.StringSpec

class ComputerViewModelTest : StringSpec() {

    private val fakePokemonRepository = FakePokemonRepositoryUnitTest()

    private val computerViewModel = ComputerViewModel(fakePokemonRepository)

    init {
        "포켓몬 아이콘 정보를 불러온다." {
            computerViewModel.uiState.testTurbino {
                awaitLastItem(
                    UiState(
                        content = ComputerUiState(
                            pokemonIcons = listOf(
                                PokemonIconModel(id = 6, iconUrl = "", order = 1),
                                PokemonIconModel(id = 9, iconUrl = "", order = 2)
                            ),
                            selectedPokemonIcon = null
                        )
                    )
                )
            }
        }
        "선택된 포켓몬이 없을 때 포켓몬을 클릭한다." {
            computerViewModel.uiState.testTurbino {
                computerViewModel.handleAction(ComputerAction.OnPokemonIconClick(pokemonIcon = computerViewModel.uiState.value.content.pokemonIcons.first()))

                awaitLastItem(
                    UiState(
                        content = ComputerUiState(
                            pokemonIcons = listOf(
                                PokemonIconModel(id = 6, iconUrl = "", order = 1),
                                PokemonIconModel(id = 9, iconUrl = "", order = 2)
                            ),
                            selectedPokemonIcon = PokemonIconModel(id = 6, iconUrl = "", order = 1)
                        )
                    )
                )
            }
        }
        "선택된 포켓몬을 다시 클릭한다." {
            computerViewModel.uiState.testTurbino {
                computerViewModel.handleAction(ComputerAction.OnPokemonIconClick(pokemonIcon = computerViewModel.uiState.value.content.pokemonIcons.first()))

                awaitLastItem(
                    UiState(
                        content = ComputerUiState(
                            pokemonIcons = listOf(
                                PokemonIconModel(id = 6, iconUrl = "", order = 1),
                                PokemonIconModel(id = 9, iconUrl = "", order = 2)
                            ),
                            selectedPokemonIcon = null
                        )
                    )
                )
            }
        }
        "포켓몬의 순서를 변경한다." {
            computerViewModel.uiState.testTurbino {
                computerViewModel.handleAction(ComputerAction.OnPokemonIconClick(pokemonIcon = computerViewModel.uiState.value.content.pokemonIcons.first()))
                computerViewModel.handleAction(ComputerAction.OnPokemonIconClick(pokemonIcon = computerViewModel.uiState.value.content.pokemonIcons.last()))

                awaitLastItem(
                    UiState(
                        content = ComputerUiState(
                            pokemonIcons = listOf(
                                PokemonIconModel(id = 6, iconUrl = "", order = 2),
                                PokemonIconModel(id = 9, iconUrl = "", order = 1)
                            ),
                            selectedPokemonIcon = null
                        )
                    )
                )
            }
        }
    }
}