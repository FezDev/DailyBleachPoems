// File: data/SettingsDataStore.kt
package com.example.bleachdailypoem.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Locale

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingsDataStore(context: Context) {

    private val dataStore = context.dataStore

    companion object {
        val NOTIFICATION_HOUR_KEY = intPreferencesKey("notification_hour")
        val NOTIFICATION_MINUTE_KEY = intPreferencesKey("notification_minute")
        val SPOILER_PREFERENCE_KEY = stringPreferencesKey("spoiler_preference")
        val DAILY_POEM_NUMBER_KEY = intPreferencesKey("daily_poem_number")
        val NOTIFICATIONS_ENABLED_KEY = booleanPreferencesKey("notifications_enabled")
        val LANGUAGE_KEY = stringPreferencesKey("language_key")

        // --- NUOVA CHIAVE PER IL TEMA ---
        val THEME_PREFERENCE_KEY = stringPreferencesKey("theme_preference")
    }

    // --- NUOVO FLUSSO DI LETTURA ---
    val themePreference: Flow<String> = dataStore.data.map { preferences ->
        // Di default, l'app seguirà il tema di sistema
        preferences[THEME_PREFERENCE_KEY] ?: "system"
    }

    val language: Flow<String> = dataStore.data.map { preferences ->
        preferences[LANGUAGE_KEY] ?: Locale.getDefault().language
    }

    val notificationsEnabled: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[NOTIFICATIONS_ENABLED_KEY] ?: true
    }

    val spoilerPreference: Flow<String> = dataStore.data.map { preferences ->
        preferences[SPOILER_PREFERENCE_KEY] ?: "option1"
    }

    val notificationTime: Flow<Pair<Int, Int>> = dataStore.data.map { preferences ->
        val hour = preferences[NOTIFICATION_HOUR_KEY] ?: 9
        val minute = preferences[NOTIFICATION_MINUTE_KEY] ?: 0
        Pair(hour, minute)
    }

    val dailyPoemNumber: Flow<Int> = dataStore.data.map { preferences ->
        preferences[DAILY_POEM_NUMBER_KEY] ?: -1
    }

    // --- NUOVA FUNZIONE DI SCRITTURA ---
    suspend fun saveThemePreference(theme: String) {
        dataStore.edit { settings ->
            settings[THEME_PREFERENCE_KEY] = theme
        }
    }

    suspend fun saveLanguage(languageCode: String) {
        dataStore.edit { settings ->
            settings[LANGUAGE_KEY] = languageCode
        }
    }

    suspend fun saveNotificationsEnabled(enabled: Boolean) {
        dataStore.edit { settings ->
            settings[NOTIFICATIONS_ENABLED_KEY] = enabled
        }
    }

    suspend fun saveSpoilerPreference(preference: String) {
        dataStore.edit { settings ->
            settings[SPOILER_PREFERENCE_KEY] = preference
        }
    }

    suspend fun saveNotificationTime(hour: Int, minute: Int) {
        dataStore.edit { settings ->
            settings[NOTIFICATION_HOUR_KEY] = hour
            settings[NOTIFICATION_MINUTE_KEY] = minute
        }
    }

    suspend fun saveDailyPoemNumber(poemNumber: Int) {
        dataStore.edit { settings ->
            settings[DAILY_POEM_NUMBER_KEY] = poemNumber
        }
    }
}
