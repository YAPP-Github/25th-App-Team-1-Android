package com.yapp.data.remote.repositoryimpl

import com.yapp.data.remote.datasource.UserInfoDataSource
import com.yapp.data.remote.dto.request.UpdateUserInfoRequest.Companion.toUpdateRequest
import com.yapp.data.remote.dto.response.toDomain
import com.yapp.domain.model.EditUser
import com.yapp.domain.model.User
import com.yapp.domain.repository.UserInfoRepository
import javax.inject.Inject

class UserInfoRepositoryImpl @Inject constructor(
    private val userInfoDataSource: UserInfoDataSource,
) : UserInfoRepository {
    override suspend fun getUserInfo(userId: Long): Result<User> {
        return userInfoDataSource.getUserInfo(userId)
            .mapCatching { userResponse ->
                userResponse.toDomain()
            }
    }

    override suspend fun updateUserInfo(userId: Long, editUser: EditUser): Result<Unit> {
        val request = editUser.toUpdateRequest()
        return userInfoDataSource.updateUserInfo(userId, request)
            .mapCatching {
                if (it) {
                    Unit
                } else {
                    throw Exception("Failed to update user info")
                }
            }
    }
}
