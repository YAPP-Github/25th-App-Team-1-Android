package com.yapp.domain.usecase

import com.yapp.domain.model.Alarm
import com.yapp.domain.repository.AlarmRepository
import javax.inject.Inject

class AlarmUseCase @Inject constructor(
    private val alarmRepository: AlarmRepository
) {
    suspend fun getPagedAlarms(limit: Int, offset: Int): List<Alarm> = alarmRepository.getPagedAlarms(limit, offset)
    suspend fun getAlarmCount(): Int = alarmRepository.getAlarmCount()
    suspend fun insertAlarm(alarm: Alarm): Alarm? = alarmRepository.insertAlarm(alarm)
    suspend fun updateAlarm(alarm: Alarm): Alarm? = alarmRepository.updateAlarm(alarm)
    suspend fun getAlarm(id: Long): Alarm? = alarmRepository.getAlarm(id)
    suspend fun deleteAlarm(id: Long): Long? = alarmRepository.deleteAlarm(id)
}
