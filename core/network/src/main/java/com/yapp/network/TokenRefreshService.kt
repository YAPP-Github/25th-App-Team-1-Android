package com.yapp.network

import com.yapp.network.model.BaseResponse
import com.yapp.network.model.ResponseAuthRefreshDto
import retrofit2.http.Header
import retrofit2.http.POST

interface TokenRefreshService {
    @POST("/$API/$VERSION/$AUTH/$REISSUE")
    suspend fun postAuthRefresh(
        @Header("refreshToken") refreshToken: String,
    ): BaseResponse<ResponseAuthRefreshDto>

    companion object {
        const val API = "api"
        const val VERSION = "v1"
        const val AUTH = "auth"
        const val REISSUE = "reissue"
    }
}
