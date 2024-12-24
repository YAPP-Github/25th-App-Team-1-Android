package com.yapp.network.authenticator

import android.content.Context
import com.jakewharton.processphoenix.ProcessPhoenix
import com.yapp.datastore.token.TokenDataStore
import com.yapp.network.TokenRefreshService
import com.yapp.network.model.ResponseAuthRefreshDto
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject

class OrbitAuthenticator @Inject constructor(
    private val dataStore: TokenDataStore,
    private val tokenRefreshService: TokenRefreshService,
    @ApplicationContext private val context: Context,
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        if (response.code == CODE_TOKEN_EXPIRED) {
            return handleTokenExpiration(response)
        }
        return null
    }

    private fun handleTokenExpiration(response: Response): Request? {
        val newTokens = refreshTokens()
        return newTokens?.let {
            response.request.newBuilder()
                .header("Authorization", "Bearer ${it.accessToken}")
                .build()
        }
    }

    private fun refreshTokens(): ResponseAuthRefreshDto? {
        return runCatching {
            runBlocking {
                val refreshToken = dataStore.token.first().refreshToken
                tokenRefreshService.postAuthRefresh(refreshToken).data
            }
        }.onSuccess { newToken ->
            runBlocking {
                newToken?.let {
                    dataStore.setAccessToken(it.accessToken)
                }
            }
        }.onFailure {
            handleTokenRefreshFailure()
        }.getOrNull()
    }

    private fun handleTokenRefreshFailure() {
        runBlocking { dataStore.setAutoLogin(false) }
        ProcessPhoenix.triggerRebirth(context)
    }

    companion object {
        const val CODE_TOKEN_EXPIRED = 401
    }
}
