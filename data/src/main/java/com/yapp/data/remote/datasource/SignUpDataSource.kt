package com.yapp.data.remote.datasource

import com.yapp.data.remote.dto.request.SignUpRequest

interface SignUpDataSource {
    suspend fun postSignUp(request: SignUpRequest): Result<Long>
}
