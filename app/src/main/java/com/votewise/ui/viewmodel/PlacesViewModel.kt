package com.votewise.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest

class PlacesViewModel : ViewModel() {

    private val _locationPredictions = MutableStateFlow<List<com.google.android.libraries.places.api.model.AutocompletePrediction>>(emptyList())
    val locationPredictions: StateFlow<List<com.google.android.libraries.places.api.model.AutocompletePrediction>> = _locationPredictions

    private val _selectedPlace = MutableStateFlow<Place?>(null)
    val selectedPlace: StateFlow<Place?> = _selectedPlace

    fun searchPlaces(query: String, placesClient: PlacesClient) {
        if (query.length < 3) {
            _locationPredictions.value = emptyList()
            return
        }
        
        Log.d("PlacesViewModel", "Searching for places with query: $query (New API)")
        val token = AutocompleteSessionToken.newInstance()
        val request = FindAutocompletePredictionsRequest.builder()
            .setSessionToken(token)
            .setQuery(query)
            .build()

        placesClient.findAutocompletePredictions(request).addOnSuccessListener { response ->
            Log.d("PlacesViewModel", "Found ${response.autocompletePredictions.size} predictions (New API)")
            _locationPredictions.value = response.autocompletePredictions
        }.addOnFailureListener { exception ->
            Log.e("PlacesViewModel", "Error searching places (New API): ${exception.message}", exception)
            
            // Provide detailed error diagnostics
            if (exception is com.google.android.gms.common.api.ApiException) {
                when (exception.statusCode) {
                    9011 -> {
                        Log.e("PlacesViewModel", "=== API KEY CONFIGURATION ERROR ===")
                        Log.e("PlacesViewModel", "Status Code 9011: The API key is invalid or not properly configured")
                        Log.e("PlacesViewModel", "Please check:")
                        Log.e("PlacesViewModel", "1. Places API (New) is enabled in Google Cloud Console")
                        Log.e("PlacesViewModel", "2. Billing is enabled for your Google Cloud project")
                        Log.e("PlacesViewModel", "3. API key restrictions match your app's package name and SHA-1")
                        Log.e("PlacesViewModel", "4. API restrictions include 'Places API (New)'")
                        Log.e("PlacesViewModel", "Package name: com.votewise.app")
                    }
                    9012 -> Log.e("PlacesViewModel", "Status Code 9012: API key is restricted and cannot be used from this app")
                    else -> Log.e("PlacesViewModel", "Status Code ${exception.statusCode}: ${exception.statusMessage}")
                }
            }
            
            _locationPredictions.value = emptyList()
        }
    }

    fun fetchPlaceDetails(placeId: String, placesClient: PlacesClient) {
        Log.d("PlacesViewModel", "Fetching place details for ID: $placeId (New API)")
        val placeFields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG)
        val request = FetchPlaceRequest.newInstance(placeId, placeFields)
        placesClient.fetchPlace(request).addOnSuccessListener { response ->
            Log.d("PlacesViewModel", "Place details fetched successfully (New API): ${response.place.address}")
            _selectedPlace.value = response.place
        }.addOnFailureListener { exception ->
            Log.e("PlacesViewModel", "Error fetching place details (New API): ${exception.message}", exception)
            
            // Provide detailed error diagnostics
            if (exception is com.google.android.gms.common.api.ApiException) {
                when (exception.statusCode) {
                    9011 -> {
                        Log.e("PlacesViewModel", "=== API KEY CONFIGURATION ERROR ===")
                        Log.e("PlacesViewModel", "Status Code 9011: The API key is invalid or not properly configured")
                        Log.e("PlacesViewModel", "See searchPlaces() error logs for detailed instructions")
                    }
                    9012 -> Log.e("PlacesViewModel", "Status Code 9012: API key is restricted and cannot be used from this app")
                    else -> Log.e("PlacesViewModel", "Status Code ${exception.statusCode}: ${exception.statusMessage}")
                }
            }
            
            _selectedPlace.value = null
        }
    }

    fun clearPredictions() {
        _locationPredictions.value = emptyList()
    }
}
