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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import co.kr.mvisample.core.navigation.PokemonRoutes
import co.kr.mvisample.core.theme.LocalNavAnimatedVisibilityScope
import co.kr.mvisample.core.theme.LocalSharedTransitionScope
import co.kr.mvisample.core.theme.PokemonTheme
import co.kr.mvisample.feature.detail.DetailScreen
import co.kr.mvisample.feature.home.HomeContainer
import kotlin.reflect.KType

@Composable
fun PokemonApp() {
    val navController = rememberNavController()

    PokemonTheme {
        SharedTransitionLayout {
            CompositionLocalProvider(
                LocalSharedTransitionScope provides this
            ) {
                NavHost(
                    navController = navController,
                    startDestination = PokemonRoutes.Home
                ) {
                    composableWithBasicTransition<PokemonRoutes.Home> {
                        HomeContainer(
                            onNavigateToPokemonDetail = { pokemonName, isDiscovered ->
                                navController.navigate(PokemonRoutes.PokemonDetail(pokemonName, isDiscovered))
                            }
                        )
                    }
                    composableWithBasicTransition<PokemonRoutes.PokemonDetail> {
                        DetailScreen()
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