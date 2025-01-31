package com.yapp.data.local.repositoryimpl

import android.content.Context
import com.yapp.data.local.datasource.AlarmLocalDataSource
import com.yapp.data.local.datasource.SoundPlayer
import com.yapp.data.local.toEntity
import com.yapp.domain.model.Alarm
import com.yapp.domain.model.AlarmSound
import com.yapp.domain.repository.AlarmRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AlarmRepositoryImpl @Inject constructor(
    private val alarmLocalDataSource: AlarmLocalDataSource,
    private val soundPlayer: SoundPlayer,
    @ApplicationContext private val context: Context,
) : AlarmRepository {
    override suspend fun getAlarmSounds(): Result<List<AlarmSound>> = withContext(Dispatchers.IO) {
        runCatching {
            val ringtoneManager = android.media.RingtoneManager(context).apply {
                setType(android.media.RingtoneManager.TYPE_ALARM)
            }

            val cursor = ringtoneManager.cursor
            val sounds = mutableListOf<AlarmSound>()

            cursor.use {
                if (it.moveToFirst()) {
                    do {
                        val title = cursor.getString(android.media.RingtoneManager.TITLE_COLUMN_INDEX)
                        val uri = ringtoneManager.getRingtoneUri(it.position)
                        sounds.add(AlarmSound(title, uri))
                    } while (it.moveToNext())
                }
            }
            sounds
        }
    }

    override fun playAlarmSound(alarmSound: AlarmSound) {
        soundPlayer.playSound(alarmSound.uri)
    }

    override fun stopAlarmSound() {
        soundPlayer.stopSound()
    }

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
