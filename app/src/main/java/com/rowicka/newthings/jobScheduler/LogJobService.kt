package com.rowicka.newthings.jobScheduler

import android.app.job.JobParameters
import android.app.job.JobService
import android.util.Log
import java.io.IOException
import java.time.LocalDateTime

class LogJobService: JobService() {
    override fun onStartJob(params: JobParameters?): Boolean {
        Log.d("MRMRMR", "LogJobService.kt: ${LocalDateTime.now()}")

        pingGoogle()
        return false
    }

    override fun onStopJob(params: JobParameters?): Boolean = false

    private fun pingGoogle(): Boolean {
        val runtime = Runtime.getRuntime()
        try {
            val mIpAddrProcess = runtime.exec("/system/bin/ping 8.8.8.8 -c 1")
            val mExitValue = mIpAddrProcess.waitFor()
            Log.d("MRMRMR", "LogJobService.kt: mExitValue $mExitValue ")
            return mExitValue == 0
        } catch (ignore: InterruptedException) {
            ignore.printStackTrace()
            println(" Exception:$ignore")
        } catch (e: IOException) {
            e.printStackTrace()
            println(" Exception:$e")
        }
        return false
    }
}