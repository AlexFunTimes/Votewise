package com.votewise.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.votewise.ui.viewmodel.HomeViewModel
import com.votewise.ui.screens.AddressInputScreen
import com.votewise.ui.screens.HomeScreen
import androidx.compose.foundation.layout.fillMaxSize

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    homeViewModel: HomeViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Screen.AddressInput.route,
        modifier = modifier.fillMaxSize()
    ) {
        // Explicitly define parameters for composable to be absolutely clear
        composable(route = Screen.AddressInput.route) {
            AddressInputScreen(
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route)
                },
                homeViewModel = homeViewModel
            )
        }
        composable(route = Screen.Home.route) {
            HomeScreen(
                homeViewModel = homeViewModel,
                navController = navController
            )
        }
    }
}

sealed class Screen(val route: String) {
    object AddressInput : Screen("address_input")
    object Home : Screen("home")
}
