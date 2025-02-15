package com.yapp.data.remote.dto.request

import kotlinx.serialization.Serializable

@Serializable
data class SignUpRequest(
    val name: String,
    val calendarType: String,
    val birthDate: String,
    val birthTime: String,
    val gender: String,
) {
    companion object {
        fun fromState(name: String, calendarType: String, birthDate: String, birthTime: String, gender: String): SignUpRequest {
            return SignUpRequest(
                name = name,
                calendarType = when (calendarType) {
                    "양력" -> "SOLAR"
                    "음력" -> "LUNAR"
                    else -> "UNKNOWN"
                },
                birthDate = birthDate,
                birthTime = birthTime,
                gender = when (gender) {
                    "남성" -> "MALE"
                    "여성" -> "FEMALE"
                    else -> "UNKNOWN"
                },
            )
        }
    }
}
