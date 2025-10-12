package com.votewise.ui.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.votewise.ui.viewmodel.PlacesViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun AddressAutocompleteTextField(
    onQueryChanged: (String) -> Unit,
    onPlaceSelected: (Place) -> Unit,
    onSearchClick: () -> Unit
) {
    val context = LocalContext.current
    // Use a single Places client instance to avoid channel shutdown issues
    val placesClient = remember {
        try {
            if (!Places.isInitialized()) {
                Places.initialize(context, com.votewise.app.BuildConfig.MAPS_API_KEY)
            }
            Places.createClient(context)
        } catch (e: Exception) {
            Log.e("AddressAutocompleteTextField", "Error creating Places client: ${e.message}", e)
            null
        }
    }
    
    // No disposal needed - SDK manages lifecycle
    DisposableEffect(Unit) {
        onDispose {
            Log.d("AddressAutocompleteTextField", "AddressAutocompleteTextField disposed")
        }
    }
    
    val placesViewModel: PlacesViewModel = viewModel()
    val predictions by placesViewModel.locationPredictions.collectAsState()
    var query by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    val placeFields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG)

    Column {
        TextField(
            value = query,
            onValueChange = {
                query = it
                onQueryChanged(it)
                Log.d("AddressAutocompleteTextField", "Query changed to: $it (New Places API)")
                placesClient?.let { client ->
                    placesViewModel.searchPlaces(it, client)
                } ?: Log.w("AddressAutocompleteTextField", "Places client is null, cannot search places")
            },
            label = { Text("Enter your address") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = {
                onSearchClick()
                keyboardController?.hide()
            }),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White.copy(alpha = 0.95f),
                unfocusedContainerColor = Color.White.copy(alpha = 0.9f),
                focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                unfocusedIndicatorColor = MaterialTheme.colorScheme.outline
            )
        )

        // Dropdown with better visibility
        if (predictions.isNotEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.White.copy(alpha = 0.95f))
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(8.dp)
                    )
            ) {
                predictions.take(5).forEach { prediction ->
                    Text(
                        text = prediction.getFullText(null).toString(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                Log.d("AddressAutocompleteTextField", "Place selected: ${prediction.getFullText(null)} (Legacy Places API)")
                                placesClient?.let { client ->
                                    val request = FetchPlaceRequest.newInstance(prediction.placeId, placeFields)
                                    client.fetchPlace(request)
                                        .addOnSuccessListener { response ->
                                            val place = response.place
                                            Log.d("AddressAutocompleteTextField", "Place fetched successfully: ${place.address} (Legacy Places API)")
                                            onPlaceSelected(place)
                                            query = place.address.orEmpty()
                                            onQueryChanged(query)
                                            // Don't clear predictions immediately, let the parent handle it
                                        }
                                        .addOnFailureListener { exception ->
                                            Log.e("AddressAutocompleteTextField", "Error fetching place (Legacy Places API): ${exception.message}", exception)
                                        }
                                } ?: Log.w("AddressAutocompleteTextField", "Places client is null, cannot fetch place")
                            }
                            .padding(12.dp),
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}