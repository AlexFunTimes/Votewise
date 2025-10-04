package com.votewise.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.votewise.data.auth.XAuthManager
import com.votewise.ui.theme.VotewiseTheme
import kotlinx.coroutines.launch
import androidx.lifecycle.viewModelScope

class OAuthCallbackActivity : ComponentActivity() {

    private val xAuthManager: XAuthManager by lazy {
        XAuthManager(this)
    }

    private val viewModel: OAuthCallbackViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            VotewiseTheme {
                OAuthCallbackScreen(
                    onAuthComplete = { success ->
                        if (success) {
                            setResult(RESULT_OK)
                        } else {
                            setResult(RESULT_CANCELED)
                        }
                        finish()
                    }
                )
            }
        }

        // Handle the OAuth callback
        intent?.let { viewModel.handleAuthCallback(it, xAuthManager) }
    }
}

@Composable
fun OAuthCallbackScreen(
    onAuthComplete: (Boolean) -> Unit
) {
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        // Simulate processing time
        kotlinx.coroutines.delay(2000)
        isLoading = false
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (isLoading) {
                CircularProgressIndicator()
                Text("Completing authentication...")
            } else if (errorMessage != null) {
                Text(
                    text = "Authentication failed: $errorMessage",
                    color = MaterialTheme.colorScheme.error
                )
                Button(
                    onClick = { onAuthComplete(false) }
                ) {
                    Text("Try Again")
                }
            } else {
                Text("Authentication successful!")
                Button(
                    onClick = { onAuthComplete(true) }
                ) {
                    Text("Continue")
                }
            }
        }
    }
}

class OAuthCallbackViewModel : androidx.lifecycle.ViewModel() {

    fun handleAuthCallback(intent: Intent, xAuthManager: XAuthManager) {
        viewModelScope.launch {
            xAuthManager.handleAuthCallback(intent)
        }
    }
}