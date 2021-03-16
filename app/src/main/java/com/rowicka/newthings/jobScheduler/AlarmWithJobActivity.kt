package com.rowicka.newthings.jobScheduler

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.rowicka.newthings.databinding.ActivityAlarmWithJobBinding
import java.util.*

const val ALARM_REQUEST_CODE = 100

class AlarmWithJobActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAlarmWithJobBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAlarmWithJobBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prepareButtons()
        initListeners()
    }

    private fun initListeners() {
        binding.apply {
            alarmStart.setOnClickListener { startAlarm() }
            alarmStop.setOnClickListener { stopAlarm() }
        }
    }

    private fun startAlarm() {
        toggleButtonEnable()
//        if (TimeHelper.isDay()) {
            startAlarmAfterTime(0)
//        } else {
//            startAlarmAfterTime(5)
//        }
    }

    private fun stopAlarm() {
        toggleButtonEnable()

        stopAllAlarms()
    }

    private fun prepareButtons() {
        binding.apply {
            alarmStart.isEnabled = true
            alarmStop.isEnabled = false
        }
    }

    private fun toggleButtonEnable() {
        binding.apply {
            val alarmStartButtonEnabled = alarmStart.isEnabled
            alarmStart.isEnabled = !alarmStartButtonEnabled
            alarmStop.isEnabled = alarmStartButtonEnabled
        }
    }
}