package com.rowicka.newthings.activityResult.ownContract

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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
        registerForActivityResult(MessageContract()) {
            it.fold(
                { error -> getMessageFailure(error) },
                { msg -> getMessage(msg) }
            )

//            it.onSuccess { msg -> getMessage(msg) }
//            it.onFailure { error -> getMessageFailure(error) }
        }

    private fun getMessage(message: String) {
        binding.apply {
            receiverHeader.show()
            receiverMessage.normal(message)
        }
    }

    private fun getMessageFailure(failure: Failure) {
        val failureMsg = when (failure) {
            Failure.NoPassData -> "Nie przekazano danych"
            Failure.ResultNoOK -> "Aktywnośc została anulowana"
        }
        binding.receiverMessage.error(failureMsg)
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