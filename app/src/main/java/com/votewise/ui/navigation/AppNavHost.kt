package com.votewise.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.votewise.ui.screens.MatchesViewModelFactory
import com.votewise.ui.screens.AddressInputScreen
import com.votewise.ui.screens.MatchesScreen
import com.votewise.ui.screens.ApiKeyDiagnosticScreen
import androidx.compose.foundation.layout.fillMaxSize
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.votewise.data.api.CivicInfoApiService
import com.votewise.data.api.RetrofitClient

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    val civicInfoApiService = RetrofitClient.instance.create(CivicInfoApiService::class.java)
    val matchesViewModelFactory = MatchesViewModelFactory(civicInfoApiService)

    NavHost(
        navController = navController,
        startDestination = Screen.AddressInput.route,
        modifier = modifier.fillMaxSize()
    ) {
        composable(route = Screen.ApiKeyDiagnostic.route) {
            ApiKeyDiagnosticScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
        composable(route = Screen.AddressInput.route) {
            AddressInputScreen(
                onNavigateToMatches = { address ->
                    navController.navigate(Screen.Matches.createRoute(address))
                },
                onNavigateToDiagnostics = {
                    navController.navigate(Screen.ApiKeyDiagnostic.route)
                }
            )
        }
        composable(
            route = Screen.Matches.route,
            arguments = listOf(navArgument("address") { type = NavType.StringType })
        ) { backStackEntry ->
            MatchesScreen(
                navController = navController,
                address = backStackEntry.arguments?.getString("address") ?: "",
                viewModel = viewModel(factory = matchesViewModelFactory)
            )
        }
    }
}
