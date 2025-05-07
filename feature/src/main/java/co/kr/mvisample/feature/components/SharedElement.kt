package co.kr.mvisample.feature.components

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import co.kr.mvisample.core.theme.LocalNavAnimatedVisibilityScope
import co.kr.mvisample.core.theme.LocalSharedTransitionScope

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun Modifier.sharedElement(
    key: String,
    sharedTransitionScope: SharedTransitionScope = LocalSharedTransitionScope.current ?: throw IllegalArgumentException("No Scope found"),
    animatedVisibilityScope: AnimatedVisibilityScope = LocalNavAnimatedVisibilityScope.current ?: throw IllegalArgumentException("No Scope found")
) : Modifier {
    with (sharedTransitionScope) {
        return this@sharedElement.sharedElement(
            state = rememberSharedContentState(key),
            animatedVisibilityScope = animatedVisibilityScope
        )
    }

}