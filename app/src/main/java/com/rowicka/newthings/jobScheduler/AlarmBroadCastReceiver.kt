package com.rowicka.newthings.jobScheduler

import android.app.Application
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log

const val JOB_ID = 123

class AlarmBroadCastReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        context ?: return

        Log.d("MRMRMR", "AlarmBroadCastReceiver.kt: ${intent?.action}")
        
        val jobService = ComponentName(context, LogJobService::class.java)

        val scheduler = context.getSystemService(Application.JOB_SCHEDULER_SERVICE) as JobScheduler
        val job = JobInfo.Builder(JOB_ID, jobService).setOverrideDeadline(0).build()

        scheduler.cancelAll()
        scheduler.schedule(job)

//        if (TimeHelper.isDay()) {
            context.startAlarmAfterTime(1)
//        } else {
//            context.startAlarmAfterTime(5)
//        }
    }
}