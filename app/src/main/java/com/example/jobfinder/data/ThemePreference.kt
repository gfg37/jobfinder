package com.example.jobfinder.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val THEME_PREF = "theme_pref"
val Context.themeDataStore by preferencesDataStore(name = THEME_PREF)

object ThemeKeys {
    val IS_DARK_MODE = booleanPreferencesKey("is_dark_mode")
}

class ThemePreference(private val context: Context) {
    val isDarkModeFlow: Flow<Boolean> = context.themeDataStore.data
        .map { prefs -> prefs[ThemeKeys.IS_DARK_MODE] ?: false }

    suspend fun setDarkMode(enabled: Boolean) {
        context.themeDataStore.edit { prefs ->
            prefs[ThemeKeys.IS_DARK_MODE] = enabled
        }
    }
}
