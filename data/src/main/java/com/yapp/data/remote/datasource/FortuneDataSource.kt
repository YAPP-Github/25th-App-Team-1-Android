package com.yapp.data.remote.datasource

import com.yapp.data.remote.dto.response.FortuneResponse

interface FortuneDataSource {
    suspend fun postFortune(userId: Long): Result<FortuneResponse>
    suspend fun getFortune(fortuneId: Long): Result<FortuneResponse>
}
