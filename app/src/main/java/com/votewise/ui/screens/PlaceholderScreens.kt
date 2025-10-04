package com.votewise.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun QuizScreen(navController: NavController) {
    PlaceholderScreen(
        title = "Political Quiz",
        description = "Answer questions about your political views to find matching candidates",
        icon = Icons.Default.Quiz,
        navController = navController
    )
}

@Composable
fun QuizResultsScreen(navController: NavController) {
    PlaceholderScreen(
        title = "Quiz Results",
        description = "View your political quiz results and candidate matches",
        icon = Icons.Default.Assessment,
        navController = navController
    )
}

@Composable
fun SettingsScreen(navController: NavController) {
    PlaceholderScreen(
        title = "Settings",
        description = "Manage your app preferences and account settings",
        icon = Icons.Default.Settings,
        navController = navController
    )
}

@Composable
fun PrivacyScreen(navController: NavController) {
    PlaceholderScreen(
        title = "Privacy Settings",
        description = "Control your data privacy and sharing preferences",
        icon = Icons.Default.PrivacyTip,
        navController = navController
    )
}

@Composable
fun NotificationsScreen(navController: NavController) {
    PlaceholderScreen(
        title = "Notifications",
        description = "Manage your notification preferences",
        icon = Icons.Default.Notifications,
        navController = navController
    )
}

@Composable
fun SubscriptionScreen(navController: NavController) {
    PlaceholderScreen(
        title = "Subscription",
        description = "Upgrade to Premium for advanced features",
        icon = Icons.Default.Star,
        navController = navController
    )
}

@Composable
fun AboutScreen(navController: NavController) {
    PlaceholderScreen(
        title = "About VoteWise",
        description = "Learn more about VoteWise and our mission",
        icon = Icons.Default.Info,
        navController = navController
    )
}

@Composable
private fun PlaceholderScreen(
    title: String,
    description: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    navController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = description,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Button(
            onClick = { navController.popBackStack() }
        ) {
            Icon(Icons.Default.ArrowBack, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Go Back")
        }
    }
}


