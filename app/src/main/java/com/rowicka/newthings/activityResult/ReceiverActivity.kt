package com.rowicka.newthings.activityResult

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.rowicka.newthings.databinding.ActivityReceiverBinding
import com.rowicka.newthings.utils.invisible

class ReceiverActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReceiverBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityReceiverBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.receiverHeader.invisible()
    }
}