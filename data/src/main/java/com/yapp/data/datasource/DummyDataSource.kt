package com.yapp.data.datasource

import com.yapp.data.dto.request.RequestDummyDto
import com.yapp.data.dto.response.ResponseDummyDto
import com.yapp.network.model.BaseResponse

interface DummyDataSource {
    suspend fun fetchDummy(): BaseResponse<ResponseDummyDto>
    suspend fun saveDummy(requestDummyDto: RequestDummyDto): BaseResponse<Unit>
}
