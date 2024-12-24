package com.yapp.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseAuthRefreshDto(
    @SerialName("accessToken")
    val accessToken: String,
)
