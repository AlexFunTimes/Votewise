package com.votewise.ui.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.android.libraries.places.api.model.Place
import com.votewise.app.R
import com.votewise.ui.components.AddressAutocompleteTextField

@Composable
fun AddressInputScreen(
    onNavigateToMatches: (String) -> Unit,
    onNavigateToDiagnostics: () -> Unit = {}
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
                .padding(16.dp)
                .background(
                    color = Color.Black.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AddressAutocompleteTextField(
                onQueryChanged = { query ->
                    addressText = query
                    selectedPlace = null
                },
                onPlaceSelected = { place ->
                    Log.d("AddressInputScreen", "Place selected: ${place.address}")
                    selectedPlace = place
                    addressText = place.address.orEmpty()
                },
                onSearchClick = {
                    // This will be handled by the Get Candidates button
                }
            )
            
            // Show selected address confirmation
            if (selectedPlace != null) {
                Spacer(modifier = Modifier.height(12.dp))
                androidx.compose.material3.Text(
                    text = "✓ Address verified: $addressText",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Medium
                )
            } else {
                Spacer(modifier = Modifier.height(12.dp))
                androidx.compose.material3.Text(
                    text = "Please select an address from the dropdown",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Button(
                onClick = {
                    Log.d("AddressInputScreen", "Search Candidates button clicked. Selected place: $selectedPlace, Address text: $addressText")
                    val currentPlace = selectedPlace
                    if (currentPlace != null) {
                        val verifiedAddress = currentPlace.address ?: addressText
                        Log.d("AddressInputScreen", "Navigating to matches with verified address: $verifiedAddress")
                        onNavigateToMatches(verifiedAddress)
                    } else if (addressText.isNotEmpty()) {
                        Log.d("AddressInputScreen", "No place selected, but using manual address: $addressText")
                        onNavigateToMatches(addressText)
                    } else {
                        Log.d("AddressInputScreen", "No address provided")
                    }
                },
                enabled = selectedPlace != null || addressText.isNotEmpty(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White,
                    disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                shape = RoundedCornerShape(12.dp),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 8.dp,
                    pressedElevation = 12.dp
                )
            ) {
                androidx.compose.material3.Text(
                    text = when {
                        selectedPlace != null -> "Search Candidates"
                        addressText.isNotEmpty() -> "Search Candidates (Manual)"
                        else -> "Enter Address First"
                    },
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
            
            // Debug button to test Civic API with a known address
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    Log.d("AddressInputScreen", "Debug button clicked - testing Civic API with test address")
                    onNavigateToMatches("1600 Pennsylvania Avenue NW, Washington, DC 20500")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                androidx.compose.material3.Text(
                    text = "Debug: Test Civic API",
                    fontWeight = FontWeight.Medium,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
            
            // Manual address input button (fallback for API issues)
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    Log.d("AddressInputScreen", "Manual address button clicked")
                    if (addressText.isNotEmpty()) {
                        onNavigateToMatches(addressText)
                    }
                },
                enabled = addressText.isNotEmpty(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.tertiary,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                androidx.compose.material3.Text(
                    text = "Use Manual Address (if API fails)",
                    fontWeight = FontWeight.Medium,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
            
            // API Diagnostic button (for troubleshooting)
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    Log.d("AddressInputScreen", "API Diagnostic button clicked")
                    onNavigateToDiagnostics()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                androidx.compose.material3.Text(
                    text = "API Diagnostics (for troubleshooting)",
                    fontWeight = FontWeight.Medium,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
    }
}
