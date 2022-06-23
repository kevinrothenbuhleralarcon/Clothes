package ch.kra.wardrobe

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
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
            ListWardrobeScreen()
        }

        composable(route = Routes.ADD_EDIT_WARDROBE) {
            AddEditWardrobeScreen()
        }
    }
}