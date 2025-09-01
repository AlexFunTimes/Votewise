package com.votewise.navigation

sealed class Screen(val route: String) {
    object Onboarding : Screen("onboarding")
    object Home : Screen("home")
    object CandidateDetail : Screen("candidate_detail")
}