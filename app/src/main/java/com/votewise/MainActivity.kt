package com.votewise

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import com.votewise.app.BuildConfig
import com.votewise.ui.navigation.AppNavHost
import com.votewise.ui.theme.VotewiseTheme

class MainActivity : ComponentActivity() {
    
    private var globalPlacesClient: PlacesClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize the Google Places SDK (New API)
        Log.d("MainActivity", "API Keys Debug:")
        Log.d("MainActivity", "MAPS_API_KEY: ${BuildConfig.MAPS_API_KEY}")
        Log.d("MainActivity", "GOOGLE_CIVIC_API_KEY: ${BuildConfig.GOOGLE_CIVIC_API_KEY}")
        Log.d("MainActivity", "VOTEWISE_FEC_API_KEY: ${BuildConfig.VOTEWISE_FEC_API_KEY}")
        
        if (!Places.isInitialized()) {
            Log.d("MainActivity", "Initializing New Places API with key: ${BuildConfig.MAPS_API_KEY}")
            Log.d("MainActivity", "Note: New Places API requires Places API (New) to be enabled in Google Cloud Console")
            Log.d("MainActivity", "Note: New Places API requires billing to be enabled")
            Places.initialize(applicationContext, BuildConfig.MAPS_API_KEY)
            Log.d("MainActivity", "New Places API initialized successfully")
        } else {
            Log.d("MainActivity", "New Places API already initialized")
        }
        
        // Create a global Places client for proper lifecycle management
        try {
            globalPlacesClient = Places.createClient(this)
            Log.d("MainActivity", "Global Places client created successfully")
        } catch (e: Exception) {
            Log.e("MainActivity", "Error creating global Places client: ${e.message}", e)
        }

        setContent {
            VotewiseTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    AppNavHost(navController = navController)
                }
            }
        }
    }
    
    override fun onDestroy() {
        super.onDestroy()
        
        // PlacesClient lifecycle is managed automatically by the SDK
        // No manual shutdown is required
        Log.d("MainActivity", "MainActivity destroyed - Places client will be cleaned up automatically")
        globalPlacesClient = null
    }
}
