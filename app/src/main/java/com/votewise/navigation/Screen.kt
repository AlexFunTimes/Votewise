package com.votewise.navigation

sealed class Screen(val route: String) {
    object Onboarding : Screen("onboarding")
    object Home : Screen("home")
    object Search : Screen("search")
    object Matches : Screen("matches")
    object Profile : Screen("profile")
    object CandidateDetail : Screen("candidate_detail/{candidateId}")
    object AddressInput : Screen("address_input")
    object Quiz : Screen("quiz")
    object QuizResults : Screen("quiz_results")
    object Settings : Screen("settings")
    object Privacy : Screen("privacy")
    object Notifications : Screen("notifications")
    object Subscription : Screen("subscription")
    object About : Screen("about")
    
    fun createRoute(vararg args: String): String {
        return route.replace(Regex("\\{[^}]+\\}")) { args[it.range.first] }
    }
}
