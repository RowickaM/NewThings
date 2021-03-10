package com.rowicka.newthings.drawOnCanvas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.rowicka.newthings.R
class DrawCanvasActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val myCanvasView = MyCanvasView(this)
        myCanvasView.contentDescription = getString(R.string.canvasContentDescription)

        setContentView(myCanvasView)
    }
}