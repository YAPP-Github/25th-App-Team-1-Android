package com.yapp.domain.repository

import com.yapp.domain.model.Dummy

interface DummyRepository {
    suspend fun fetchDummy(): Result<Dummy>
    suspend fun saveDummy(dummy: Dummy): Result<Unit>
}
