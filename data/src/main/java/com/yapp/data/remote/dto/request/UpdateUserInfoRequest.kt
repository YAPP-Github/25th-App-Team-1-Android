package com.yapp.data.remote.dto.request

import com.yapp.domain.model.EditUser
import kotlinx.serialization.Serializable

@Serializable
data class UpdateUserInfoRequest(
    val name: String,
    val calendarType: String,
    val birthDate: String,
    val birthTime: String,
    val gender: String,
) {
    companion object {
        fun EditUser.toUpdateRequest(): UpdateUserInfoRequest {
            return UpdateUserInfoRequest(
                name = this.name,
                calendarType = when (this.calendarType) {
                    "양력" -> "SOLAR"
                    "음력" -> "LUNAR"
                    else -> "UNKNOWN"
                },
                birthDate = this.birthDate,
                birthTime = this.birthTime ?: "00:00:00",
                gender = when (this.gender) {
                    "남성" -> "MALE"
                    "여성" -> "FEMALE"
                    else -> "UNKNOWN"
                },
            )
        }
    }
}
