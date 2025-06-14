package co.kr.mvisample.testing.utils

import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.SemanticsNodeInteractionCollection
import androidx.compose.ui.test.junit4.AndroidComposeTestRule

fun AndroidComposeTestRule<*, *>.waitUntilAssert(
    node: AndroidComposeTestRule<*, *>.() -> SemanticsNodeInteraction,
    assert: SemanticsNodeInteraction.() -> Unit,
    action: SemanticsNodeInteraction.() -> Unit = {},
    timeoutMillis: Long = 5_000L
) {
    waitUntil (
        timeoutMillis = timeoutMillis,
        condition = {
            try {
                node().assert()
                true
            } catch (e: AssertionError) {
                false
            }
        }
    )

    node().action()
}

fun AndroidComposeTestRule<*, *>.waitUntilAllNodesAsserted(
    node: AndroidComposeTestRule<*, *>.() -> SemanticsNodeInteractionCollection,
    assert: SemanticsNodeInteractionCollection.() -> Unit,
    timeoutMillis: Long = 5_000L
) {
    waitUntil (
        timeoutMillis = timeoutMillis,
        condition = {
            try {
                node().assert()
                true
            } catch (e: AssertionError) {
                false
            }
        }
    )
}

fun AndroidComposeTestRule<*, *>.waitUntilNodeAssertedAndAction(
    node: AndroidComposeTestRule<*, *>.() -> SemanticsNodeInteractionCollection,
    assert: SemanticsNodeInteractionCollection.() -> SemanticsNodeInteraction,
    action: SemanticsNodeInteraction.() -> Unit = {},
    timeoutMillis: Long = 5_000L
) {
    var semanticsNodeInteraction: SemanticsNodeInteraction? = null

    waitUntil (
        timeoutMillis = timeoutMillis,
        condition = {
            try {
                semanticsNodeInteraction = node().assert()
                true
            } catch (e: AssertionError) {
                false
            }
        }
    )

    semanticsNodeInteraction?.action()
}