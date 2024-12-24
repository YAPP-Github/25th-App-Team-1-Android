package com.yapp.domain.usecase

import com.yapp.domain.model.Dummy
import com.yapp.domain.repository.DummyRepository
import javax.inject.Inject

class DummyUseCase @Inject constructor(
    private val dummyRepository: DummyRepository,
) {
    suspend fun fetch(): Result<Dummy> = dummyRepository.fetchDummy()
    suspend fun save(dummy: Dummy): Result<Unit> = dummyRepository.saveDummy(dummy)
}
