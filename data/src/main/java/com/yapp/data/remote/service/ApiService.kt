package com.yapp.data.remote.service

import com.yapp.data.remote.dto.request.SignUpRequest
import com.yapp.data.remote.dto.response.UserResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @POST("/api/v1/users")
    suspend fun postSignUp(@Body request: SignUpRequest): Response<ResponseBody>

    @GET("/api/v1/users/{userId}")
    suspend fun getUserInfo(@Path("userId") userId: Long): UserResponse
}
