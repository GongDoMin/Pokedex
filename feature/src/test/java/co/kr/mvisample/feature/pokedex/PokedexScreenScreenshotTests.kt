package co.kr.mvisample.feature.pokedex

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithContentDescription
import co.kr.mvisample.design.PreviewPokemonTheme
import co.kr.mvisample.feature.R
import co.kr.mvisample.feature.home.pokedex.PokedexScreen
import co.kr.mvisample.testing.HiltTestActivity
import co.kr.mvisample.testing.captureMultiDevice
import co.kr.mvisample.testing.utils.waitUntilAssert
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(application = HiltTestApplication::class)
@HiltAndroidTest
class PokedexScreenScreenshotTests {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val composeTestRule = createAndroidComposeRule<HiltTestActivity>()

    @Test
    fun 도감화면_스크린샷테스트() {
        composeTestRule.captureMultiDevice(
            screenshotName = "도감화면_스크린샷테스트",
            content = {
                PreviewPokemonTheme {
                    PokedexScreen(
                        onNavigateToPokemonDetail = { _, _, _ -> }
                    )
                }
            },
            assertion = {
                waitUntilAssert(
                    node = {
                        onNodeWithContentDescription(activity.getString(R.string.pokemon_name_list))
                            .onChildren()
                            .onFirst()
                    },
                    assert = { assertIsDisplayed() },
                )
            }
        )
    }
}