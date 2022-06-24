package com.example.funnymemes.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.funnymemes.ui.screens.favorite.Favorite
import com.example.funnymemes.ui.screens.home.Home
import com.example.funnymemes.ui.screens.settings.Settings
import com.example.funnymemes.ui.shared.SharedViewModel

sealed class Screen(val route: String) {

    object HomeScreen : Screen("home")
    object FavoriteScreen : Screen("favorite")
    object SettingsScreen : Screen("settings")

    companion object {
        fun fromRoute(route: String?) =
            when (route) {
                null -> HomeScreen
                HomeScreen.route -> HomeScreen
                FavoriteScreen.route -> FavoriteScreen
                SettingsScreen.route -> SettingsScreen
                else -> throw IllegalArgumentException("Route $this is not recognized.")
            }
    }
}

@Composable
fun NavigationHost(
    navController: NavHostController,
    viewModel: SharedViewModel,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.HomeScreen.route,
        modifier = modifier
    ) {
        composable(Screen.HomeScreen.route) {
            Home(viewModel = viewModel)
        }
        composable(Screen.FavoriteScreen.route) {
            Favorite(viewModel = viewModel)
        }
        composable(Screen.SettingsScreen.route) {
            Settings(viewModel = viewModel)
        }
    }
}