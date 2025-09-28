package com.votewise.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.votewise.ui.screens.OnboardingScreen
import com.votewise.ui.screens.HomeScreen
import com.votewise.ui.screens.CandidateDetailScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import com.votewise.data.CandidateRepository
import com.votewise.ui.viewmodel.HomeViewModel
import com.votewise.ui.viewmodel.HomeViewModelFactory
import com.votewise.ui.screens.AddressInputScreen

@Composable
fun NavGraph(
    candidateRepository: CandidateRepository,
    startDestination: String
) {
    val navController = rememberNavController()
    val homeViewModel: HomeViewModel = viewModel(factory = HomeViewModelFactory(candidateRepository))

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(route = Screen.Onboarding.route) {
            OnboardingScreen(navController = navController)
        }
        composable(route = Screen.AddressInput.route) {
            AddressInputScreen(
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.AddressInput.route) {
                            inclusive = true
                        }
                    }
                },
                homeViewModel = homeViewModel
            )
        }
        composable(route = Screen.Home.route) {
            HomeScreen(navController = navController, homeViewModel = homeViewModel)
        }
        composable(route = Screen.CandidateDetail.route + "/{candidateId}") { backStackEntry ->
            val candidateId = backStackEntry.arguments?.getString("candidateId")
            CandidateDetailScreen(candidateId = candidateId, navController = navController)
        }
    }
}
