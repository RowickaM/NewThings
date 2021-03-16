package com.rowicka.newthings.jobScheduler

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import java.time.LocalTime

object TimeHelper {
    val night = LocalTime.of(22, 0, 0)
    val day = LocalTime.of(6, 0, 0)

    fun isDay(): Boolean {
        val current = LocalTime.now()

        return current.isBefore(night) && current.isAfter(day)
    }
}

fun Context.startAlarmAfterTime(minute: Long) {
    val alarmManager = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val intent = Intent(this, AlarmBroadCastReceiver::class.java)
    val pendingIntent = PendingIntent.getBroadcast(
        this,
        ALARM_REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT
    )

    val timeToInvoke = minute * 60 * 1000 + System.currentTimeMillis()

//    if (Build.VERSION.SDK_INT >= 23) {
//        alarmManager.setExactAndAllowWhileIdle(
//            AlarmManager.RTC_WAKEUP,
//            timeToInvoke,
//            pendingIntent
//        )
//    } else {
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeToInvoke, pendingIntent)
//    }
}

fun Context.stopAllAlarms() {
    val intent = Intent(this, AlarmBroadCastReceiver::class.java)
    val pendingIntent = PendingIntent.getBroadcast(
        this,
        ALARM_REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT
    )
    val alarmManager = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    alarmManager.cancel(pendingIntent)
}