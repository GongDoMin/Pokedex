package co.kr.mvisample.feature.detail

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.lifecycle.SavedStateHandle
import co.kr.mvisample.data.repository.PokemonRepository
import co.kr.mvisample.design.PreviewPokemonTheme
import co.kr.mvisample.feature.detail.presentation.DetailViewModel
import co.kr.mvisample.testing.HiltTestActivity
import co.kr.mvisample.testing.utils.captureMultiDevice
import co.kr.mvisample.testing.utils.waitUntilAssert
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import javax.inject.Inject

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(application = HiltTestApplication::class)
@HiltAndroidTest
class DetailScreenScreenshotTests {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val composeTestRule = createAndroidComposeRule<HiltTestActivity>()

    @Inject lateinit var pokemonRepository: PokemonRepository

    @Before
    fun 테스트준비() {
        hiltRule.inject()
    }

    @Test
    fun 디테일스크린_스크린샷테스트() {
        composeTestRule.captureMultiDevice(
            screenshotName = "디테일스크린_스크린샷테스트",
            content = {
                PreviewPokemonTheme {
                    DetailScreen(
                        onNavigateToBack = {},
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
                    )
                }
            },
            assertion = {
                waitUntilAssert(
                    node = { onNodeWithText("1.7 M") },
                    assert = { assertIsDisplayed() }
                )
            }
        )
    }
}