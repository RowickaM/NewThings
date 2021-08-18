package com.rowicka.newthings.motionLayout.examples

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import com.google.android.material.appbar.AppBarLayout
import com.rowicka.newthings.R

class ExampleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_example)

        coordinateMotion()
    }

    private fun coordinateMotion() {
        val appBarLayout = findViewById<AppBarLayout>(R.id.appbar_layout)
        val motionLayout = findViewById<MotionLayout>(R.id.motion_layout)

        val listener = AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            val seekPosition = -verticalOffset / appBarLayout.totalScrollRange.toFloat()
            motionLayout.progress = seekPosition
        }

        appBarLayout.addOnOffsetChangedListener(listener)
    }
}