package com.live.streetview.navigation.earthmap.compass.map.fuelmanager.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import java.util.Calendar

class AlarmHelper(private val context: Context) {

    fun setAlarm(
        alarmId: Int,
        title: String,
        message: String,
        triggerAtMillis: Long,
        afterMonths : Int? = null,
        recurring: Boolean
    ) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val alarmIntent = Intent(context, AlarmReceiver::class.java).apply {
            action = "ALARM_ACTION"
            putExtra("ALARM_ID", alarmId)
            putExtra("ALARM_TITLE", title)
            putExtra("ALARM_MESSAGE", message)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            alarmId,
            alarmIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        if (recurring && afterMonths != null) {
            // Set a repeating alarm
                alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    triggerAtMillis,
                    AlarmManager.INTERVAL_DAY * 30 * afterMonths, // Repeat After Specific Months
                    pendingIntent
                )
        } else {
            // Set a one-time alarm
            alarmManager.set(
                AlarmManager.RTC_WAKEUP,
                triggerAtMillis,
                pendingIntent
            )
        }
    }

    fun cancelAlarm(alarmId: Int) {
        val alarmIntent = Intent(context, AlarmReceiver::class.java).apply {
            action = "ALARM_ACTION"
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            alarmId,
            alarmIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)
    }

    companion object {
        // Utility function to calculate trigger time based on selected date and time
        fun calculateTriggerTime(calendar: Calendar): Long {
            return calendar.timeInMillis - SystemClock.elapsedRealtime()
        }
    }




}
