package com.votewise.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.votewise.data.model.Candidate
import com.votewise.data.model.CandidateStatus
import com.votewise.data.model.ElectionType
import com.votewise.data.model.Contest
import com.votewise.ui.components.CandidateCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MatchesScreen(
    navController: NavController,
    address: String,
    viewModel: MatchesViewModel = viewModel()
) {
    LaunchedEffect(address) {
        viewModel.findCandidates(address)
    }

    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Candidates for $address",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            
            Button(
                onClick = { navController.popBackStack() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                )
            ) {
                Text("Back")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (val state = uiState) {
            is MatchesScreenState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Loading candidates...")
                    }
                }
            }
            is MatchesScreenState.Success -> {
                val contests = state.civicInfo.contests ?: emptyList()
                if (contests.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "No candidates found for this address.",
                                style = MaterialTheme.typography.headlineSmall,
                                color = MaterialTheme.colorScheme.error
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "This could mean:\n• No elections scheduled for this location\n• Address not recognized by the Civic API\n• Elections exist but no contests are available\n• API configuration issue\n\nTry the 'Debug: Test Civic API' button to test with a known address, or use API Diagnostics for troubleshooting.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                } else {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        // Group contests by level (federal, state, local)
                        val federalContests = contests.filter { contest ->
                            contest.level?.contains("country") == true
                        }
                        val stateContests = contests.filter { contest ->
                            contest.level?.contains("administrativeArea1") == true
                        }
                        val localContests = contests.filter { contest ->
                            contest.level?.contains("administrativeArea2") == true || 
                            contest.level?.contains("locality") == true
                        }

                        // Federal Candidates
                        if (federalContests.isNotEmpty()) {
                            item {
                                Text(
                                    text = "Federal Candidates",
                                    style = MaterialTheme.typography.headlineSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                            items(federalContests.flatMap { contest ->
                                contest.candidates?.map { civicInfoCandidate ->
                                    Pair(contest, civicInfoCandidate)
                                } ?: emptyList()
                            }) { (contest, civicInfoCandidate) ->
                                val candidate = Candidate(
                                    id = "${contest.id}_${civicInfoCandidate.name}",
                                    name = civicInfoCandidate.name ?: "",
                                    party = civicInfoCandidate.party ?: "",
                                    imageUrl = civicInfoCandidate.photoUrl ?: "",
                                    office = contest.office ?: "",
                                    district = contest.district?.name,
                                    electionYear = 2024,
                                    status = CandidateStatus.DECLARED,
                                    electionType = ElectionType.FEDERAL,
                                    consistencyScore = 0.0
                                )
                                CandidateCard(candidate = candidate, matchPercentage = null, onClick = {})
                            }
                        }

                        // State Candidates
                        if (stateContests.isNotEmpty()) {
                            item {
                                Text(
                                    text = "State Candidates",
                                    style = MaterialTheme.typography.headlineSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                            items(stateContests.flatMap { contest ->
                                contest.candidates?.map { civicInfoCandidate ->
                                    Pair(contest, civicInfoCandidate)
                                } ?: emptyList()
                            }) { (contest, civicInfoCandidate) ->
                                val candidate = Candidate(
                                    id = "${contest.id}_${civicInfoCandidate.name}",
                                    name = civicInfoCandidate.name ?: "",
                                    party = civicInfoCandidate.party ?: "",
                                    imageUrl = civicInfoCandidate.photoUrl ?: "",
                                    office = contest.office ?: "",
                                    district = contest.district?.name,
                                    electionYear = 2024,
                                    status = CandidateStatus.DECLARED,
                                    electionType = ElectionType.STATE,
                                    consistencyScore = 0.0
                                )
                                CandidateCard(candidate = candidate, matchPercentage = null, onClick = {})
                            }
                        }

                        // Local Candidates
                        if (localContests.isNotEmpty()) {
                            item {
                                Text(
                                    text = "Local Candidates",
                                    style = MaterialTheme.typography.headlineSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                            items(localContests.flatMap { contest ->
                                contest.candidates?.map { civicInfoCandidate ->
                                    Pair(contest, civicInfoCandidate)
                                } ?: emptyList()
                            }) { (contest, civicInfoCandidate) ->
                                val candidate = Candidate(
                                    id = "${contest.id}_${civicInfoCandidate.name}",
                                    name = civicInfoCandidate.name ?: "",
                                    party = civicInfoCandidate.party ?: "",
                                    imageUrl = civicInfoCandidate.photoUrl ?: "",
                                    office = contest.office ?: "",
                                    district = contest.district?.name,
                                    electionYear = 2024,
                                    status = CandidateStatus.DECLARED,
                                    electionType = ElectionType.LOCAL,
                                    consistencyScore = 0.0
                                )
                                CandidateCard(candidate = candidate, matchPercentage = null, onClick = {})
                            }
                        }
                    }
                }
            }
            is MatchesScreenState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Error loading candidates",
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = state.message,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}
