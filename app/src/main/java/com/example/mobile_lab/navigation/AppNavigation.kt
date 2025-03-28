package com.example.mobile_lab.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mobile_lab.ui.screens.CocktailDetailsScreen
import com.example.mobile_lab.ui.screens.CocktailListScreen
import androidx.navigation.NavType
import androidx.navigation.navArgument

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "cocktailList"){
        composable("cocktailList") {
            CocktailListScreen(
                onCocktailClick = { cocktailId ->
                    navController.navigate("cocktailDetail/$cocktailId")
                }
            )
        }
        composable(
            route = "cocktailDetail/{cocktailId}",
            arguments = listOf(
                navArgument("cocktailId") { type = NavType.StringType }
            )
        ){
            backStackEntry ->
            val cocktailId = backStackEntry.arguments?.getString("cocktailId") ?: ""
            CocktailDetailsScreen(
                cocktailId = cocktailId,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}