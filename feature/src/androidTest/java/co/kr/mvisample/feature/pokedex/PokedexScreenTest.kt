package co.kr.mvisample.feature.pokedex

import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import co.kr.mvisample.design.PreviewPokemonTheme
import co.kr.mvisample.feature.R
import co.kr.mvisample.feature.home.pokedex.PokedexScreen
import co.kr.mvisample.feature.utils.waitUntil
import co.kr.mvisample.testing.HiltTestActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class PokedexScreenTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<HiltTestActivity>()
    val activity get() = composeTestRule.activity

    @Before
    fun setUp() {
        hiltRule.inject()

        composeTestRule.setContent {
            PreviewPokemonTheme {
                PokedexScreen(
                    onNavigateToPokemonDetail = { _, _, _ -> }
                )
            }
        }
    }

    @Test
    fun 포켓몬이름을_클릭한다() = runTest {
        composeTestRule.waitUntil(
            node = composeTestRule
                .onNodeWithContentDescription(activity.getString(R.string.pokemon_name_list))
                .onChildren()
                .onFirst(),
            action = { it.performClick() }
        )

        composeTestRule.waitUntil(
            node = composeTestRule
                .onNodeWithContentDescription(activity.getString(R.string.pokemon_image))
        )
    }

    @Test
    fun 발견하기를_클릭한다() = runTest {
        composeTestRule.waitUntil(
            node = composeTestRule
                .onNodeWithContentDescription(activity.getString(R.string.pokemon_name_list))
                .onChildren()
                .onFirst(),
            action = { it.performClick() }
        )

        composeTestRule.waitUntil(
            node = composeTestRule
                .onNodeWithText("발견하기"),
            action = { it.performClick() }
        )

        composeTestRule.waitUntil(
            node = composeTestRule
                .onNodeWithText("bulbasaur")
        )
    }

    @Test
    fun 포획하기를_클릭한다() = runTest {
        composeTestRule.waitUntil(
            node = composeTestRule
                .onNodeWithContentDescription(activity.getString(R.string.pokemon_name_list))
                .onChildren()
                .onFirst(),
            action = { it.performClick() }
        )

        val node = composeTestRule
            .onNodeWithContentDescription(activity.getString(R.string.pokemon_name_list))
            .onChildren()
            .onFirst()

        if (node.containTextExactly("???")) {
            composeTestRule
                .onNodeWithText("발견하기")
                .performClick()
        }

        composeTestRule.waitUntil(
            node = composeTestRule
                .onNodeWithText("포획하기"),
            action = { it.performClick() }
        )

        composeTestRule.waitUntil {
            val node = composeTestRule
                .onNodeWithContentDescription(activity.getString(R.string.pokemon_name_list))
                .onChildren()
                .onFirst()
                .fetchSemanticsNode()

            hasContentDescription(activity.getString(R.string.pokeball, true.toString())).matches(node)
        }
    }

    @Test
    fun 놓아주기를_클릭한다() = runTest {
        composeTestRule.waitUntil(
            node = composeTestRule
                .onNodeWithContentDescription(activity.getString(R.string.pokemon_name_list))
                .onChildren()
                .onFirst(),
            action = { it.performClick() }
        )

        val node = composeTestRule
            .onNodeWithContentDescription(activity.getString(R.string.pokemon_name_list))
            .onChildren()
            .onFirst()

        if (node.containTextExactly("???")) {
            composeTestRule.waitUntil(
                node = composeTestRule
                    .onNodeWithText("발견하기"),
                action = { it.performClick() }
            )

            composeTestRule.waitUntil(
                node = composeTestRule
                    .onNodeWithText("포획하기"),
                action = { it.performClick() }
            )

            composeTestRule.waitUntil(
                node = composeTestRule
                    .onNodeWithText("놓아주기"),
                action = { it.performClick() }
            )
        } else {
            composeTestRule.waitUntil(
                node = composeTestRule
                    .onNodeWithText("놓아주기"),
                action = { it.performClick() }
            )
        }

        composeTestRule.waitUntil {
            val node = composeTestRule
                .onNodeWithContentDescription(activity.getString(R.string.pokemon_name_list))
                .onChildren()
                .onFirst()
                .fetchSemanticsNode()

            hasContentDescription(activity.getString(R.string.pokeball, false.toString())).matches(node)
        }
    }

    @Test
    fun 선택된_포켓몬이_없을때_발견하기를_클릭한다() = runTest {
        composeTestRule.waitUntil {
            composeTestRule
                .onNodeWithContentDescription(activity.getString(R.string.pokemon_name_list))
                .onChildren()
                .onFirst()
                .isDisplayed()
        }

        composeTestRule
            .onNodeWithText("발견하기")
            .performClick()

        composeTestRule
            .onNodeWithText("선택된 포켓몬이 없습니다.")
            .assertIsDisplayed()
    }

    private fun SemanticsNodeInteraction.containTextExactly(expected: String): Boolean {
        return try {
            this.assertTextContains(expected)
            true
        } catch (e: AssertionError) {
            false
        }
    }
}