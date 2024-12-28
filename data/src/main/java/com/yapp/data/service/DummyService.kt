package com.yapp.data.service

import com.yapp.data.dto.request.RequestDummyDto
import com.yapp.data.dto.response.ResponseDummyDto
import com.yapp.network.model.BaseResponse

interface DummyService {
    suspend fun fetchDummy(): BaseResponse<ResponseDummyDto>
    suspend fun saveDummy(requestDummyDto: RequestDummyDto): BaseResponse<Unit>
}
