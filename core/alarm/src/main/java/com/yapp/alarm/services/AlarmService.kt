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
            val notificationId = bundle.getLong(AlarmConstants.EXTRA_NOTIFICATION_ID, 0)
            val isSnoozeEnabled = bundle.getBoolean(AlarmConstants.EXTRA_SNOOZE_ENABLED, false)
            val snoozeInterval = bundle.getInt(AlarmConstants.EXTRA_SNOOZE_INTERVAL, 0)
            val snoozeCount = bundle.getInt(AlarmConstants.EXTRA_SNOOZE_COUNT, 0)
            val isSoundEnabled = bundle.getBoolean(AlarmConstants.EXTRA_SOUND_ENABLED, false)
            val soundUri = bundle.getString(AlarmConstants.EXTRA_SOUND_URI, "")
            val soundVolume = bundle.getInt(AlarmConstants.EXTRA_SOUND_VOLUME, 0)
            val isVibrationEnabled = bundle.getBoolean(AlarmConstants.EXTRA_VIBRATION_ENABLED, false)

            val isOneTimeAlarm = bundle.getBoolean(AlarmConstants.EXTRA_IS_ONE_TIME_ALARM, false)
            val isDismiss = bundle.getBoolean(AlarmConstants.EXTRA_IS_DISMISS, false)

            when (isDismiss) {
                true -> {
                    stopSelf()
                }

                false -> {
                    startForeground(
                        notificationId.toInt(),
                        createNotification(notificationId, isSnoozeEnabled, snoozeInterval, snoozeCount),
                    )
                    if (isVibrationEnabled) startVibration()
                    if (isSoundEnabled) startSound(soundUri, soundVolume)
                }
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
        alarmId: Long,
        isSnoozeEnabled: Boolean,
        snoozeInterval: Int,
        snoozeCount: Int,
    ): Notification {
        Log.d("AlarmForegroundService", "createNotification()")

        val alarmAlertPendingIntent =
            createAlarmAlertPendingIntent(applicationContext, alarmId, isSnoozeEnabled, snoozeInterval, snoozeCount)
        val alarmDismissPendingIntent =
            createAlarmDismissPendingIntent(applicationContext, pendingIntentId = alarmId)
        return NotificationCompat.Builder(applicationContext, ALARM_NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(core.designsystem.R.mipmap.ic_launcher)
            .setLargeIcon(BitmapFactory.decodeResource(resources, core.designsystem.R.mipmap.ic_launcher))
            .setContentTitle("오르비 알람")
            .setContentText("알람을 해제할 시간이예요!")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setFullScreenIntent(
                alarmAlertPendingIntent,
                true,
            )
            .addAction(
                core.designsystem.R.drawable.ic_cancel,
                "알람 해제",
                alarmDismissPendingIntent,
            )
            .build()
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
