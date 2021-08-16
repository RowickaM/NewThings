package com.rowicka.newthings.clippingCanvasObjects

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class ClippingCanvasObjects : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(ClippingCanvasObjectsView(this))

    }
}