package com.votewise.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.votewise.data.model.Result
import com.votewise.navigation.Screen
import com.votewise.ui.components.CandidateRow
import com.votewise.ui.viewmodel.HomeViewModel

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel,
    navController: NavController
) {
    val voterInfo by homeViewModel.voterInfo.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (val result = voterInfo) {
            is Result.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is Result.Success -> {
                if (result.data.contests != null) {
                    LazyColumn {
                        result.data.contests.forEach { contest ->
                            if (contest.candidates != null) {
                                items(contest.candidates) { candidate ->
                                    CandidateRow(candidate = candidate) {
                                        navController.navigate(Screen.CandidateDetail.route + "/${candidate.name}")
                                    }
                                }
                            }
                        }
                    }
                }
            }
            is Result.Error -> {
                Snackbar(
                    modifier = Modifier.padding(16.dp),
                    action = {
                        Button(onClick = { /* TODO: Add dismiss action */ }) {
                            Text("Dismiss")
                        }
                    }
                ) {
                    Text(text = result.exception.message ?: "An unknown error occurred")
                }
            }
        }
    }
}