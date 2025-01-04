package com.yapp.data.remote.dto.request

import com.yapp.domain.model.Dummy
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestDummyDto(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
)
fun Dummy.toData() = RequestDummyDto(
    id = id,
    name = name,
)
