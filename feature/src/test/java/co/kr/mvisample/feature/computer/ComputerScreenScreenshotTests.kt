package co.kr.mvisample.feature.computer

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import co.kr.mvisample.design.PokemonTheme
import co.kr.mvisample.feature.R
import co.kr.mvisample.feature.home.computer.ComputerScreen
import co.kr.mvisample.testing.HiltTestActivity
import co.kr.mvisample.testing.utils.captureMultiDevice
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
class ComputerScreenScreenshotTests {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val composeTestRule = createAndroidComposeRule<HiltTestActivity>()

    @Test
    fun 컴퓨터스크린_스크린샷테스트() {
        composeTestRule.captureMultiDevice(
            screenshotName = "컴퓨터스크린_스크린샷테스트",
            content = {
                PokemonTheme {
                    ComputerScreen()
                }
            },
            assertion = {
                waitUntilAssert(
                    node = { onNodeWithContentDescription(composeTestRule.activity.getString(R.string.pokemon_icon, "charizard")) },
                    assert = { assertIsDisplayed() },
                )
            }
        )
    }
}