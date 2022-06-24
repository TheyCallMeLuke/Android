package com.example.funnymemes.ui.components

import androidx.annotation.StringRes
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.example.funnymemes.R

enum class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    @StringRes val label: Int
) {
    Home(Screen.HomeScreen.route, Icons.Default.Home, R.string.home_screen),
    Favorite(Screen.FavoriteScreen.route, Icons.Default.Favorite, R.string.favorite_screen),
    Settings(Screen.SettingsScreen.route, Icons.Default.Settings, R.string.settings_screen)
}

@Composable
fun BottomBar(currentScreen: Screen, onNavItemClicked: (BottomNavItem) -> Unit) {
    BottomNavigation {
        BottomNavItem.values().forEach { item ->
            BottomNavigationItem(
                selected = (currentScreen.route == item.route),
                onClick = { onNavItemClicked(item) },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = stringResource(id = item.label)
                    )
                },
                label = {
                    Text(text = stringResource(id = item.label))
                }
            )
        }
    }
}