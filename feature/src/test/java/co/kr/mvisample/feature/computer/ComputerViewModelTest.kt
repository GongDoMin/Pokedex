package co.kr.mvisample.feature.computer

import app.cash.turbine.test
import co.kr.mvisample.feature.home.computer.model.ComputerAction
import co.kr.mvisample.feature.home.computer.presentation.ComputerViewModel
import co.kr.mvisample.testing.data.FakePokemonRepositoryUnitTest
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class ComputerViewModelTest : StringSpec() {

    private val fakePokemonRepository = FakePokemonRepositoryUnitTest()

    private val computerViewModel = ComputerViewModel(fakePokemonRepository)

    init {
        "포켓몬 아이콘 정보를 불러온다." {
            computerViewModel.uiState.test {
                val uiState = awaitItem()

                uiState.content.pokemonIcons.size shouldBe 2
            }
        }
        "선택된 포켓몬이 없을 때 포켓몬을 클릭한다." {
            computerViewModel.uiState.test {
                awaitItem()

                computerViewModel.handleAction(ComputerAction.OnPokemonIconClick(pokemonIcon = computerViewModel.uiState.value.content.pokemonIcons.first()))
                val uiState = awaitItem()

                uiState.content.selectedPokemonIcon?.id shouldBe 6
            }
        }
        "선택된 포켓몬을 다시 클릭한다." {
            computerViewModel.uiState.test {
                awaitItem()

                computerViewModel.handleAction(ComputerAction.OnPokemonIconClick(pokemonIcon = computerViewModel.uiState.value.content.pokemonIcons.first()))
                val uiState = awaitItem()

                uiState.content.selectedPokemonIcon shouldBe null
            }
        }
        "포켓몬의 순서를 변경한다." {
            computerViewModel.uiState.test {
                awaitItem()

                computerViewModel.handleAction(ComputerAction.OnPokemonIconClick(pokemonIcon = computerViewModel.uiState.value.content.pokemonIcons.first()))
                awaitItem()
                computerViewModel.handleAction(ComputerAction.OnPokemonIconClick(pokemonIcon = computerViewModel.uiState.value.content.pokemonIcons.last()))
                awaitItem() // loading is true
                awaitItem() // loading is false
                awaitItem() // update pokemonIcons
                val uiState = awaitItem()

                uiState.content.selectedPokemonIcon shouldBe null
                uiState.content.pokemonIcons.first { it.id == 6 }.order shouldBe 2
                uiState.content.pokemonIcons.first { it.id == 9 }.order shouldBe 1
            }
        }
    }
}