package com.yapp.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPreferences @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) {
    private object Keys {
        val USER_ID = longPreferencesKey("user_id")
        val USER_NAME = stringPreferencesKey("user_name")
        val ONBOARDING_COMPLETED = booleanPreferencesKey("onboarding_completed")
        val FORTUNE_ID = longPreferencesKey("fortune_id")
        val FORTUNE_DATE = stringPreferencesKey("fortune_date")
        val FORTUNE_IMAGE_ID = intPreferencesKey("fortune_image_id")
        val FORTUNE_SCORE = intPreferencesKey("fortune_score")
        val FORTUNE_CHECKED = booleanPreferencesKey("fortune_checked")
    }

    val userIdFlow: Flow<Long?> = dataStore.data
        .catch { emit(emptyPreferences()) }
        .map { it[Keys.USER_ID] }
        .distinctUntilChanged()

    val userNameFlow: Flow<String?> = dataStore.data
        .catch { emit(emptyPreferences()) }
        .map { it[Keys.USER_NAME] }
        .distinctUntilChanged()

    val onboardingCompletedFlow: Flow<Boolean> = dataStore.data
        .catch { emit(emptyPreferences()) }
        .map { it[Keys.ONBOARDING_COMPLETED] ?: false }
        .distinctUntilChanged()

    val fortuneIdFlow: Flow<Long?> = dataStore.data
        .catch { emit(emptyPreferences()) }
        .map { it[Keys.FORTUNE_ID] }
        .distinctUntilChanged()

    val fortuneDateFlow: Flow<String?> = dataStore.data
        .catch { emit(emptyPreferences()) }
        .map { it[Keys.FORTUNE_DATE] }
        .distinctUntilChanged()

    val fortuneImageIdFlow: Flow<Int?> = dataStore.data
        .catch { emit(emptyPreferences()) }
        .map { it[Keys.FORTUNE_IMAGE_ID] }
        .distinctUntilChanged()

    val fortuneScoreFlow: Flow<Int?> = dataStore.data
        .catch { emit(emptyPreferences()) }
        .map { it[Keys.FORTUNE_SCORE] }
        .distinctUntilChanged()

    val hasNewFortuneFlow: Flow<Boolean> = dataStore.data
        .catch { emit(emptyPreferences()) }
        .map { preferences ->
            val savedDate = preferences[Keys.FORTUNE_DATE]
            val isChecked = preferences[Keys.FORTUNE_CHECKED] ?: true
            val todayDate = LocalDate.now().format(DateTimeFormatter.ISO_DATE)
            savedDate == todayDate && !isChecked
        }
        .distinctUntilChanged()

    suspend fun saveUserId(userId: Long) {
        dataStore.edit { preferences ->
            preferences[Keys.USER_ID] = userId
        }
    }

    suspend fun saveUserName(userName: String) {
        dataStore.edit { preferences ->
            preferences[Keys.USER_NAME] = userName
        }
    }

    suspend fun saveFortuneId(fortuneId: Long) {
        val currentDate = LocalDate.now().format(DateTimeFormatter.ISO_DATE)
        dataStore.edit { preferences ->
            preferences[Keys.FORTUNE_ID] = fortuneId
            preferences[Keys.FORTUNE_DATE] = currentDate
            preferences[Keys.FORTUNE_CHECKED] = false
        }
    }

    suspend fun markFortuneAsChecked() {
        dataStore.edit { preferences ->
            preferences[Keys.FORTUNE_CHECKED] = true
        }
    }

    suspend fun saveFortuneImageId(imageResId: Int) {
        dataStore.edit { preferences ->
            preferences[Keys.FORTUNE_IMAGE_ID] = imageResId
        }
    }

    suspend fun saveFortuneScore(score: Int) {
        dataStore.edit { preferences ->
            preferences[Keys.FORTUNE_SCORE] = score
        }
    }

    suspend fun setOnboardingCompleted() {
        dataStore.edit { preferences ->
            preferences[Keys.ONBOARDING_COMPLETED] = true
        }
    }

    suspend fun clearUserData() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    suspend fun clearFortuneId() {
        dataStore.edit { preferences ->
            preferences.remove(Keys.FORTUNE_ID)
            preferences.remove(Keys.FORTUNE_DATE)
            preferences.remove(Keys.FORTUNE_IMAGE_ID)
            preferences.remove(Keys.FORTUNE_SCORE)
            preferences.remove(Keys.FORTUNE_CHECKED)
        }
    }
}
