package com.votewise.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.votewise.navigation.Screen
import com.votewise.ui.components.CandidateRow
import com.votewise.ui.viewmodel.HomeViewModel

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel,
    navController: NavController
) {
    val candidates by homeViewModel.candidates.collectAsState()
    val isLoading by homeViewModel.isLoading.collectAsState()
    val error by homeViewModel.error.collectAsState()
    val predictions by homeViewModel.predictions.collectAsState()
    var addressText by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                value = addressText,
                onValueChange = {
                    addressText = it
                    homeViewModel.fetchPredictions(it)
                },
                label = { Text("Enter your address") }
            )
            LazyColumn {
                items(predictions) { prediction ->
                    Text(
                        text = prediction.getFullText(null).toString(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                addressText = prediction.getFullText(null).toString()
                            }
                            .padding(8.dp)
                    )
                }
            }
            Button(
                onClick = { homeViewModel.findCandidates(addressText) },
                enabled = addressText.isNotBlank()
            ) {
                Text("Find Candidates")
            }
        }
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (error != null) {
            Snackbar(
                modifier = Modifier.padding(16.dp),
                action = {
                    Button(onClick = { /* TODO: Add dismiss action */ }) {
                        Text("Dismiss")
                    }
                }
            ) {
                Text(text = error!!)
            }
        } else {
            LazyColumn {
                items(candidates) { candidate ->
                    CandidateRow(candidate = candidate) {
                        // Use candidate.name as the unique identifier for navigation
                        navController.navigate(Screen.CandidateDetail.route + "/${candidate.name}")
                    }
                }
            }
        }
    }
}