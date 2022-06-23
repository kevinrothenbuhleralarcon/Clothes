package ch.kra.wardrobe

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import ch.kra.wardrobe.cloth_list.presentation.add_edit_wardrobe.screen.AddEditWardrobeScreen
import ch.kra.wardrobe.cloth_list.presentation.list_wardrobe.screen.ListWardrobeScreen
import ch.kra.wardrobe.core.Routes

@Composable
fun Navigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Routes.LIST_WARDROBE
    ) {
        composable(route = Routes.LIST_WARDROBE) {
            ListWardrobeScreen(
                navigate = { navigateEvent ->
                    navController.navigate(navigateEvent.route)
                }
            )
        }

        composable(
            route = Routes.ADD_EDIT_WARDROBE + "?wardrobeId={wardrobeId}",
            arguments = listOf(
                navArgument("wardrobeId") {
                    type = NavType.IntType
                    defaultValue = -1
                }
            )
        ) {
            AddEditWardrobeScreen()
        }
    }
}