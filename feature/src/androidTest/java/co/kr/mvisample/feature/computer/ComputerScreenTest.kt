package co.kr.mvisample.feature.computer

import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertContentDescriptionEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import co.kr.mvisample.core.theme.PreviewPokemonTheme
import co.kr.mvisample.feature.home.computer.ComputerScreen
import co.kr.mvisample.feature.utils.sementics.CustomOffsetYKey
import co.kr.mvisample.testing.HiltTestActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class ComputerScreenTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<HiltTestActivity>()

    lateinit var density: Density

    @Before
    fun setUp() {
        hiltRule.inject()

        composeTestRule.setContent {
            PreviewPokemonTheme {
                density = LocalDensity.current
                ComputerScreen()
            }
        }
    }

    @Test
    fun 포켓몬아이콘이_초기화면에_보인다() {
        composeTestRule
            .onNodeWithContentDescription("pokemonIcon_6")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithContentDescription("pokemonIcon_9")
            .assertIsDisplayed()
    }

    @Test
    fun 리자몽을_누른후_offset이_변경되는지_확인한다() = runTest {
        composeTestRule
            .onNodeWithContentDescription("pokemonIcon_6")
            .performClick()

        composeTestRule.awaitIdle()

        composeTestRule
            .onNodeWithContentDescription("pokemonIcon_6")
            .assert(SemanticsMatcher.expectValue(CustomOffsetYKey, with(density) { 8.dp.roundToPx().times(-1) }))
    }

    @Test
    fun 리자몽을_누른후_다시_리자몽을_누른다() = runTest {
        composeTestRule
            .onNodeWithContentDescription("pokemonIcon_6")
            .performClick()

        composeTestRule
            .onNodeWithContentDescription("pokemonIcon_6")
            .performClick()

        composeTestRule.awaitIdle()

        composeTestRule
            .onNodeWithContentDescription("pokemonIcon_6")
            .assert(SemanticsMatcher.expectValue(CustomOffsetYKey, 0))
    }

    @Test
    fun 리자몽을_누른후_거북왕을_누른다() = runTest {
        with (composeTestRule.onAllNodes(hasContentPrefixDescription())) {
            this[0].assertContentDescriptionEquals("pokemonIcon_6")
            this[1].assertContentDescriptionEquals("pokemonIcon_9")
        }

        composeTestRule
            .onNodeWithContentDescription("pokemonIcon_6")
            .performClick()

        composeTestRule
            .onNodeWithContentDescription("pokemonIcon_9")
            .performClick()

        composeTestRule.awaitIdle()

        with (composeTestRule.onAllNodes(hasContentPrefixDescription())) {
            this[0].assertContentDescriptionEquals("pokemonIcon_9")
            this[1].assertContentDescriptionEquals("pokemonIcon_6")
        }
    }

    private fun hasContentPrefixDescription(
        prefixes: String = "pokemonIcon_",
    ): SemanticsMatcher {
        return SemanticsMatcher(
            "ContentDescription starts with $prefixes"
        ) { semanticsNode ->
            val descriptions = semanticsNode.config.getOrNull(SemanticsProperties.ContentDescription)
            descriptions?.any { desc -> prefixes.any { prefix -> desc.startsWith(prefix) } } == true
        }
    }
}