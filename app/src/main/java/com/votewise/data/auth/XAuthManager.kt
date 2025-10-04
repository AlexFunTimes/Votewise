package com.votewise.data.auth

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import com.votewise.data.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class XAuthManager(
    private val context: Context
) {
    private val _authState = MutableStateFlow<AuthState>(AuthState.NotAuthenticated)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()
    
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()
    
    companion object {
        private const val X_CLIENT_ID = "YOUR_X_CLIENT_ID"
        private const val X_REDIRECT_URI = "votewise://oauth/callback"
        private const val X_SCOPE = "tweet.read%20users.read%20offline.access"
        
        private const val X_AUTH_URL = "https://twitter.com/i/oauth2/authorize"
        private const val X_TOKEN_URL = "https://api.twitter.com/2/oauth2/token"
    }
    
    fun startXAuth() {
        val authUrl = buildAuthUrl()
        openAuthUrl(authUrl)
    }
    
    private fun buildAuthUrl(): String {
        val state = generateState()
        val codeChallenge = generateCodeChallenge()
        
        return Uri.parse(X_AUTH_URL)
            .buildUpon()
            .appendQueryParameter("response_type", "code")
            .appendQueryParameter("client_id", X_CLIENT_ID)
            .appendQueryParameter("redirect_uri", X_REDIRECT_URI)
            .appendQueryParameter("scope", X_SCOPE)
            .appendQueryParameter("state", state)
            .appendQueryParameter("code_challenge", codeChallenge)
            .appendQueryParameter("code_challenge_method", "S256")
            .build()
            .toString()
    }
    
    private fun openAuthUrl(url: String) {
        val customTabsIntent = CustomTabsIntent.Builder()
            .setShowTitle(true)
            .build()
        
        customTabsIntent.launchUrl(context, Uri.parse(url))
    }
    
    suspend fun handleAuthCallback(intent: Intent) {
        val uri = intent.data
        if (uri != null && uri.scheme == "votewise") {
            val code = uri.getQueryParameter("code")
            val state = uri.getQueryParameter("state")
            val error = uri.getQueryParameter("error")
            
            if (error != null) {
                _authState.value = AuthState.Error("Authentication failed: $error")
                return
            }
            
            if (code != null && state != null) {
                exchangeCodeForToken(code, state)
            }
        }
    }
    
    private suspend fun exchangeCodeForToken(code: String, state: String) {
        try {
            _authState.value = AuthState.Authenticating
            
            // Exchange authorization code for access token
            val tokenResponse = exchangeCodeForAccessToken(code)
            
            if (tokenResponse != null) {
                // Get user information from X API
                val userInfo = getUserInfoFromX(tokenResponse.accessToken)
                
                if (userInfo != null) {
                    val user = User(
                        id = userInfo.id,
                        email = userInfo.email ?: "",
                        displayName = userInfo.name,
                        profileImageUrl = userInfo.profileImageUrl,
                        twitterId = userInfo.id,
                        twitterUsername = userInfo.username,
                        createdAt = java.util.Date(),
                        lastLoginAt = java.util.Date(),
                        zipCode = null,
                        state = null,
                        city = null
                    )
                    
                    _currentUser.value = user
                    _authState.value = AuthState.Authenticated
                } else {
                    _authState.value = AuthState.Error("Failed to get user information")
                }
            } else {
                _authState.value = AuthState.Error("Failed to exchange code for token")
            }
        } catch (e: Exception) {
            _authState.value = AuthState.Error("Authentication error: ${e.message}")
        }
    }
    
    private suspend fun exchangeCodeForAccessToken(code: String): TokenResponse? {
        // Implement token exchange with X API
        // This would typically use Retrofit to call the token endpoint
        return null // Placeholder
    }
    
    private suspend fun getUserInfoFromX(accessToken: String): XUserInfo? {
        // Implement user info retrieval from X API
        // This would typically use Retrofit to call the user endpoint
        return null // Placeholder
    }
    
    fun logout() {
        _currentUser.value = null
        _authState.value = AuthState.NotAuthenticated
    }
    
    private fun generateState(): String = "state_${System.currentTimeMillis()}_${(1000..9999).random()}"
    private fun generateCodeChallenge(): String = "code_challenge_${System.currentTimeMillis()}"
}

sealed class AuthState {
    object NotAuthenticated : AuthState()
    object Authenticating : AuthState()
    object Authenticated : AuthState()
    data class Error(val message: String) : AuthState()
}

data class TokenResponse(
    val accessToken: String,
    val tokenType: String,
    val expiresIn: Int,
    val refreshToken: String?,
    val scope: String
)

data class XUserInfo(
    val id: String,
    val username: String,
    val name: String,
    val email: String?,
    val profileImageUrl: String?,
    val verified: Boolean
)
