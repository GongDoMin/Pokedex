package co.kr.mvisample.feature.pokedex

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onRoot
import co.kr.mvisample.design.PreviewPokemonTheme
import co.kr.mvisample.feature.R
import co.kr.mvisample.feature.home.pokedex.PokedexScreen
import co.kr.mvisample.testing.HiltTestActivity
import co.kr.mvisample.testing.utils.waitUntilNodeAssertedAndAction
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import com.github.takahirom.roborazzi.captureRoboImage
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
@Config(application = HiltTestApplication::class, qualifiers = RobolectricDeviceQualifiers.Pixel4a)
@HiltAndroidTest
class PokedexScreenScreenshotTests {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val composeTestRule = createAndroidComposeRule<HiltTestActivity>()

    @Test
    fun 도감화면_스크린샷테스트() {
        composeTestRule.setContent {
            PreviewPokemonTheme {
                PokedexScreen(
                    onNavigateToPokemonDetail = { _, _, _ -> }
                )
            }
        }

        composeTestRule.waitUntilNodeAssertedAndAction(
            node = {
                onNodeWithContentDescription(activity.getString(R.string.pokemon_name_list))
                    .onChildren()
            },
            assert = { onFirst().assertIsDisplayed() },
        )

        composeTestRule.onRoot()
            .captureRoboImage()
    }
}