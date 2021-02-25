package com.rowicka.newthings.activityResult

import android.os.Bundle
import com.rowicka.newthings.databinding.ActivityResultHomeBinding
import com.rowicka.newthings.utils.BaseActivity
import com.rowicka.newthings.activityResult.ownContract.ReceiverActivity as OwnContractReceiver
import com.rowicka.newthings.activityResult.simple.ReceiverActivity as SimpleReceiver

class HomeScreenActivity : BaseActivity() {

    private lateinit var binding: ActivityResultHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityResultHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initListeners()
    }

    private fun initListeners() {
        binding.apply {
            navSimple.setOnClickListener { navigateToActivity(SimpleReceiver::class) }
            navOwnContract.setOnClickListener { navigateToActivity(OwnContractReceiver::class) }
        }
    }
}