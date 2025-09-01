package com.votewise.ui.viewmodel

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
        val token = AutocompleteSessionToken.newInstance()
        val request = FindAutocompletePredictionsRequest.builder()
            .setSessionToken(token)
            .setQuery(query)
            .build()

        placesClient.findAutocompletePredictions(request).addOnSuccessListener { response ->
            _locationPredictions.value = response.autocompletePredictions
        }.addOnFailureListener {
            _locationPredictions.value = emptyList()
        }
    }

    fun fetchPlaceDetails(placeId: String, placesClient: PlacesClient) {
        val placeFields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS_COMPONENTS)
        val request = FetchPlaceRequest.newInstance(placeId, placeFields)
        placesClient.fetchPlace(request).addOnSuccessListener { response ->
            _selectedPlace.value = response.place
        }.addOnFailureListener {
            _selectedPlace.value = null
        }
    }

    fun clearPredictions() {
        _locationPredictions.value = emptyList()
    }
}
