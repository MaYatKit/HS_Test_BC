package com.example.hs_test_bc.ui.nav

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.hs_test_bc.ui.nav.Screen.Repository
import com.example.hs_test_bc.ui.nav.Screen.Search
import com.example.hs_test_bc.ui.screen.home.HomeScreen
import com.example.hs_test_bc.ui.screen.repository.RepositoryScreen
import com.example.hs_test_bc.ui.screen.search.SearchScreen

@Composable
fun Navigation(
    snackBarHostState: SnackbarHostState = remember { SnackbarHostState() }
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                goToSearch = {
                    navController.navigate(Search.route)
                },
                goToRepository = { owner, repo ->
                    navController.navigate(Repository.createRoute(owner, repo))
                },
                snackBarHostState = snackBarHostState
            )
        }

        composable(Search.route) {
            SearchScreen(
                goToRepository = { owner, repo ->
                    navController.navigate(Repository.createRoute(owner, repo))
                },
                goBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = Repository.route,
            arguments = listOf(
                navArgument("owner") { type = NavType.StringType },
                navArgument("repo") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val owner = backStackEntry.arguments?.getString("owner") ?: ""
            val repo = backStackEntry.arguments?.getString("repo") ?: ""

            RepositoryScreen(
                owner = owner,
                repo = repo,
                goBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Search : Screen("search")
    object Repository : Screen("repository/{owner}/{repo}") {
        fun createRoute(owner: String, repo: String) = "repository/$owner/$repo"
    }


}