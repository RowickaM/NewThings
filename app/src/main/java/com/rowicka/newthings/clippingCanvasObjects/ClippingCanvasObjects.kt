package com.rowicka.newthings.clippingCanvasObjects

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.rowicka.newthings.databinding.ActivityClippingCanvasObjectsBinding

class ClippingCanvasObjects : AppCompatActivity() {

    private lateinit var binding: ActivityClippingCanvasObjectsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityClippingCanvasObjectsBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}