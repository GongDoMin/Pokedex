package co.kr.mvisample.feature.pokedex

import androidx.compose.ui.test.assertAll
import androidx.compose.ui.test.assertAny
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.filter
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import co.kr.mvisample.design.PreviewPokemonTheme
import co.kr.mvisample.feature.R
import co.kr.mvisample.feature.home.pokedex.PokedexScreen
import co.kr.mvisample.testing.HiltTestActivity
import co.kr.mvisample.testing.utils.hasPrefixContentDescription
import co.kr.mvisample.testing.utils.hasSuffixContentDescription
import co.kr.mvisample.testing.utils.waitUntilAllNodesAsserted
import co.kr.mvisample.testing.utils.waitUntilAssert
import co.kr.mvisample.testing.utils.waitUntilNodeAssertedAndAction
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
    fun 테스트준비() {
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
        composeTestRule.waitUntilNodeAssertedAndAction(
            node = {
                onNodeWithContentDescription(activity.getString(R.string.pokemon_name_list))
                    .onChildren()
           },
            assert = { onFirst().performScrollTo().assertIsDisplayed() },
            action = { performClick() }
        )

        composeTestRule.waitUntilAssert(
            node = { onNodeWithContentDescription(activity.getString(R.string.pokemon_image)) },
            assert = { assertIsDisplayed() }
        )
    }

    @Test
    fun 발견하기를_클릭한다() = runTest {
        composeTestRule.waitUntilNodeAssertedAndAction(
            node = {
                onNodeWithContentDescription(activity.getString(R.string.pokemon_name_list))
                    .onChildren()
            },
            assert = { filter(hasPrefixContentDescription("isDiscover is false")).onFirst().performScrollTo().assertIsDisplayed() },
            action = { performClick() }
        )

        composeTestRule.waitUntilAssert(
            node = { onNodeWithText("발견하기") },
            assert = { assertIsDisplayed() },
            action = { performClick() }
        )

        composeTestRule.waitUntilAllNodesAsserted(
            node = {
                onNodeWithContentDescription(activity.getString(R.string.pokemon_name_list))
                    .onChildren()
            },
            assert = { assertAny(hasPrefixContentDescription("isDiscover is true")) },
        )
    }

    @Test
    fun 포획하기를_클릭한다() = runTest {
        composeTestRule.waitUntilNodeAssertedAndAction(
            node = {
                onNodeWithContentDescription(activity.getString(R.string.pokemon_name_list))
                    .onChildren()
            },
            assert = { filter(hasPrefixContentDescription("isDiscover is false")).onFirst().performScrollTo().assertIsDisplayed() },
            action = { performClick() }
        )

        composeTestRule.waitUntilAssert(
            node = { onNodeWithText("발견하기") },
            assert = { assertIsDisplayed() },
            action = { performClick() }
        )

        composeTestRule.waitUntilAssert(
            node = { onNodeWithText("포획하기") },
            assert = { assertIsDisplayed() },
            action = { performClick() }
        )

        composeTestRule.waitUntilAllNodesAsserted(
            node = {
                onNodeWithContentDescription(activity.getString(R.string.pokemon_name_list))
                    .onChildren()
            },
            assert = { assertAny(hasContentDescription("isDiscover is true and isCaught is true")) },
        )
    }

    @Test
    fun 놓아주기를_클릭한다() = runTest {
        composeTestRule.waitUntilNodeAssertedAndAction(
            node = {
                onNodeWithContentDescription(activity.getString(R.string.pokemon_name_list))
                    .onChildren()
            },
            assert = { filter(hasPrefixContentDescription("isDiscover is false")).onFirst().performScrollTo().assertIsDisplayed() },
            action = { performClick() }
        )

        composeTestRule.waitUntilAssert(
            node = { onNodeWithText("발견하기") },
            assert = { assertIsDisplayed() },
            action = { performClick() }
        )

        composeTestRule.waitUntilAssert(
            node = { onNodeWithText("포획하기") },
            assert = { assertIsDisplayed() },
            action = { performClick() }
        )

        composeTestRule.waitUntilAssert(
            node = { onNodeWithText("놓아주기") },
            assert = { assertIsDisplayed() },
            action = { performClick() }
        )

        composeTestRule.waitUntilAllNodesAsserted(
            node = {
                onNodeWithContentDescription(activity.getString(R.string.pokemon_name_list))
                    .onChildren()
            },
            assert = { assertAll(hasSuffixContentDescription("isCaught is false")) },
        )
    }

    @Test
    fun 선택된_포켓몬이_없을때_발견하기를_클릭한다() = runTest {
        composeTestRule.waitUntilNodeAssertedAndAction(
            node = {
                onNodeWithContentDescription(activity.getString(R.string.pokemon_name_list))
                    .onChildren()
            },
            assert = { onFirst().performScrollTo().assertIsDisplayed() }
        )

        composeTestRule.waitUntilAssert(
            node = { onNodeWithText("발견하기") },
            assert = { assertIsDisplayed() },
            action = { performClick() }
        )

        composeTestRule.waitUntilAssert(
            node = { onNodeWithText("선택된 포켓몬이 없습니다.") },
            assert = { assertIsDisplayed() },
            action = { performClick() }
        )
    }
}