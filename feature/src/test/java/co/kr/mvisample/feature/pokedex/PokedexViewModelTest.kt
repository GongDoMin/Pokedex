package co.kr.mvisample.feature.pokedex

import app.cash.turbine.test
import co.kr.mvisample.testing.data.FakePokemonRepository
import co.kr.mvisample.feature.home.pokedex.model.PokedexAction
import co.kr.mvisample.feature.home.pokedex.model.PokedexEvent
import co.kr.mvisample.feature.home.pokedex.model.PokemonModel
import co.kr.mvisample.feature.home.pokedex.presentation.PokedexViewModel
import co.kr.mvisample.testing.data.FakePokemonRepositoryUnitTest
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class PokedexViewModelTest : StringSpec() {

    private val fakePokemonRepository = FakePokemonRepositoryUnitTest()

    private val pokedexViewModel = PokedexViewModel(fakePokemonRepository)

    init {
        "포켓몬 클릭한다." {
            pokedexViewModel.uiState.test {
                awaitItem() // initial

                pokedexViewModel.handleAction(
                    PokedexAction.OnPokemonClick(
                        pokemon = PokemonModel(
                            id = 6,
                            name = "charizard"
                        )
                    )
                )

                val uiState = awaitItem()

                uiState.content.selectedPokemon?.id shouldBe 6
                uiState.content.selectedPokemon?.name shouldBe "charizard"
            }
        }
        "포켓몬 상세보기를 클릭한다." {
            pokedexViewModel.event.test {
                pokedexViewModel.handleAction(PokedexAction.ShowPokemonDetail)

                val event = awaitItem()

                event shouldBe PokedexEvent.OnNavigateToDetail(id = 6, name = "charizard", isDiscovered = false)
            }
        }
        "포켓몬 발견하기를 클릭한다." {
            pokedexViewModel.uiState.test {
                awaitItem() // initial

                pokedexViewModel.handleAction(PokedexAction.MarkPokemonAsDiscovered)

                awaitItem() // loading = true
                awaitItem() // loading = false
                val uiState = awaitItem()

                uiState.content.selectedPokemon shouldBe PokemonModel(id = 6, name = "charizard", isDiscovered = true, isCaught = false)
            }
        }
        "포켓몬 포획하기를 클릭한다." {
            pokedexViewModel.uiState.test {
                awaitItem()

                pokedexViewModel.handleAction(PokedexAction.AttemptCatchPokemon)

                awaitItem() // loading = true
                awaitItem() // loading = false
                val uiState = awaitItem()

                uiState.content.selectedPokemon shouldBe PokemonModel(id = 6, name = "charizard", isDiscovered = true, isCaught = true)
            }
        }
        "포켓몬 놓아주기를 클릭한다." {
            pokedexViewModel.uiState.test {
                awaitItem()

                pokedexViewModel.handleAction(PokedexAction.ReleasePokemon)

                awaitItem() // loading = true
                awaitItem() // loading = false
                val uiState = awaitItem()

                uiState.content.selectedPokemon shouldBe PokemonModel(id = 6, name = "charizard", isDiscovered = true, isCaught = false)
            }
        }
    }
}