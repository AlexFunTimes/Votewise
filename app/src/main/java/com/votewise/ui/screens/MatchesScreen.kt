package com.votewise.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.votewise.data.model.Candidate
import com.votewise.data.model.CandidateStatus
import com.votewise.data.model.ElectionType
import com.votewise.ui.components.CandidateCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MatchesScreen(
    navController: NavController
) {
    var sortBy by remember { mutableStateOf("Match %") }
    var filterBy by remember { mutableStateOf("All") }

    val sortOptions = listOf("Match %", "Name", "Party", "Office")
    val filterOptions = listOf("All", "80%+", "60%+", "40%+")

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
                text = "Your Matches",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            IconButton(onClick = { navController.navigate("quiz") }) {
                Icon(Icons.Default.Refresh, contentDescription = "Retake Quiz")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Sort and Filter Controls
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Sort Dropdown
            var expandedSort by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = expandedSort,
                onExpandedChange = { expandedSort = !expandedSort }
            ) {
                OutlinedTextField(
                    value = sortBy,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Sort by") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedSort) },
                    modifier = Modifier
                        .weight(1f)
                        .menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = expandedSort,
                    onDismissRequest = { expandedSort = false }
                ) {
                    sortOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                sortBy = option
                                expandedSort = false
                            }
                        )
                    }
                }
            }

            // Filter Dropdown
            var expandedFilter by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = expandedFilter,
                onExpandedChange = { expandedFilter = !expandedFilter }
            ) {
                OutlinedTextField(
                    value = filterBy,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Filter") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedFilter) },
                    modifier = Modifier
                        .weight(1f)
                        .menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = expandedFilter,
                    onDismissRequest = { expandedFilter = false }
                ) {
                    filterOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                filterBy = option
                                expandedFilter = false
                            }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Matches List
        val mockMatches = remember {
            listOf(
                Match(
                    candidate = Candidate(
                        id = "1",
                        name = "Jane Doe",
                        party = "Democrat",
                        imageUrl = "https://example.com/jane_doe.jpg",
                        office = "President",
                        district = null,
                        electionYear = 2024,
                        status = CandidateStatus.DECLARED,
                        electionType = ElectionType.FEDERAL,
                        consistencyScore = 0.85
                    ),
                    matchPercentage = 92.5
                ),
                Match(
                    candidate = Candidate(
                        id = "2",
                        name = "John Smith",
                        party = "Republican",
                        imageUrl = "https://example.com/john_smith.jpg",
                        office = "Senator",
                        district = "12",
                        electionYear = 2024,
                        status = CandidateStatus.QUALIFIED,
                        electionType = ElectionType.STATE,
                        consistencyScore = 0.78
                    ),
                    matchPercentage = 78.2
                )
            )
        }

        if (mockMatches.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Default.Favorite,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "No matches yet",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Take the political quiz to find your matches",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { navController.navigate("quiz") }
                    ) {
                        Icon(Icons.Default.Quiz, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Take Quiz")
                    }
                }
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(mockMatches) { match ->
                    CandidateCard(
                        candidate = match.candidate,
                        matchPercentage = match.matchPercentage,
                        onClick = {
                            navController.navigate("candidate_detail/${match.candidate.id}")
                        }
                    )
                }
            }
        }
    }
}

data class Match(
    val candidate: Candidate,
    val matchPercentage: Double
)