@file:OptIn(
    ExperimentalSharedTransitionApi::class
)

package co.kr.mvisample.ui

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import co.kr.mvisample.design.LocalNavAnimatedVisibilityScope
import co.kr.mvisample.design.LocalSharedTransitionScope
import co.kr.mvisample.design.PokemonTheme
import co.kr.mvisample.feature.detail.DetailScreen
import co.kr.mvisample.feature.detail.navigation.PokemonDetail
import co.kr.mvisample.navigation.Home
import co.kr.mvisample.navigation.HomeContainer
import co.kr.mvisample.navigation.rememberPokedexNavigator
import kotlin.reflect.KType

@Composable
fun PokemonApp() {
    val navController = rememberPokedexNavigator()

    PokemonTheme {
        SharedTransitionLayout {
            CompositionLocalProvider(
                LocalSharedTransitionScope provides this
            ) {
                NavHost(
                    modifier = Modifier
                        .fillMaxSize()
                        .safeContentPadding(),
                    navController = navController.navController,
                    startDestination = Home
                ) {
                    composableWithBasicTransition<Home> {
                        HomeContainer(
                            onNavigateToPokemonDetail = navController::navigatePokemonDetail
                        )
                    }
                    composableWithBasicTransition<PokemonDetail> {
                        DetailScreen(
                            onNavigateToBack = navController::popBackStack
                        )
                    }
                }
            }
        }
    }
}

inline fun <reified T : Any> NavGraphBuilder.composableWithBasicTransition(
    typeMap: Map<KType, @JvmSuppressWildcards NavType<*>> = emptyMap(),
    deepLinks: List<NavDeepLink> = emptyList(),
    noinline enterTransition: (@JvmSuppressWildcards AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?)? = { fadeIn() },
    noinline exitTransition: (@JvmSuppressWildcards AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?)? = { fadeOut() },
    noinline popEnterTransition: (@JvmSuppressWildcards AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?)? = enterTransition,
    noinline popExitTransition: (@JvmSuppressWildcards AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?)? = exitTransition,
    noinline content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit
) {
    composable<T>(
        typeMap = typeMap,
        deepLinks = deepLinks,
        enterTransition = enterTransition,
        exitTransition = exitTransition,
        popEnterTransition = popEnterTransition,
        popExitTransition = popExitTransition
    ) {
        CompositionLocalProvider(
            LocalNavAnimatedVisibilityScope provides this@composable
        ) {
            content(it)
        }
    }
}