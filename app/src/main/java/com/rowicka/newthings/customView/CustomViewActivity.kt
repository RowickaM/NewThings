package com.rowicka.newthings.customView

import android.os.Bundle
import com.rowicka.newthings.databinding.ActivityCustomViewBinding
import com.rowicka.newthings.utils.BaseActivity

class CustomViewActivity : BaseActivity() {
    private lateinit var binding: ActivityCustomViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCustomViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}