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
import co.kr.mvisample.common.utils.CustomOffsetYKey
import co.kr.mvisample.design.PreviewPokemonTheme
import co.kr.mvisample.feature.R
import co.kr.mvisample.feature.home.computer.ComputerScreen
import co.kr.mvisample.feature.home.computer.yOffset
import co.kr.mvisample.feature.utils.waitUntil
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
        composeTestRule.waitUntil(
            node = composeTestRule
                .onNodeWithContentDescription(getPokemonIcon(6))
        )

        composeTestRule.waitUntil(
            node = composeTestRule
                .onNodeWithContentDescription(getPokemonIcon(9))
        )
    }

    @Test
    fun 리자몽을_누른후_offset이_변경되는지_확인한다() = runTest {
        composeTestRule.waitUntil(
            node = composeTestRule
                .onNodeWithContentDescription(getPokemonIcon(6)),
            action = { it.performClick() }
        )

        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithContentDescription(getPokemonIcon(6))
            .assert(SemanticsMatcher.expectValue(CustomOffsetYKey, with(density) { yOffset.roundToPx().times(-1) }))
    }

    @Test
    fun 리자몽을_누른후_다시_리자몽을_누른다() = runTest {
        composeTestRule.waitUntil(
            node = composeTestRule
                .onNodeWithContentDescription(getPokemonIcon(6)),
            action = { it.performClick() }
        )

        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithContentDescription(getPokemonIcon(6))
            .performClick()

        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithContentDescription(getPokemonIcon(6))
            .assert(SemanticsMatcher.expectValue(CustomOffsetYKey, 0))
    }

    @Test
    fun 리자몽을_누른후_거북왕을_누른다() = runTest {
        val before = getCurrentPokemonOrder()

        composeTestRule
            .onNodeWithContentDescription(getPokemonIcon(6))
            .performClick()

        composeTestRule
            .onNodeWithContentDescription(getPokemonIcon(9))
            .performClick()

        composeTestRule.waitForIdle()

        val after = getCurrentPokemonOrder()
        assert(before != after)
        assert(before.sorted() == after.sorted())
    }

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

    private fun getCurrentPokemonOrder(): List<String> {
        composeTestRule.waitUntil(timeoutMillis = 5_000) {
            composeTestRule
                .onAllNodes(hasPrefixContentDescription())
                .fetchSemanticsNodes().isNotEmpty()
        }

        return composeTestRule
            .onAllNodes(hasPrefixContentDescription())
            .fetchSemanticsNodes()
            .mapNotNull {
                it.config.getOrNull(SemanticsProperties.ContentDescription)?.firstOrNull()
            }
    }

    private fun getPokemonIcon(id: Int) = activity.getString(R.string.pokemon_icon, id)
}