package com.rowicka.newthings.activityResult.ownContract

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.rowicka.newthings.activityResult.GET_MESSAGE
import com.rowicka.newthings.databinding.ActivitySenderBinding

class SenderActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySenderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySenderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initListeners()
    }

    private fun onSendClick() {
        val message = binding.senderInput.text.toString()

        val intent = Intent()
        if (message.isNotEmpty()) {
            intent.putExtra(GET_MESSAGE, message)
        }

        setResult(Activity.RESULT_OK, intent)

        finish()
    }

    private fun initListeners() {
        binding.senderButton.setOnClickListener { onSendClick() }
    }
}