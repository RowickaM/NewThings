package com.rowicka.newthings.utils

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import kotlin.reflect.KClass

open class BaseActivity : AppCompatActivity() {
    protected fun navigateToActivity(activityClass: KClass<*>, vararg flags: Int) {
        val intent = Intent(this, activityClass.java)
        for (flag in flags) {
            intent.addFlags(flag)
        }
        startActivity(intent)
    }
}