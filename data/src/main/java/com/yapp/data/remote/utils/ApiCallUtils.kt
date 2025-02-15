package com.yapp.data.remote.utils

import retrofit2.HttpException
import java.io.IOException

internal inline fun <T> safeApiCall(action: () -> T): Result<T> =
    runCatching(action).recoverCatching { exception ->
        when (exception) {
            is HttpException -> throw mapHttpException(exception)
            is IOException -> throw ApiError("네트워크 오류 발생")
            else -> throw exception
        }
    }

private fun mapHttpException(exception: HttpException): ApiError {
    return when (exception.code()) {
        400 -> ApiError("잘못된 요청")
        401 -> ApiError("인증이 필요합니다")
        403 -> ApiError("권한이 없습니다")
        404 -> ApiError("요청한 리소스를 찾을 수 없습니다")
        in 500..599 -> ApiError("서버 오류")
        else -> ApiError("알 수 없는 서버 오류가 발생했습니다.")
    }
}
