package com.yapp.data.remote.service

import com.yapp.data.remote.dto.request.RequestDummyDto
import com.yapp.data.remote.dto.response.ResponseDummyDto
import com.yapp.network.model.BaseResponse

interface DummyService {
    suspend fun fetchDummy(): BaseResponse<ResponseDummyDto>
    suspend fun saveDummy(requestDummyDto: RequestDummyDto): BaseResponse<Unit>
}
