package com.yapp.alarm.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.ComponentActivity
import com.yapp.alarm.AlarmConstants
import com.yapp.datastore.UserPreferences
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@AndroidEntryPoint
class AlarmInteractionActivityReceiver(private val activity: ComponentActivity) : BroadcastReceiver() {

    @Inject
    lateinit var userPreferences: UserPreferences

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("AlarmInteractionActivityReceiver", "알람 수신, AlarmAlertActivity 종료")
        val isSnoozed = intent?.getBooleanExtra(AlarmConstants.EXTRA_IS_SNOOZED, false) ?: false

        if (intent?.action == AlarmConstants.ACTION_ALARM_INTERACTION_ACTIVITY_CLOSE) {
            activity.finish()

            if (!isSnoozed) {
                CoroutineScope(Dispatchers.IO).launch {
                    val fortuneDate = userPreferences.fortuneDateFlow.firstOrNull()
                    val todayDate = LocalDate.now().format(DateTimeFormatter.ISO_DATE)

                    Log.d("AlarmReceiver", "fortuneDate: $fortuneDate, todayDate: $todayDate")

                    if (fortuneDate != todayDate) {
                        context?.let {
                            val missionIntent =
                                Intent(Intent.ACTION_VIEW, Uri.parse("orbitapp://mission")).apply {
                                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                }
                            it.startActivity(missionIntent)
                        }
                    }
                }
            }
        }
    }
}
