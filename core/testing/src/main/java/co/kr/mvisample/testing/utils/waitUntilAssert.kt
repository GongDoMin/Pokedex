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

fun AndroidComposeTestRule<*, *>.waitUntilAllNodesAsserted(
    node: AndroidComposeTestRule<*, *>.() -> SemanticsNodeInteractionCollection,
    assert: SemanticsNodeInteractionCollection.() -> Unit,
    timeoutMillis: Long = 5_000L
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
}

fun AndroidComposeTestRule<*, *>.waitUntilNodeAssertedAndAction(
    node: AndroidComposeTestRule<*, *>.() -> SemanticsNodeInteractionCollection,
    assert: SemanticsNodeInteractionCollection.() -> SemanticsNodeInteraction,
    action: SemanticsNodeInteraction.() -> Unit = {},
    timeoutMillis: Long = 5_000L
) {
    val targetNode = this.node()
    var semanticsNodeInteraction: SemanticsNodeInteraction? = null

    waitUntil (
        timeoutMillis = timeoutMillis,
        condition = {
            try {
                semanticsNodeInteraction = targetNode.assert()
                true
            } catch (e: AssertionError) {
                false
            }
        }
    )

    semanticsNodeInteraction?.action()
}