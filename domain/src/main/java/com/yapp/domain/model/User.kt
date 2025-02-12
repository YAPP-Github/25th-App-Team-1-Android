package com.yapp.domain.model

data class User(
    val id: Long,
    val name: String,
    val calendarType: String,
    val birthDate: String,
    val birthTime: String?,
    val gender: String,
)
