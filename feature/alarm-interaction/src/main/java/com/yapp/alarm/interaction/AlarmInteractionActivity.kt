package com.yapp.alarm.interaction

import android.annotation.SuppressLint
import android.app.KeyguardManager
import android.content.IntentFilter
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.yapp.alarm.AlarmConstants
import com.yapp.alarm.receivers.AlarmInteractionActivityReceiver
import com.yapp.common.navigation.destination.AlarmInteractionDestination
import com.yapp.common.navigation.rememberOrbitNavigator
import com.yapp.designsystem.theme.OrbitTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AlarmInteractionActivity : ComponentActivity() {

    private val broadcastReceiver = AlarmInteractionActivityReceiver(this)

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        val notificationId = intent.getLongExtra(AlarmConstants.EXTRA_NOTIFICATION_ID, -1L)
        val snoozeEnabled = intent.getBooleanExtra(AlarmConstants.EXTRA_SNOOZE_ENABLED, false)
        val snoozeInterval = intent.getIntExtra(AlarmConstants.EXTRA_SNOOZE_INTERVAL, 5)
        val snoozeCount = intent.getIntExtra(AlarmConstants.EXTRA_SNOOZE_COUNT, 1)

        Log.d("AlarmInteractionActivity", "notificationId: $notificationId, snoozeEnabled: $snoozeEnabled, snoozeInterval: $snoozeInterval, snoozeCount: $snoozeCount")

        unlockScreen()

        registerAlarmInteractionActivityCloseReceiver()

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                android.graphics.Color.TRANSPARENT,
                android.graphics.Color.TRANSPARENT,
            ),
            navigationBarStyle = SystemBarStyle.light(
                android.graphics.Color.BLACK,
                android.graphics.Color.BLACK,
            ),
        )
        setContent {
            val navigator = rememberOrbitNavigator()

            Surface(
                color = OrbitTheme.colors.gray_900,
                modifier = Modifier
                    .fillMaxSize()
                    .navigationBarsPadding(),
            ) {
                NavHost(
                    navController = navigator.navController,
                    startDestination = AlarmInteractionDestination.Route.route,
                    modifier = Modifier.navigationBarsPadding(),
                ) {
                    alarmInteractionNavGraph(
                        navigator = navigator,
                    )
                }
            }

            LaunchedEffect(Unit) {
                val route =
                    "${AlarmInteractionDestination.AlarmAction.route}?" +
                        "notificationId=$notificationId" +
                        "&snoozeEnabled=$snoozeEnabled" +
                        "&snoozeInterval=$snoozeInterval" +
                        "&snoozeCount=$snoozeCount"

                navigator.navController.navigate(route) {
                    popUpTo(AlarmInteractionDestination.Route.route) { inclusive = true }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterAlarmInteractionActivityCloseReceiver()
    }

    private fun unlockScreen() {
        setShowWhenLocked(true)
        setTurnScreenOn(true)

        val keyguardManager = getSystemService(KeyguardManager::class.java)
        keyguardManager.requestDismissKeyguard(this, null)
    }

    private fun registerAlarmInteractionActivityCloseReceiver() {
        val filter = IntentFilter(AlarmConstants.ACTION_ALARM_INTERACTION_ACTIVITY_CLOSE)
        registerReceiver(broadcastReceiver, filter, RECEIVER_NOT_EXPORTED)
    }

    private fun unregisterAlarmInteractionActivityCloseReceiver() {
        unregisterReceiver(broadcastReceiver)
    }
}
