package com.yapp.domain.repository

interface SignUpRepository {
    suspend fun postSignUp(
        name: String,
        calendarType: String,
        birthDate: String,
        birthTime: String,
        gender: String,
    ): Result<Long>
}
