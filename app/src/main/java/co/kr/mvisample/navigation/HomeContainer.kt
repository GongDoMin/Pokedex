package co.kr.mvisample.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import co.kr.mvisample.components.HomeRoutes
import co.kr.mvisample.components.HomeSections
import co.kr.mvisample.components.HomeSections.Companion.toSection
import co.kr.mvisample.components.PokemonBottomBar
import co.kr.mvisample.design.PokemonTheme
import co.kr.mvisample.feature.home.computer.ComputerScreen
import co.kr.mvisample.feature.home.pokedex.PokedexScreen
import kotlinx.collections.immutable.toPersistentList
import kotlinx.serialization.Serializable

@Serializable data object Home

@Composable
fun HomeContainer(
    onNavigateToPokemonDetail: (id: Int, name: String, isDiscovered: Boolean) -> Unit
) {
    val nestedNavController = rememberHomeNavigator()
    val navBackStackEntry by nestedNavController.navController.currentBackStackEntryAsState()
    val currentSection = navBackStackEntry?.destination?.route.toSection()

    Scaffold(
        containerColor = PokemonTheme.colors.backgroundRed,
        bottomBar = {
            PokemonBottomBar(
                tabs = HomeSections.entries.toPersistentList(),
                currentSection = currentSection ?: HomeSections.POKEDEX,
                onClickSection = {
                    if (it != (currentSection?.route ?: HomeSections.POKEDEX)) {
                        nestedNavController.navigateHomeRoutes(it)
                    }
                }
            )
        }
    ) { innerPadding ->
        NavHost(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            navController = nestedNavController.navController,
            startDestination = HomeRoutes.Pokedex
        ) {
            composable<HomeRoutes.Pokedex> {
                PokedexScreen(
                    onNavigateToPokemonDetail = onNavigateToPokemonDetail
                )
            }
            composable<HomeRoutes.Computer> {
                ComputerScreen()
            }
        }
    }
}