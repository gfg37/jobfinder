package com.example.jobfinder.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val DATASTORE_NAME = "user_prefs"

val Context.dataStore by preferencesDataStore(name = DATASTORE_NAME)

object PreferenceKeys {
    val USER_ROLE = stringPreferencesKey("user_role")
}

class UserPreferences(private val context: Context) {
    val userRoleFlow: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[PreferenceKeys.USER_ROLE]
        }

    suspend fun saveUserRole(role: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferenceKeys.USER_ROLE] = role
        }
    }
}
