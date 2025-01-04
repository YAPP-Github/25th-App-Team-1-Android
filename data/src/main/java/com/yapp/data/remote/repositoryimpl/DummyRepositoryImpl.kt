package com.yapp.data.remote.repositoryimpl

import com.yapp.data.remote.datasource.DummyDataSource
import com.yapp.data.remote.dto.request.toData
import com.yapp.data.remote.dto.response.toDomain
import com.yapp.data.remote.utils.ApiError
import com.yapp.data.remote.utils.safeApiCall
import com.yapp.domain.model.Dummy
import com.yapp.domain.repository.DummyRepository
import javax.inject.Inject

class DummyRepositoryImpl @Inject constructor(
    private val dummyDataSource: DummyDataSource,
) : DummyRepository {

    override suspend fun fetchDummy(): Result<Dummy> = safeApiCall {
        dummyDataSource.fetchDummy().data?.toDomain()
            ?: return Result.failure(ApiError("No data found"))
    }

    override suspend fun saveDummy(dummy: Dummy): Result<Unit> = safeApiCall {
        dummyDataSource.saveDummy(dummy.toData()).data
            ?: return Result.failure(ApiError("Save operation failed"))
    }
}
