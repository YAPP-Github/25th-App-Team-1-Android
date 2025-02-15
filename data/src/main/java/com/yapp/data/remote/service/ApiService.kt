package com.yapp.data.remote.service

import com.yapp.data.remote.dto.request.SignUpRequest
import com.yapp.data.remote.dto.request.UpdateUserInfoRequest
import com.yapp.data.remote.dto.response.FortuneResponse
import com.yapp.data.remote.dto.response.UserResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @POST("/api/v1/users")
    suspend fun postSignUp(@Body request: SignUpRequest): Response<ResponseBody>

    @GET("/api/v1/users/{userId}")
    suspend fun getUserInfo(@Path("userId") userId: Long): UserResponse

    @PUT("/api/v1/users/{userId}")
    suspend fun updateUserInfo(
        @Path("userId") userId: Long,
        @Body request: UpdateUserInfoRequest,
    ): Response<Unit>

    @POST("/api/v1/fortunes")
    suspend fun postFortune(@Query("userId") userId: Long): FortuneResponse

    @GET("/api/v1/fortunes/{fortuneId}")
    suspend fun getFortune(@Path("fortuneId") fortuneId: Long): FortuneResponse
}
