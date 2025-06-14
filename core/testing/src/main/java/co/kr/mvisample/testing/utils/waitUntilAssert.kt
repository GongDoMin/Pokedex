package co.kr.mvisample.testing.utils

import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.junit4.AndroidComposeTestRule

fun AndroidComposeTestRule<*, *>.waitUntilAssert(
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