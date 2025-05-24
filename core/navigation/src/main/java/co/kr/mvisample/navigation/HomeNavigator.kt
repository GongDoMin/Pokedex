package co.kr.mvisample.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@Composable
fun rememberHomeNavigator(
    navController: NavHostController = rememberNavController()
) : HomeNavigator =
    remember(navController) {
        HomeNavigator(navController)
    }

@Immutable
class HomeNavigator(
    val navController: NavHostController
) {
    fun navigateHomeRoutes(homeRoutes: HomeRoutes) {
        navController.navigate(homeRoutes) {
            launchSingleTop = true
            restoreState = true
            popUpTo(navController.graph.id) {
                saveState = true
            }
        }
    }
}