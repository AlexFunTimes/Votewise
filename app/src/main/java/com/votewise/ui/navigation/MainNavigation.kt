package com.votewise.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.votewise.navigation.Screen
import com.votewise.ui.screens.*

@Composable
fun MainNavigation() {
    val navController = rememberNavController()
    
    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                navController = navController
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                HomeScreen(navController = navController)
            }
            
            composable(Screen.Search.route) {
                SearchScreen(navController = navController)
            }
            
            composable(Screen.Matches.route) {
                MatchesScreen(navController = navController)
            }
            
            composable(Screen.Profile.route) {
                ProfileScreen(navController = navController)
            }
            
            composable(Screen.CandidateDetail.route) { backStackEntry ->
                val candidateId = backStackEntry.arguments?.getString("candidateId") ?: ""
                CandidateDetailScreen(
                    candidateId = candidateId,
                    navController = navController
                )
            }
            
            composable(Screen.Quiz.route) {
                QuizScreen(navController = navController)
            }
            
            composable(Screen.QuizResults.route) {
                QuizResultsScreen(navController = navController)
            }
            
            composable(Screen.Settings.route) {
                SettingsScreen(navController = navController)
            }
            
            composable(Screen.Privacy.route) {
                PrivacyScreen(navController = navController)
            }
            
            composable(Screen.Notifications.route) {
                NotificationsScreen(navController = navController)
            }
            
            composable(Screen.Subscription.route) {
                SubscriptionScreen(navController = navController)
            }
            
            composable(Screen.About.route) {
                AboutScreen(navController = navController)
            }
        }
    }
}

@Composable
private fun BottomNavigationBar(
    navController: androidx.navigation.NavController
) {
    val items = listOf(
        BottomNavItem("Home", Icons.Default.Home, Screen.Home.route),
        BottomNavItem("Search", Icons.Default.Search, Screen.Search.route),
        BottomNavItem("Matches", Icons.Default.Favorite, Screen.Matches.route),
        BottomNavItem("Profile", Icons.Default.Person, Screen.Profile.route)
    )
    
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    
    NavigationBar {
        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.title) },
                label = { Text(item.title) },
                selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

data class BottomNavItem(
    val title: String,
    val icon: ImageVector,
    val route: String
)


