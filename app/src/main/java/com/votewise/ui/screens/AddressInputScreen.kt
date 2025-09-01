package com.votewise.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.google.android.libraries.places.api.model.Place
import com.votewise.app.R
import com.votewise.ui.components.AddressAutocompleteTextField
import com.votewise.ui.viewmodel.HomeViewModel

@Composable
fun AddressInputScreen(
    onNavigateToHome: () -> Unit,
    homeViewModel: HomeViewModel
) {
    var addressText by remember { mutableStateOf("") }
    var selectedPlace by remember { mutableStateOf<Place?>(null) }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.votewise_pioneers_words),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.6f))
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AddressAutocompleteTextField(
                onQueryChanged = { query ->
                    addressText = query
                    selectedPlace = null
                },
                onPlaceSelected = { place ->
                    selectedPlace = place
                    addressText = place.address.orEmpty()
                },
                onSearchClick = {
                    selectedPlace?.let {
                        homeViewModel.findCandidates(it.address.orEmpty())
                        onNavigateToHome()
                    }
                }
            )

            Button(
                onClick = {
                    selectedPlace?.let {
                        homeViewModel.findCandidates(it.address.orEmpty())
                        onNavigateToHome()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                enabled = selectedPlace != null,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White
                )
            ) {
                Text("Find Candidates")
            }
        }
    }
}