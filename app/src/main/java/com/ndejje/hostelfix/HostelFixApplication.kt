package com.ndejje.hostelfix

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.ndejje.hostelfix.data.local.DatabaseProvider
import com.ndejje.hostelfix.data.repository.ComplaintRepository
import com.ndejje.hostelfix.data.repository.UserPreferencesRepository
import com.ndejje.hostelfix.data.repository.UserRepository

private const val SETTINGS_PREFERENCE_NAME = "settings_preferences"
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = SETTINGS_PREFERENCE_NAME
)

class HostelFixApplication : Application() {
    val database by lazy { DatabaseProvider.getDatabase(this) }
    val userRepository by lazy { UserRepository(database.userDao()) }
    val complaintRepository by lazy { ComplaintRepository(database.complaintDao()) }
    val userPreferencesRepository by lazy { UserPreferencesRepository(dataStore) }
}