package co.kr.mvisample.feature.detail

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.lifecycle.SavedStateHandle
import co.kr.mvisample.data.repository.PokemonRepository
import co.kr.mvisample.design.PreviewPokemonTheme
import co.kr.mvisample.feature.R
import co.kr.mvisample.feature.detail.presentation.DetailViewModel
import co.kr.mvisample.testing.HiltTestActivity
import co.kr.mvisample.testing.utils.waitUntilAllNodesAsserted
import co.kr.mvisample.testing.utils.waitUntilAssert
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class DetailScreenTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<HiltTestActivity>()

    private lateinit var viewModel: DetailViewModel

    @Inject lateinit var pokemonRepository: PokemonRepository

    private var isDetailScreen = true

    @Before
    fun 테스트준비() {
        hiltRule.inject()

        viewModel = DetailViewModel(
            pokemonRepository = pokemonRepository,
            savedStateHandle = SavedStateHandle(
                mapOf(
                    DetailViewModel.ID to 6,
                    DetailViewModel.NAME to "charizard",
                    DetailViewModel.IS_DISCOVERED to true,
                )
            )
        )

        composeTestRule.setContent {
            PreviewPokemonTheme {
                DetailScreen(
                    onNavigateToBack = { isDetailScreen = false },
                    viewModel = viewModel
                )
            }
        }
    }

    @Test
    fun 포켓몬상세정보를_불러온다() {
        composeTestRule.waitUntilAssert(
            node = { onNodeWithContentDescription(activity.getString(R.string.pokemon_detail_image)) },
            assert = { assertIsDisplayed() }
        )

        composeTestRule.waitUntilAllNodesAsserted(
            node = { onAllNodesWithText("charizard") },
            assert = { assertCountEquals(2) }
        )

        composeTestRule.waitUntilAssert(
            node = { onNodeWithText("006") },
            assert = { assertIsDisplayed() }
        )

        composeTestRule.waitUntilAssert(
            node = { onNodeWithText("90.5 KG") },
            assert = { assertIsDisplayed() }
        )

        composeTestRule.waitUntilAssert(
            node = { onNodeWithText("1.7 M") },
            assert = { assertIsDisplayed() }
        )

        composeTestRule.waitUntilAssert(
            node = { onNodeWithText("fire\nflying") },
            assert = { performScrollTo().assertIsDisplayed() }
        )
    }

    @Test
    fun 뒤로가기를_누른다() {
        composeTestRule.waitUntilAssert(
            node = { onNodeWithContentDescription("backIcon") },
            assert = { assertIsDisplayed() },
            action = { performClick() }
        )

        composeTestRule.waitUntil(
            timeoutMillis = 2000L
        ) {
            isDetailScreen.not()
        }
    }
}