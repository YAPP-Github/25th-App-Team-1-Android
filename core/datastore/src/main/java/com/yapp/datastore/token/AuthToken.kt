package com.yapp.datastore.token

import kotlinx.serialization.Serializable

@Serializable
data class AuthToken(
    val accessToken: String = "",
    val refreshToken: String = "",
    val isSigned: Boolean = false,
)
