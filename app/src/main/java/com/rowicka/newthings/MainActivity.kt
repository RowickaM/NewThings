package com.rowicka.newthings

import android.os.Bundle
import com.rowicka.newthings.activityResult.HomeScreenActivity
import com.rowicka.newthings.clippingCanvasObjects.ClippingCanvasObjects
import com.rowicka.newthings.customView.CustomViewActivity
import com.rowicka.newthings.databinding.ActivityMainBinding
import com.rowicka.newthings.drawOnCanvas.DrawCanvasActivity
import com.rowicka.newthings.motionLayout.MotionLayoutActivity
import com.rowicka.newthings.notifications.MainActivity as NotificationActivity
import com.rowicka.newthings.propertyAnimations.PropertyAnimationsActivity
import com.rowicka.newthings.utils.BaseActivity

class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initListeners()
    }

    private fun initListeners(){
        binding.apply {
            navActivityResult.setOnClickListener { navigateToActivity(HomeScreenActivity::class) }
            navCustomView.setOnClickListener { navigateToActivity(CustomViewActivity::class) }
            navDrawOnCanvas.setOnClickListener { navigateToActivity(DrawCanvasActivity::class) }
            navClippingCanvasObjects.setOnClickListener { navigateToActivity(ClippingCanvasObjects::class) }
            navPropertyAnimation.setOnClickListener { navigateToActivity(PropertyAnimationsActivity::class) }
            navMotionLayout.setOnClickListener { navigateToActivity(MotionLayoutActivity::class) }
            navNotification.setOnClickListener { navigateToActivity(NotificationActivity::class) }
        }
    }
}