package com.votewise.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
fun SearchScreen(
    navController: NavController
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("All") }
    var selectedParty by remember { mutableStateOf("All") }
    var selectedOffice by remember { mutableStateOf("All") }

    val filters = listOf("All", "Federal", "State", "County", "Local")
    val parties = listOf("All", "Democratic", "Republican", "Independent", "Green", "Libertarian")
    val offices = listOf("All", "President", "Senate", "House", "Governor", "Mayor")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search candidates, offices, etc.") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { searchQuery = "" }) {
                        Icon(Icons.Default.Close, contentDescription = "Clear search")
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Filters
        FilterChips(
            title = "Election Type",
            options = filters,
            selectedOption = selectedFilter,
            onOptionSelected = { selectedFilter = it }
        )

        Spacer(modifier = Modifier.height(12.dp))

        FilterChips(
            title = "Party",
            options = parties,
            selectedOption = selectedParty,
            onOptionSelected = { selectedParty = it }
        )

        Spacer(modifier = Modifier.height(12.dp))

        FilterChips(
            title = "Office",
            options = offices,
            selectedOption = selectedOffice,
            onOptionSelected = { selectedOffice = it }
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Results
        Text(
            text = "Search Results",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        val mockCandidates = remember {
            listOf(
                Candidate(
                    id = "3",
                    name = "Alice Johnson",
                    party = "Independent",
                    imageUrl = "https://example.com/alice_johnson.jpg",
                    office = "Mayor",
                    district = "Metropolis",
                    electionYear = 2024,
                    status = CandidateStatus.DECLARED,
                    electionType = ElectionType.COUNTY,
                    consistencyScore = 0.91
                ),
                Candidate(
                    id = "4",
                    name = "Bob Williams",
                    party = "Green",
                    imageUrl = "https://example.com/bob_williams.jpg",
                    office = "City Council",
                    district = "District 4",
                    electionYear = 2024,
                    status = CandidateStatus.QUALIFIED,
                    electionType = ElectionType.COUNTY,
                    consistencyScore = 0.82
                )
            )
        }

        if (mockCandidates.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(bottom = 64.dp) // Adjust padding to center better
                ) {
                    Icon(
                        Icons.Default.SearchOff,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "No candidates found",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Try adjusting your search or filter criteria.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                    )
                }
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(items = mockCandidates, key = { it.id }) { candidate ->
                    CandidateCard(
                        candidate = candidate,
                        onClick = {
                            navController.navigate("candidate_detail/${candidate.id}")
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FilterChips(
    title: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Medium
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 2.dp)
        ) {
            items(items = options, key = { it }) { option ->
                FilterChip(
                    onClick = { onOptionSelected(option) },
                    label = { Text(option) },
                    selected = option == selectedOption,
                    leadingIcon = if (option == selectedOption) {
                        {
                            Icon(
                                imageVector = Icons.Default.Done,
                                contentDescription = "Selected",
                                modifier = Modifier.size(FilterChipDefaults.IconSize)
                            )
                        }
                    } else null,
                )
            }
        }
    }
}
