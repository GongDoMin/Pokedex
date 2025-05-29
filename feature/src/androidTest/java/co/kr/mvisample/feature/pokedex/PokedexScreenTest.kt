package co.kr.mvisample.feature.pokedex

import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assertAny
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
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
        waitUntil {
            composeTestRule
                .onNodeWithContentDescription(activity.getString(R.string.pokemon_name_list))
                .onChildren()
                .onFirst()
                .isDisplayed()
        }

        composeTestRule
            .onNodeWithContentDescription(activity.getString(R.string.pokemon_name_list))
            .onChildren()
            .onFirst()
            .performClick()

        composeTestRule
            .onNodeWithContentDescription(activity.getString(R.string.pokemon_image))
            .assertIsDisplayed()
    }

    @Test
    fun 발견하기를_클릭한다() = runTest {
        waitUntil {
            composeTestRule
                .onNodeWithContentDescription(activity.getString(R.string.pokemon_name_list))
                .onChildren()
                .onFirst()
                .isDisplayed()
        }

        composeTestRule
            .onNodeWithContentDescription(activity.getString(R.string.pokemon_name_list))
            .onChildren()
            .onFirst()
            .performClick()

        composeTestRule
            .onNodeWithText("발견하기")
            .performClick()

        composeTestRule
            .onNodeWithContentDescription(activity.getString(R.string.pokemon_name_list))
            .onChildren()
            .assertAny(hasText("bulbasaur"))
    }

    @Test
    fun 포획하기를_클릭한다() = runTest {
        waitUntil {
            composeTestRule
                .onNodeWithContentDescription(activity.getString(R.string.pokemon_name_list))
                .onChildren()
                .onFirst()
                .isDisplayed()
        }

        composeTestRule
            .onNodeWithContentDescription(activity.getString(R.string.pokemon_name_list))
            .onChildren()
            .onFirst()
            .performClick()

        val node = composeTestRule
            .onNodeWithContentDescription(activity.getString(R.string.pokemon_name_list))
            .onChildren()
            .onFirst()

        if (node.containTextExactly("???")) {
            composeTestRule
                .onNodeWithText("발견하기")
                .performClick()
        }

        waitUntil {
            composeTestRule
                .onNodeWithText("포획하기")
                .isDisplayed()
        }

        composeTestRule
            .onNodeWithText("포획하기")
            .performClick()

        waitUntil {
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
        waitUntil {
            composeTestRule
                .onNodeWithContentDescription(activity.getString(R.string.pokemon_name_list))
                .onChildren()
                .onFirst()
                .isDisplayed()
        }

        composeTestRule
            .onNodeWithContentDescription(activity.getString(R.string.pokemon_name_list))
            .onChildren()
            .onFirst()
            .performClick()

        val node = composeTestRule
            .onNodeWithContentDescription(activity.getString(R.string.pokemon_name_list))
            .onChildren()
            .onFirst()

        if (node.containTextExactly("???")) {
            composeTestRule
                .onNodeWithText("발견하기")
                .performClick()

            waitUntil {
                composeTestRule
                    .onNodeWithText("포획하기")
                    .isDisplayed()
            }

            composeTestRule
                .onNodeWithText("포획하기")
                .performClick()

            waitUntil {
                composeTestRule
                    .onNodeWithText("놓아주기")
                    .isDisplayed()
            }

            composeTestRule
                .onNodeWithText("놓아주기")
                .performClick()
        } else {
            waitUntil {
                composeTestRule
                    .onNodeWithText("놓아주기")
                    .isDisplayed()
            }

            composeTestRule
                .onNodeWithText("놓아주기")
                .performClick()
        }

        waitUntil {
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
        waitUntil {
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

    private fun waitUntil(condition: () -> Boolean) {
        composeTestRule.waitUntil(
            timeoutMillis = 5000L,
            condition = condition
        )
    }
}