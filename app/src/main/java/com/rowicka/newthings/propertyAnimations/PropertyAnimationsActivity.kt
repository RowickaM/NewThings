package com.rowicka.newthings.propertyAnimations

import android.animation.*
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.Button
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import com.rowicka.newthings.R
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
        val container = binding.star.parent as ViewGroup
        var startWidth = binding.star.width.toFloat()
        var startHeight = binding.star.height.toFloat()

        val newsStar = createStar()

        startWidth *= newsStar.scaleX
        startHeight *= newsStar.scaleY

        newsStar.translationX = Math.random().toFloat() * container.width - startWidth / 2

        newsStar.fallStarAnimation(
            container,
            startHeight,
            container.height.toFloat()
        )
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

    private fun createStar(): AppCompatImageView {
        val container = binding.star.parent as ViewGroup

        val star = AppCompatImageView(this)
        star.setImageResource(R.drawable.ic_star)
        star.layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        )

        container.addView(star)

        star.scaleX = Math.random().toFloat() * 1.5f + .1f
        star.scaleY = star.scaleX

        return star
    }

    private fun AppCompatImageView.fallStarAnimation(
        container: ViewGroup,
        startHeight: Float,
        containerHeight: Float,
    ) {
        val fallAnimation = ObjectAnimator.ofFloat(
            this,
            View.TRANSLATION_Y,
            -startHeight,
            containerHeight + startHeight
        ).apply {
            interpolator = AccelerateInterpolator(1f)
        }

        val rotation = ObjectAnimator.ofFloat(
            this,
            View.ROTATION,
            (Math.random() * 1080).toFloat()
        ).apply {
            interpolator = LinearInterpolator()
        }

        val set = AnimatorSet().apply {
            playTogether(fallAnimation, rotation)
            duration = (Math.random() * 1500 + 500).toLong()
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    container.removeView(this@fallStarAnimation)
                }
            })
        }
        set.start()
    }
}