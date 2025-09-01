package com.votewise

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxSize
import androidx.lifecycle.ViewModelProvider
import coil.compose.rememberAsyncImagePainter
import com.votewise.data.CandidateRepository
import com.votewise.data.api.CivicInfoApiService
import com.votewise.data.api.RetrofitInstance
import com.votewise.ui.viewmodel.HomeViewModel
import com.votewise.ui.theme.VotewiseTheme
import com.votewise.navigation.NavGraph
import com.votewise.ui.viewmodel.AuthViewModel
import androidx.compose.foundation.layout.Box
import com.google.android.libraries.places.api.Places
import com.votewise.app.BuildConfig
import com.votewise.data.UserPreferencesRepository
import com.votewise.ui.viewmodel.HomeViewModelFactory
import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale
import com.votewise.app.R

class MainActivity : ComponentActivity() {

    private val civicInfoApiService: CivicInfoApiService by lazy {
        RetrofitInstance.civicInfoApi
    }

    private val candidateRepository: CandidateRepository by lazy {
        CandidateRepository(civicInfoApiService)
    }

    private val authViewModel: AuthViewModel by viewModels()

    private val homeViewModelFactory: ViewModelProvider.Factory by lazy {
        HomeViewModelFactory(candidateRepository, UserPreferencesRepository(applicationContext), applicationContext)
    }

    private val homeViewModel: HomeViewModel by viewModels { homeViewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize the Google Places SDK
        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, BuildConfig.GOOGLE_CIVIC_API_KEY)
        }

        setContent {
            VotewiseTheme {
                Box(modifier = Modifier.fillMaxSize()) {
                    Image(
                        painter = rememberAsyncImagePainter(R.drawable.votewise_pioneers_words),
                        contentDescription = null, // Or provide a proper content description
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    NavGraph(candidateRepository = candidateRepository)
                }
            }
        }
    }
}
