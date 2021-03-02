package com.rowicka.newthings.drawOnCanvas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.rowicka.newthings.R
import com.rowicka.newthings.databinding.ActivityDrawCanvasBinding

class DrawCanvasActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDrawCanvasBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDrawCanvasBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}