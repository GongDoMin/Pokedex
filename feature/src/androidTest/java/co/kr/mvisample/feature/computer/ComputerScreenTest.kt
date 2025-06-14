package co.kr.mvisample.feature.computer

import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import co.kr.mvisample.design.PreviewPokemonTheme
import co.kr.mvisample.feature.R
import co.kr.mvisample.feature.home.computer.ComputerScreen
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
    private val activity get() = composeTestRule.activity

    lateinit var density: Density

    @Before
    fun 테스트준비() {
        hiltRule.inject()

        composeTestRule.setContent {
            PreviewPokemonTheme {
                density = LocalDensity.current
                ComputerScreen()
            }
        }
    }

    @Test
    fun 포켓몬아이콘이_초기화면에_보인다() = runTest {
        composeTestRule.waitUntilAssert(
            node = { onNodeWithContentDescription(getPokemonIcon(6, dpToInt(0.dp))) },
            assert = { assertIsDisplayed() }
        )
        composeTestRule.waitUntilAssert(
            node = { onNodeWithContentDescription(getPokemonIcon(9, dpToInt(0.dp))) },
            assert = { assertIsDisplayed() }
        )
    }

    @Test
    fun 리자몽을_누른후_offset이_변경되는지_확인한다() = runTest {
        composeTestRule.waitUntilAssert(
            node = { onNodeWithContentDescription(getPokemonIcon(6, dpToInt(0.dp))) },
            assert = { assertIsDisplayed() },
            action = { performClick() }
        )

        composeTestRule.waitUntilAssert(
            node = { onNodeWithContentDescription(getPokemonIcon(6, dpToInt((-8).dp))) },
            assert = { assertIsDisplayed() }
        )
    }

    @Test
    fun 리자몽을_누른후_다시_리자몽을_누른다() = runTest {
        composeTestRule.waitUntilAssert(
            node = { onNodeWithContentDescription(getPokemonIcon(6, dpToInt(0.dp))) },
            assert = { assertIsDisplayed() },
            action = { performClick() }
        )

        composeTestRule.onNode(hasPrefixContentDescription("pokemonIcon_6_"))
            .performClick()

        composeTestRule.waitUntilAssert(
            node = { onNodeWithContentDescription(getPokemonIcon(6, dpToInt(0.dp))) },
            assert = { assertIsDisplayed() }
        )
    }

    @Test
    fun 리자몽을_누른후_거북왕을_누른다() = runTest {
        composeTestRule.waitUntilAssert(
            node = { onNodeWithContentDescription(getPokemonIcon(6, dpToInt(0.dp))) },
            assert = { assertIsDisplayed() },
            action = { performClick() }
        )

        composeTestRule.waitUntilAssert(
            node = { onNodeWithContentDescription(getPokemonIcon(9, dpToInt(0.dp))) },
            assert = { assertIsDisplayed() },
            action = { performClick() }
        )

        composeTestRule.waitUntil {
            val pokemonDescriptions = getPokemonDescriptions()
            pokemonDescriptions.size == 2 &&
                pokemonDescriptions[0] == getPokemonIcon(9, dpToInt(0.dp)) &&
                pokemonDescriptions[1] == getPokemonIcon(6, dpToInt(0.dp))
        }
    }

    private fun dpToInt(dp: Dp) = with (density) { dp.roundToPx() }

    private fun AndroidComposeTestRule<*, *>.waitUntilAssert(
        node: AndroidComposeTestRule<*, *>.() -> SemanticsNodeInteraction,
        assert: SemanticsNodeInteraction.() -> Unit,
        action: SemanticsNodeInteraction.() -> Unit = {},
        timeoutMillis: Long = 2_000L
    ) {
        val targetNode = this.node()

        waitUntil (
            timeoutMillis = timeoutMillis,
            condition = {
                try {
                    targetNode.assert()
                    true
                } catch (e: AssertionError) {
                    false
                }
            }
        )

        targetNode.action()
    }

    private fun getPokemonDescriptions(): List<String> =
        composeTestRule.onAllNodes(hasPrefixContentDescription())
            .fetchSemanticsNodes()
            .mapNotNull { it.config.getOrNull(SemanticsProperties.ContentDescription)?.firstOrNull() }

    private fun hasPrefixContentDescription(
        prefix: String = "pokemonIcon_",
    ): SemanticsMatcher {
        return SemanticsMatcher(
            "ContentDescription starts with $prefix"
        ) { semanticsNode ->
            val descriptions = semanticsNode.config.getOrNull(SemanticsProperties.ContentDescription)
            descriptions?.any { desc -> desc.startsWith(prefix) } == true
        }
    }

    private fun getPokemonIcon(id: Int, offsetY: Int) = activity.getString(R.string.pokemon_icon, id, offsetY)
}