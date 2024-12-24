package com.yapp.data.utils

data class ApiError(
    override val message: String,
) : Exception()
