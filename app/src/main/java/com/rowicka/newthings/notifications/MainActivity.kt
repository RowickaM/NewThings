package com.rowicka.newthings.notifications

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.rowicka.newthings.notifications.ui.EggTimerFragment
import com.rowicka.newthings.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main_egg)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, EggTimerFragment.newInstance())
                .commitNow()
        }
    }
}