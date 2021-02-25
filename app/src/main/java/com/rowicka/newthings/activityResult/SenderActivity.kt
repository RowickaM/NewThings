package com.rowicka.newthings.activityResult

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.rowicka.newthings.databinding.ActivitySenderBinding

class SenderActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySenderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySenderBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}