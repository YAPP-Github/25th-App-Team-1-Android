package com.yapp.data.local.repositoryimpl

import com.yapp.data.local.datasource.AlarmLocalDataSource
import com.yapp.data.local.toEntity
import com.yapp.domain.model.Alarm
import com.yapp.domain.repository.AlarmRepository
import javax.inject.Inject

class AlarmRepositoryImpl @Inject constructor(
    private val alarmLocalDataSource: AlarmLocalDataSource,
) : AlarmRepository {
    override suspend fun getPagedAlarms(limit: Int, offset: Int): Result<List<Alarm>> = runCatching {
        alarmLocalDataSource.getPagedAlarms(limit, offset)
    }.onFailure {
        return Result.failure(Exception("Failed to get paged alarms"))
    }

    override suspend fun getAlarmCount(): Result<Int> = runCatching {
        alarmLocalDataSource.getAlarmCount()
    }.onFailure {
        return Result.failure(Exception("Failed to get alarm count"))
    }

    override suspend fun insertAlarm(alarm: Alarm): Result<Alarm> = runCatching {
        val alarmId = alarmLocalDataSource.insertAlarm(alarm.toEntity())
        alarmLocalDataSource.getAlarm(alarmId)
            ?: return Result.failure(Exception("Failed to insert alarm"))
    }.onFailure {
        return Result.failure(Exception("Failed to insert alarm"))
    }

    override suspend fun updateAlarm(alarm: Alarm): Result<Alarm> = runCatching {
        val updatedRows = alarmLocalDataSource.updateAlarm(alarm.toEntity())
        if (updatedRows > 0) {
            alarmLocalDataSource.getAlarm(alarm.id)
                ?: throw Exception("Failed to fetch updated alarm")
        } else {
            throw Exception("No rows updated")
        }
    }

    override suspend fun getAlarm(id: Long): Result<Alarm> = runCatching {
        alarmLocalDataSource.getAlarm(id)
            ?: return Result.failure(Exception("Failed to get alarm"))
    }.onFailure {
        return Result.failure(Exception("Failed to get alarm"))
    }

    override suspend fun deleteAlarm(id: Long): Result<Unit> = runCatching {
        val deletedRows = alarmLocalDataSource.deleteAlarm(id)
        if (deletedRows > 0) {
            Unit
        } else {
            throw Exception("No rows deleted")
        }
    }
}
