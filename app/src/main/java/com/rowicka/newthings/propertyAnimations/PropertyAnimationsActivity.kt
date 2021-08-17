package com.rowicka.newthings.propertyAnimations

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.rowicka.newthings.databinding.ActivityPropertyAnimationsBinding

class PropertyAnimationsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPropertyAnimationsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPropertyAnimationsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initListeners()
    }

    private fun initListeners() {
        binding.apply {
            rotateButton.setOnClickListener { rotater() }
            translateButton.setOnClickListener { translater() }
            scaleButton.setOnClickListener { scaler() }
            fadeButton.setOnClickListener { fader() }
            colorizeButton.setOnClickListener { colorizer() }
            showerButton.setOnClickListener { shower() }
        }
    }

    private fun rotater() {
        val animator = ObjectAnimator.ofFloat(
            binding.star,
            View.ROTATION,
            -360f,
            0f
        ).apply {
            duration = 1000
            disableButton(binding.rotateButton)
        }

        animator.start()
    }

    private fun translater() {
        val animator = ObjectAnimator.ofFloat(
            binding.star,
            View.TRANSLATION_X,
            200f
        ).apply {
            duration = 1000
            repeatCount = 1
            repeatMode = ObjectAnimator.REVERSE
            disableButton(binding.translateButton)
        }

        animator.start()
    }

    private fun scaler() {
        val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 2f)
        val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 2f)

        val animator = ObjectAnimator.ofPropertyValuesHolder(binding.star, scaleX, scaleY)
            .apply {
                duration = 1000
                repeatCount = 1
                repeatMode = ObjectAnimator.REVERSE
                disableButton(binding.scaleButton)
            }

        animator.start()
    }

    private fun fader() {
        val animator = ObjectAnimator.ofFloat(binding.star, View.ALPHA, 0f)
            .apply {
                duration = 1000
                repeatCount = 1
                repeatMode = ObjectAnimator.REVERSE
                disableButton(binding.fadeButton)
            }

        animator.start()
    }

    private fun colorizer() {
        val animator = ObjectAnimator.ofArgb(
            binding.star.parent as FrameLayout,
            "backgroundColor",
            Color.BLACK,
            Color.RED
        ).apply {
            duration = 1000
            repeatCount = 1
            repeatMode = ObjectAnimator.REVERSE
            disableButton(binding.colorizeButton)
        }

        animator.start()
    }

    private fun shower() {
    }

    private fun Animator.disableButton(button: Button) {
        this.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator?) {
                button.isEnabled = false
            }

            override fun onAnimationEnd(animation: Animator?) {
                button.isEnabled = true
            }
        })
    }
}