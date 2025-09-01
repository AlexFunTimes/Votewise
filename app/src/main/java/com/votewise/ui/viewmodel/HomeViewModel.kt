package com.votewise.ui.viewmodel

import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.votewise.data.CandidateRepository
import com.votewise.data.UserPreferencesRepository
import com.votewise.data.model.CivicInfoResponse
import com.votewise.data.model.CivicInfoCandidate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import android.location.Geocoder
import android.os.Build
import androidx.annotation.RequiresApi
import com.votewise.data.model.Result
import java.io.IOException

class HomeViewModel(
    private val candidateRepository: CandidateRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val context: Context,
    private val placesClient: PlacesClient
) : ViewModel() {

    private val _candidates = MutableStateFlow<List<CivicInfoCandidate>>(emptyList())
    val candidates: StateFlow<List<CivicInfoCandidate>> = _candidates

    private val _isAddressValid = MutableStateFlow(false)
    val isAddressValid: StateFlow<Boolean> = _isAddressValid

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error
    
    private val _address = MutableStateFlow("")
    val address: StateFlow<String> = _address

    private val _predictions = MutableStateFlow<List<AutocompletePrediction>>(emptyList())
    val predictions: StateFlow<List<AutocompletePrediction>> = _predictions

    fun findCandidates(address: String) {
        updateAddress(address)
        fetchVoterInfo(address)
    }

    fun fetchVoterInfo(address: String) {
        val apiKey = try {
            val appInfo = context.packageManager.getApplicationInfo(context.packageName, PackageManager.GET_META_DATA)
            appInfo.metaData.getString("com.google.android.geo.API_KEY")
        } catch (e: PackageManager.NameNotFoundException) {
            null
        }

        if (apiKey == "YOUR_API_KEY" || apiKey.isNullOrEmpty()) {
            _error.value = "Invalid API Key. Please replace 'YOUR_API_KEY' in AndroidManifest.xml with your actual Google Places API key."
            return
        }
        
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            when (val result = candidateRepository.getVoterInfo(address)) {
                is Result.Success -> {
                    _candidates.value = result.data.contests?.flatMap { it.candidates ?: emptyList() } ?: emptyList()
                    userPreferencesRepository.saveZipCode(address)
                }
                is Result.Error -> {
                    Log.e("HomeViewModel", "Error fetching voter info: ${result.exception.message}")
                    _error.value = "Failed to fetch voter info. Please check your API key and try again."
                }
            }
            _isLoading.value = false
        }
    }
    
    fun updateAddress(address: String) {
        _address.value = address
        viewModelScope.launch {
            userPreferencesRepository.saveZipCode(address)
        }
    }

    fun fetchPredictions(query: String) {
        val request = FindAutocompletePredictionsRequest.builder()
            .setQuery(query)
            .build()
        placesClient.findAutocompletePredictions(request).addOnSuccessListener { response ->
            _predictions.value = response.autocompletePredictions
        }.addOnFailureListener { exception ->
            Log.e("HomeViewModel", "Error fetching predictions: ${exception.message}")
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun verifyAddress(address: String) {
        viewModelScope.launch {
            val geocoder = Geocoder(context)
            try {
                geocoder.getFromLocationName(address, 1) { addresses ->
                    _isAddressValid.value = addresses.isNotEmpty()
                }
            } catch (e: IOException) {
                // Handle the exception, e.g., log it or show an error message.
                _isAddressValid.value = false
            }
        }
    }
    
    fun clearZipCode() {
        viewModelScope.launch {
            userPreferencesRepository.saveZipCode("")
        }
    }
}