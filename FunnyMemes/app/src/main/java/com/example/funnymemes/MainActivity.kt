package com.example.funnymemes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.funnymemes.ui.components.BottomBar
import com.example.funnymemes.ui.components.NavigationHost
import com.example.funnymemes.ui.components.Screen
import com.example.funnymemes.ui.shared.SharedViewModel
import com.example.funnymemes.ui.theme.FunnyMemesTheme
import java.lang.IllegalArgumentException

class MainActivity : ComponentActivity() {

    private val viewModel = SharedViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FunnyMemesTheme {
                MainScreen(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun MainScreen(viewModel: SharedViewModel) {
    val navController = rememberNavController()
    val backstackEntry = navController.currentBackStackEntryAsState()
    val currentScreen = try {
        Screen.fromRoute(backstackEntry.value?.destination?.route)
    } catch (e: IllegalArgumentException) {
        e.printStackTrace()
        return
    }

    Scaffold(
        topBar = {
            TopAppBar {
                Text(text = viewModel.title)
            }
        },
        bottomBar = {
            BottomBar(
                currentScreen = currentScreen
            ) { item ->
                navController.navigate(item.route) {
                    launchSingleTop = true
                }
            }
        }
    ) { innerPadding ->
        NavigationHost(
            navController = navController,
            viewModel = viewModel,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

