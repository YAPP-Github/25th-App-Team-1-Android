package com.yapp.domain.repository

import com.yapp.domain.model.User

interface UserInfoRepository {
    suspend fun getUserInfo(userId: Long): Result<User>
}
