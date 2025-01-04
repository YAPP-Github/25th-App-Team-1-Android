package com.yapp.data.remote.utils

data class ApiError(
    override val message: String,
) : Exception()
