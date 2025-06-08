package co.kr.mvisample.feature.utils

import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.test.ext.junit.rules.ActivityScenarioRule
import co.kr.mvisample.testing.HiltTestActivity

fun AndroidComposeTestRule<ActivityScenarioRule<HiltTestActivity>, HiltTestActivity>.waitUntil(
    node: SemanticsNodeInteraction,
    action: (SemanticsNodeInteraction) -> Unit = {},
    timeout: Long = 3_000L
) {
    this.waitUntil(
        timeoutMillis = timeout,
        condition = {
            node.isDisplayed()
        }
    )
    action(node)
}

fun AndroidComposeTestRule<ActivityScenarioRule<HiltTestActivity>, HiltTestActivity>.waitUntil(condition: () -> Boolean) {
    this.waitUntil(
        timeoutMillis = 5000L,
        condition = condition
    )
}