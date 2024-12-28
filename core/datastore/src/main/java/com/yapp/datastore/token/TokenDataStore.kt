package com.yapp.datastore.token

import android.util.Log
import androidx.datastore.core.DataStore
import java.io.IOException
import javax.inject.Inject

class TokenDataStore @Inject constructor(
    private val tokenPreferences: DataStore<AuthToken>,
) {

    val token = tokenPreferences.data

    suspend fun setAuthToken(authToken: AuthToken) {
        updateDataSafely { copy(authToken.accessToken, authToken.refreshToken, authToken.isSigned) }
    }

    suspend fun setAutoLogin(isSigned: Boolean) {
        updateDataSafely { copy(isSigned = isSigned) }
    }

    suspend fun setAccessToken(accessToken: String) {
        updateDataSafely { copy(accessToken = accessToken) }
    }

    suspend fun setRefreshToken(refreshToken: String) {
        updateDataSafely { copy(refreshToken = refreshToken) }
    }

    private suspend fun updateDataSafely(transform: AuthToken.() -> AuthToken) {
        runCatching {
            tokenPreferences.updateData { it.transform() }
        }.onFailure { exception ->
            if (exception is IOException) {
                Log.e("TokenDataStore", "데이터 업데이트 실패: ${exception.message}")
            }
        }
    }
}
