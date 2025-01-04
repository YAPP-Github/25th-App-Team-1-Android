package com.yapp.data.remote.dto.response

import com.yapp.domain.model.Dummy
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseDummyDto(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
)
fun ResponseDummyDto.toDomain() = Dummy(
    id = id,
    name = name,
)
