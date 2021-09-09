package com.rowicka.newthings

import android.os.Bundle
import androidx.compose.material.ExperimentalMaterialApi
import com.rowicka.newthings.activityResult.HomeScreenActivity
import com.rowicka.newthings.calendar.CalendarActivity
import com.rowicka.newthings.clippingCanvasObjects.ClippingCanvasObjects
import com.rowicka.newthings.customView.CustomViewActivity
import com.rowicka.newthings.databinding.ActivityMainBinding
import com.rowicka.newthings.drawOnCanvas.DrawCanvasActivity
import com.rowicka.newthings.geofences.GeofencesActivity
import com.rowicka.newthings.motionLayout.MotionLayoutActivity
import com.rowicka.newthings.propertyAnimations.PropertyAnimationsActivity
import com.rowicka.newthings.recyclerViewSection.RecyclerViewSectionActivity
import com.rowicka.newthings.utils.BaseActivity
import com.rowicka.newthings.notifications.MainActivity as NotificationActivity

@ExperimentalMaterialApi
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
            recyclerViewSection.setOnClickListener { navigateToActivity(RecyclerViewSectionActivity::class) }
            navClippingCanvasObjects.setOnClickListener { navigateToActivity(ClippingCanvasObjects::class) }
            navPropertyAnimation.setOnClickListener { navigateToActivity(PropertyAnimationsActivity::class) }
            navMotionLayout.setOnClickListener { navigateToActivity(MotionLayoutActivity::class) }
            navNotification.setOnClickListener { navigateToActivity(NotificationActivity::class) }
            navGeofences.setOnClickListener { navigateToActivity(GeofencesActivity::class) }
            navCalendar.setOnClickListener { navigateToActivity(CalendarActivity::class) }
        }
    }
}