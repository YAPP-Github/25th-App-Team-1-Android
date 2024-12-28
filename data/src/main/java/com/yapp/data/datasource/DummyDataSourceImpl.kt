package com.yapp.data.datasource

import com.yapp.data.dto.request.RequestDummyDto
import com.yapp.data.dto.response.ResponseDummyDto
import com.yapp.data.service.DummyService
import com.yapp.network.model.BaseResponse
import javax.inject.Inject

class DummyDataSourceImpl @Inject constructor(
    private val dummyService: DummyService,
) : DummyDataSource {
    override suspend fun fetchDummy(): BaseResponse<ResponseDummyDto> = dummyService.fetchDummy()
    override suspend fun saveDummy(requestDummyDto: RequestDummyDto): BaseResponse<Unit> = dummyService.saveDummy(requestDummyDto)
}
