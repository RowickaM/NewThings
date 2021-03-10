package com.rowicka.newthings.jobScheduler

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.rowicka.newthings.R
import com.rowicka.newthings.databinding.ActivityAlarmWithJobBinding

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

    private fun startAlarm(){
        toggleButtonEnable()
    }

    private fun stopAlarm(){
        toggleButtonEnable()
    }

    private fun prepareButtons() {
        binding.apply {
            alarmStart.isEnabled = true
            alarmStop.isEnabled = false
        }
    }

    private fun toggleButtonEnable(){
        binding.apply {
            val alarmStartButtonEnabled = alarmStart.isEnabled
            alarmStart.isEnabled = !alarmStartButtonEnabled
            alarmStop.isEnabled = alarmStartButtonEnabled
        }
    }
}