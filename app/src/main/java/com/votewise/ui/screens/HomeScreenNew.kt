package com.votewise.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.votewise.ui.components.CandidateCard
import com.votewise.ui.viewmodel.HomeViewModel
import com.votewise.data.model.Result
import com.votewise.data.model.Candidate
import com.votewise.data.model.CivicInfoCandidate
import com.votewise.data.api.CivicInfoApiService
import com.votewise.data.CandidateRepository
import com.votewise.ui.viewmodel.HomeViewModelFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = viewModel(
        factory = HomeViewModelFactory(
            CandidateRepository(
                Retrofit.Builder()
                    .baseUrl("https://www.googleapis.com/civicinfo/v2/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(CivicInfoApiService::class.java)
            )
        )
    )
) {
    val voterInfoState by viewModel.voterInfo.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Welcome Section
        item {
            WelcomeSection(
                userName = "User", // Replace with actual user name
                onTakeQuiz = { navController.navigate("quiz") }
            )
        }

        // Quick Actions
        item {
            QuickActionsSection(navController = navController)
        }

        // Content based on voterInfoState
        when (voterInfoState) {
            is Result.Loading -> {
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
            is Result.Success -> {
                val civicInfo = (voterInfoState as Result.Success<com.votewise.data.model.CivicInfoResponse>).data
                val candidates = civicInfo.contests?.flatMap { it.candidates ?: emptyList() }?.map { it.toCandidate() } ?: emptyList()
                
                // Top Matches (dummy data)
                if (candidates.isNotEmpty()) {
                    item {
                        SectionHeader(
                            title = "Your Top Matches",
                            actionText = "See All",
                            onActionClick = { navController.navigate("matches") }
                        )
                    }

                    item {
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            contentPadding = PaddingValues(horizontal = 4.dp)
                        ) {
                            items(candidates.take(5)) { candidate ->
                                CandidateCard(
                                    candidate = candidate,
                                    matchPercentage = (50..99).random().toDouble(), // dummy data
                                    onClick = {
                                        navController.navigate("candidate_detail/${candidate.id}")
                                    },
                                    modifier = Modifier.width(280.dp)
                                )
                            }
                        }
                    }
                }

                // Recent Candidates
                item {
                    SectionHeader(
                        title = "Recent Candidates",
                        actionText = "Search",
                        onActionClick = { navController.navigate("search") }
                    )
                }

                items(candidates.take(10)) { candidate ->
                    CandidateCard(
                        candidate = candidate,
                        onClick = {
                            navController.navigate("candidate_detail/${candidate.id}")
                        }
                    )
                }
            }
            is Result.Error -> {
                item {
                    Text(
                        text = "Error: ${(voterInfoState as Result.Error).exception.message}",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

private fun CivicInfoCandidate.toCandidate(): Candidate {
    return Candidate(
        id = this.name ?: "",
        name = this.name ?: "Unknown",
        party = this.party ?: "Unknown",
        imageUrl = this.photoUrl,
        office = null,
        district = null,
        electionYear = null,
        status = null,
        electionType = null,
        consistencyScore = null
    )
}

@Composable
private fun WelcomeSection(
    userName: String?,
    onTakeQuiz: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = if (userName != null) "Welcome back, $userName!" else "Welcome to VoteWise!",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Discover candidates that align with your values and make informed voting decisions.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onTakeQuiz,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Quiz, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Take Political Quiz")
            }
        }
    }
}

@Composable
private fun QuickActionsSection(navController: NavController) {
    Column {
        Text(
            text = "Quick Actions",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            QuickActionCard(
                title = "Search Candidates",
                icon = Icons.Default.Search,
                onClick = { navController.navigate("search") },
                modifier = Modifier.weight(1f)
            )

            QuickActionCard(
                title = "View Matches",
                icon = Icons.Default.Favorite,
                onClick = { navController.navigate("matches") },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            QuickActionCard(
                title = "Election Calendar",
                icon = Icons.Default.CalendarToday,
                onClick = { /* Navigate to calendar */ },
                modifier = Modifier.weight(1f)
            )

            QuickActionCard(
                title = "Voter Registration",
                icon = Icons.Default.HowToVote,
                onClick = { /* Navigate to registration */ },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun QuickActionCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                modifier = Modifier.size(32.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}

@Composable
private fun SectionHeader(
    title: String,
    actionText: String,
    onActionClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        TextButton(onClick = onActionClick) {
            Text(actionText)
            Icon(
                Icons.Default.ArrowForward,
                contentDescription = null,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}
