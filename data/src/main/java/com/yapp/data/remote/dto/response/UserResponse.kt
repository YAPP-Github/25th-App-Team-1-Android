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
        birthDate = formatFullBirthDate(calendarType, birthDate), // ✅ 변환된 값으로 수정
        birthTime = birthTime ?: "시간모름",
        gender = when (gender) {
            "MALE" -> "남성"
            "FEMALE" -> "여성"
            else -> "알 수 없음"
        },
    )
}

/**
 * ✅ `SOLAR 2000-01-01` → `양력 2000년 1월 1일` 변환 함수
 */
private fun formatFullBirthDate(calendarType: String, birthDate: String): String {
    val type = when (calendarType) {
        "SOLAR" -> "양력"
        "LUNAR" -> "음력"
        else -> "알 수 없음"
    }
    val (year, month, day) = birthDate.split("-").map { it.toInt() }
    return "$type ${year}년 ${month}월 ${day}일"
}
