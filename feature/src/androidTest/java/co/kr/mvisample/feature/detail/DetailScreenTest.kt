package co.kr.mvisample.feature.detail

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.lifecycle.SavedStateHandle
import co.kr.mvisample.core.theme.PreviewPokemonTheme
import co.kr.mvisample.data.repository.PokemonRepository
import co.kr.mvisample.feature.detail.presentation.DetailViewModel
import co.kr.mvisample.testing.HiltTestActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Assert.assertFalse
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
    fun setUp() {
        hiltRule.inject()

        initViewModel()

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
    fun 뒤로가기를_누른다() {
        composeTestRule
            .onNodeWithContentDescription("backIcon")
            .performClick()

        assertFalse(isDetailScreen)
    }

    private fun initViewModel() {
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
    }
}