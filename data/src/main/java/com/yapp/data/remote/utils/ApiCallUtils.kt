package com.yapp.data.remote.utils

import retrofit2.HttpException

internal inline fun <T> safeApiCall(action: () -> T): Result<T> =
    runCatching(action).recoverCatching { exception ->
        when (exception) {
            is HttpException -> throw mapHttpException(exception)
            else -> throw exception
        }
    }

private fun mapHttpException(exception: HttpException): ApiError {
    return when (exception.code()) {
        in 300..399 -> ApiError("Redirect Error")
        400 -> ApiError("Bad Request")
        401 -> ApiError("Unauthorized")
        402 -> ApiError("Payment Required")
        403 -> ApiError("Forbidden")
        404 -> ApiError("Not Found")
        else -> ApiError("Server Error")
    }
}
