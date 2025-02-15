package com.yapp.data.remote.datasource

import com.yapp.data.remote.dto.request.UpdateUserInfoRequest
import com.yapp.data.remote.dto.response.UserResponse

interface UserInfoDataSource {
    suspend fun getUserInfo(userId: Long): Result<UserResponse>
    suspend fun updateUserInfo(userId: Long, updateUserInfoRequest: UpdateUserInfoRequest): Result<Boolean>
}
