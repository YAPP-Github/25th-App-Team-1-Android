package com.yapp.alarm.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.HandlerThread
import android.os.IBinder
import android.os.Looper
import android.os.Message
import android.os.Process.THREAD_PRIORITY_BACKGROUND
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.util.Log
import androidx.core.app.NotificationCompat
import com.yapp.alarm.AlarmConstants
import com.yapp.alarm.AlarmHelper
import com.yapp.alarm.pendingIntent.interaction.createAlarmAlertPendingIntent
import com.yapp.alarm.pendingIntent.interaction.createAlarmDismissPendingIntent
import com.yapp.alarm.pendingIntent.interaction.createAlarmSnoozePendingIntent
import com.yapp.domain.model.Alarm
import com.yapp.domain.model.AlarmDay
import com.yapp.domain.usecase.AlarmUseCase
import com.yapp.media.sound.SoundPlayer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AlarmService : Service() {

    private var serviceLooper: Looper? = null
    private var serviceHandler: ServiceHandler? = null

    @Inject
    lateinit var alarmUseCase: AlarmUseCase

    @Inject
    lateinit var soundPlayer: SoundPlayer

    private lateinit var vibrator: Vibrator

    @Inject
    lateinit var alarmHelper: AlarmHelper

    private inner class ServiceHandler(looper: Looper) : Handler(looper) {

        override fun handleMessage(message: Message) {
            super.handleMessage(message)

            val bundle = message.data
            val alarm: Alarm? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                bundle?.getParcelable(AlarmConstants.EXTRA_ALARM, Alarm::class.java)
            } else {
                @Suppress("DEPRECATION")
                bundle?.getParcelable(AlarmConstants.EXTRA_ALARM)
            }

            if (alarm == null) {
                Log.e("AlarmService", "Failed to retrieve Alarm object from intent")
                return
            }

            val notificationId = alarm.id
            val isDismiss = bundle.getBoolean(AlarmConstants.EXTRA_IS_DISMISS, false)
            val isOneTimeAlarm = alarm.repeatDays == 0

            Log.d("AlarmService", "AlarmService started for alarm: $alarm")

            if (!isOneTimeAlarm) {
                bundle.getString(AlarmConstants.EXTRA_ALARM_DAY)
                    ?.let { AlarmDay.valueOf(it) }
                    ?.let { alarmDay ->
                        alarmHelper.scheduleWeeklyAlarm(alarm, alarmDay)
                    }
            }

            if (isDismiss) {
                stopSelf()
            } else {
                startForeground(notificationId.toInt(), createNotification(alarm))
                if (alarm.isVibrationEnabled) startVibration()
                if (alarm.isSoundEnabled) startSound(alarm.soundUri, alarm.soundVolume)
            }

            if (isOneTimeAlarm) {
                turnOffAlarm(alarmId = notificationId)
            }
        }
    }

    override fun onCreate() {
        super.onCreate()

        createNotificationChannel()

        setupVibrator()

        HandlerThread("AlarmServiceThread", THREAD_PRIORITY_BACKGROUND).apply {
            start()
            serviceLooper = looper
            serviceHandler = ServiceHandler(looper)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        serviceHandler?.obtainMessage()?.also { message ->
            message.arg1 = startId
            message.data = intent?.extras
            serviceHandler?.sendMessage(message)
        }
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        stopVibration()
        stopSound()
        // remove notification
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    private fun createNotification(
        alarm: Alarm,
    ): Notification {
        Log.d("AlarmForegroundService", "createNotification()")

        val closeIntent = Intent(AlarmConstants.ACTION_ALARM_INTERACTION_ACTIVITY_CLOSE).apply {
            putExtra(AlarmConstants.EXTRA_IS_SNOOZED, true)
        }
        applicationContext.sendBroadcast(closeIntent)

        val alarmAlertPendingIntent =
            createAlarmAlertPendingIntent(applicationContext, alarm)
        val alarmDismissPendingIntent =
            createAlarmDismissPendingIntent(applicationContext, pendingIntentId = alarm.id)

        val snoozePendingIntent = if (alarm.isSnoozeEnabled && alarm.snoozeCount != 0) {
            createAlarmSnoozePendingIntent(
                applicationContext,
                alarm,
            )
        } else {
            null
        }

        val builder = NotificationCompat.Builder(applicationContext, ALARM_NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(core.designsystem.R.mipmap.ic_launcher)
            .setLargeIcon(BitmapFactory.decodeResource(resources, core.designsystem.R.mipmap.ic_launcher))
            .setContentTitle("오르비 알람")
            .setContentText("알람을 해제할 시간이예요!")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setFullScreenIntent(alarmAlertPendingIntent, true)
            .addAction(core.designsystem.R.drawable.ic_cancel, "알람 해제", alarmDismissPendingIntent)

        if (snoozePendingIntent != null) {
            builder.addAction(core.designsystem.R.drawable.ic_cancel, "미루기", snoozePendingIntent)
        }

        return builder.build()
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            ALARM_NOTIFICATION_CHANNEL_ID,
            ALARM_NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH,
        ).apply {
            description = ALARM_NOTIFICATION_CHANNEL_DESCRIPTION
            enableVibration(true)
            setSound(
                RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM),
                null,
            )
        }

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun turnOffAlarm(alarmId: Long) {
        CoroutineScope(Dispatchers.IO).launch {
            alarmUseCase.updateAlarmActive(
                id = alarmId,
                active = false,
            )
        }
    }

    private fun setupVibrator() {
        vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = applicationContext.getSystemService(VibratorManager::class.java)
            vibratorManager?.defaultVibrator ?: applicationContext.getSystemService(Vibrator::class.java)
        } else {
            applicationContext.getSystemService(Vibrator::class.java)
        } ?: throw IllegalStateException("Vibrator service is unavailable")
    }

    private fun startVibration() {
        val pattern: LongArray = longArrayOf(0, 1000, 500)
        val effect = VibrationEffect.createWaveform(pattern, 0)
        vibrator.vibrate(effect)
    }

    private fun stopVibration() {
        vibrator.cancel()
    }

    private fun startSound(soundUri: String, volume: Int) {
        val uri: Uri = if (soundUri.isNotEmpty()) {
            Uri.parse(soundUri)
        } else {
            RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        }

        soundPlayer.initialize(uri)
        soundPlayer.playSound(volume)
    }

    private fun stopSound() {
        soundPlayer.stopSound()
    }

    companion object {
        const val ALARM_NOTIFICATION_CHANNEL_ID = "Orbit_Channel_Id"
        const val ALARM_NOTIFICATION_CHANNEL_NAME = "Orbit"
        const val ALARM_NOTIFICATION_CHANNEL_DESCRIPTION = "To show notification for alarms"
    }
}
