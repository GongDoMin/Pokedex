package co.kr.mvisample.feature.detail

import androidx.lifecycle.SavedStateHandle
import co.kr.mvisample.common.base.UiState
import co.kr.mvisample.feature.detail.model.DetailAction
import co.kr.mvisample.feature.detail.model.DetailEvent
import co.kr.mvisample.feature.detail.model.DetailUiState
import co.kr.mvisample.feature.detail.model.PokemonDetailModel
import co.kr.mvisample.feature.detail.model.TypeModel
import co.kr.mvisample.feature.detail.presentation.DetailViewModel
import co.kr.mvisample.testing.data.FakePokemonRepositoryUnitTest
import co.kr.turbino.testTurbino
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class DetailViewModelTest : StringSpec() {

    private val fakePokemonRepository = FakePokemonRepositoryUnitTest()

    private val detailViewModel = DetailViewModel(
        pokemonRepository = fakePokemonRepository,
        savedStateHandle = SavedStateHandle(
            mapOf(
                DetailViewModel.ID to 6,
                DetailViewModel.NAME to "charizard",
                DetailViewModel.IS_DISCOVERED to true
            )
        )
    )

    init {
        "포켓몬 상세 정보를 불러온다" {
            detailViewModel.uiState.testTurbino {
                awaitLastItem(
                    UiState(
                        content = DetailUiState(
                            pokemonDetail = PokemonDetailModel(
                                id = 6,
                                name = "charizard",
                                imageUrl = "",
                                isDiscovered = true,
                                weight = 90.5f,
                                height = 1.7f,
                                types = listOf(
                                    TypeModel(name = "fire"),
                                    TypeModel(name = "flying")
                                )
                            )
                        )
                    )
                )
            }
        }
        "뒤로 가기를 누른다." {
            detailViewModel.event.testTurbino {
                detailViewModel.handleAction(DetailAction.OnBackClick)

                awaitItem() shouldBe DetailEvent.OnNavigateToBack
            }
        }
    }
}