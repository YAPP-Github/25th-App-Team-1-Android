package com.yapp.network.authenticator

import com.yapp.datastore.token.TokenDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject

class AuthenticationIntercept @Inject constructor(
    private val datastore: TokenDataStore,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val authRequest = originalRequest.addAuthorizationHeader()
        return chain.proceed(authRequest)
    }

    private fun Request.addAuthorizationHeader(): Request {
        val accessToken = runBlocking { datastore.token.first().accessToken }
        return this.newBuilder()
            .addHeader("Authorization", "Bearer $accessToken")
            .build()
    }
}
