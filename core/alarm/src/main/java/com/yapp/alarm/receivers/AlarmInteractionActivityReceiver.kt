package com.yapp.alarm.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.ComponentActivity
import com.yapp.alarm.AlarmConstants

class AlarmInteractionActivityReceiver(private val activity: ComponentActivity) : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("AlarmInteractionActivityReceiver", "알람 수신, AlarmAlertActivity 종료")
        if (intent?.action == AlarmConstants.ACTION_ALARM_INTERACTION_ACTIVITY_CLOSE) {
            activity.finish()
            context?.let {
                val missionIntent = Intent(Intent.ACTION_VIEW, Uri.parse("orbitapp://mission"))
                it.startActivity(missionIntent)
            }
        }
    }
}
