package com.votewise.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "user_preferences")

class UserPreferencesRepository(private val context: Context) {

    companion object {
        val ZIP_CODE = stringPreferencesKey("zip_code")
    }

    val userPreferencesFlow = context.dataStore.data
        .map { preferences ->
            preferences[ZIP_CODE] ?: ""
        }

    suspend fun saveZipCode(zipCode: String) {
        context.dataStore.edit { preferences ->
            preferences[ZIP_CODE] = zipCode
        }
    }
}