package com.yapp.domain.repository

import com.yapp.domain.model.EditUser
import com.yapp.domain.model.User

interface UserInfoRepository {
    suspend fun getUserInfo(userId: Long): Result<User>
    suspend fun updateUserInfo(userId: Long, editUser: EditUser): Result<Unit>
}
