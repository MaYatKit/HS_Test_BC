package com.example.hs_test_bc.ui.nav

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.hs_test_bc.ui.screen.home.HomeScreen

@Composable
fun Navigation(snackBarHostState: SnackbarHostState = remember { SnackbarHostState() }) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                snackBarHostState = snackBarHostState
            )
        }
    }
}

sealed class Screen(val route: String) {
    object Home : Screen("home")
}