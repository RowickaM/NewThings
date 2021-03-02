package com.rowicka.newthings.activityResult.simple

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.rowicka.newthings.R
import com.rowicka.newthings.activityResult.GET_MESSAGE
import com.rowicka.newthings.databinding.ActivityReceiverBinding
import com.rowicka.newthings.utils.error
import com.rowicka.newthings.utils.invisible
import com.rowicka.newthings.utils.normal
import com.rowicka.newthings.utils.show

class ReceiverActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReceiverBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityReceiverBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.receiverHeader.invisible()

        initListeners()
    }

    private val messageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                it.data?.getStringExtra(GET_MESSAGE)?.let { msg -> getMessage(msg) }
                    ?: getMessageFailure()
                return@registerForActivityResult
            }

            getMessageFailure()
        }

    private fun getMessage(message: String) {
        binding.apply {
            receiverHeader.show()
            receiverMessage.normal(message)
        }
    }

    private fun getMessageFailure() {
        binding.receiverMessage.error(getString(R.string.activity_result_error_message))
    }


    private fun goToMessageSender() {
        val senderIntent = Intent(this, SenderActivity::class.java)

        messageResult.launch(senderIntent)
    }

    private fun initListeners() {
        binding.apply {
            receiverGetMessageButton.setOnClickListener { goToMessageSender() }
        }
    }
}