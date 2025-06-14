package co.kr.mvisample.testing.utils

import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.SemanticsMatcher

fun hasPrefixContentDescription(
    prefix: String = "pokemonIcon_",
): SemanticsMatcher {
    return SemanticsMatcher(
        "ContentDescription starts with $prefix"
    ) { semanticsNode ->
        val descriptions = semanticsNode.config.getOrNull(SemanticsProperties.ContentDescription)
        descriptions?.any { desc -> desc.startsWith(prefix) } == true
    }
}