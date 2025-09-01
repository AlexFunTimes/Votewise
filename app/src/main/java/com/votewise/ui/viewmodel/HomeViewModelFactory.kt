package com.votewise.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import com.votewise.data.CandidateRepository
import com.votewise.data.UserPreferencesRepository

class HomeViewModelFactory(
    private val repository: CandidateRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(repository, userPreferencesRepository, context, Places.createClient(context)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}