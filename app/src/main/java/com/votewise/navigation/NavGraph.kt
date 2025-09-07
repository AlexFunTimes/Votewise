package com.votewise.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.votewise.ui.screens.OnboardingScreen
import com.votewise.ui.screens.HomeScreen
import com.votewise.ui.screens.CandidateDetailScreen
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.votewise.data.CandidateRepository
import com.votewise.data.UserPreferencesRepository
import com.votewise.ui.viewmodel.HomeViewModel
import com.votewise.ui.viewmodel.HomeViewModelFactory

@Composable
fun NavGraph(candidateRepository: CandidateRepository) {
    val navController = rememberNavController()
    val context = LocalContext.current
    val userPreferencesRepository = UserPreferencesRepository(context)
    val zipCodeState = userPreferencesRepository.userPreferencesFlow.collectAsState(initial = "")

    val startDestination = if (zipCodeState.value.isNotBlank()) {
        Screen.Home.route
    } else {
        Screen.Onboarding.route
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(route = Screen.Onboarding.route) {
            OnboardingScreen(navController = navController)
        }
        composable(route = Screen.Home.route) {
            val homeViewModel: HomeViewModel = viewModel(factory = HomeViewModelFactory(candidateRepository))
            HomeScreen(navController = navController, homeViewModel = homeViewModel)
        }
        composable(route = Screen.CandidateDetail.route + "/{candidateId}") { backStackEntry ->
            val candidateId = backStackEntry.arguments?.getString("candidateId")
            CandidateDetailScreen(candidateId = candidateId, navController = navController)
        }
    }
}
