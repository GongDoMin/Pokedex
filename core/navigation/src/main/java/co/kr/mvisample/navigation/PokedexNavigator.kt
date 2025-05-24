package co.kr.mvisample.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@Composable
fun rememberPokedexNavigator(
    navController: NavHostController = rememberNavController()
) : PokedexNavigator =
    remember(navController) {
        PokedexNavigator(navController)
    }

@Immutable
class PokedexNavigator(
    val navController: NavHostController
) {
    fun popBackStack() {
        navController.popBackStack()
    }

    fun navigatePokemonDetail(id: Int, name: String, isDiscovered: Boolean) {
        navController.navigate(PokemonRoutes.PokemonDetail(id, name, isDiscovered))
    }
}