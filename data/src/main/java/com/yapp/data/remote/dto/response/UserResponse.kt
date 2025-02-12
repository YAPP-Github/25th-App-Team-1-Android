package com.yapp.data.remote.dto.response

import com.yapp.domain.model.User
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    @SerialName("id") val userId: Long,
    val name: String,
    val calendarType: String,
    val birthDate: String,
    val birthTime: String?,
    val gender: String,

)

fun UserResponse.toDomain(): User {
    return User(
        id = userId,
        name = name,
        calendarType = when (calendarType) {
            "LUNAR" -> "음력"
            "SOLAR" -> "양력"
            else -> "알 수 없음"
        },
        birthDate = birthDate.toFormattedDate(),
        birthTime = birthTime ?: "시간모름",
        gender = when (gender) {
            "MALE" -> "남"
            "FEMALE" -> "여"
            else -> "알 수 없음"
        },
    )
}

private fun String.toFormattedDate(): String {
    val parts = this.split("-") // "2000-01-01" → ["2000", "01", "01"]
    val year = parts[0] + "년"
    val month = parts[1].toInt().toString() + "월"
    val day = parts[2].toInt().toString() + "일"
    return "$year $month $day"
}
