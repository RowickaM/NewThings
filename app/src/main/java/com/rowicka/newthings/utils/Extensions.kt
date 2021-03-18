package com.rowicka.newthings.utils

import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Color
import android.os.Build
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.rowicka.newthings.contacts.model.Event

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.hide() {
    this.visibility = View.GONE
}

fun TextView.error(message: String) {
    this.setTextColor(Color.RED)
    this.text = message
}

fun TextView.normal(message: String) {
    this.setTextColor(Color.BLACK)
    this.text = message
}

fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.toast(message: Int) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun ComponentActivity.checkPermission(
    permission: String,
    onGranted: () -> Unit = {},
    onDenied: () -> Unit = {},
    onNeverAskAgain: () -> Unit = {},
) {
    val permissionRequest = activityResultRegistry.register(
        "REQUEST PERMISSION $permission",
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            onGranted()
        } else {
            onDenied()
        }
    }

    val permissionState = ContextCompat.checkSelfPermission(this, permission)
    val showRationale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        this.shouldShowRequestPermissionRationale(permission)
    } else {
        true
    }

    when {
        permissionState == PackageManager.PERMISSION_GRANTED -> onGranted.invoke()
        !showRationale -> onNeverAskAgain.invoke()
        else -> permissionRequest.launch(permission)
    }
}

fun Int.toPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()

fun Int.toDp(): Int = (this / Resources.getSystem().displayMetrics.density).toInt()

inline fun <T : Any, L : LiveData<Event<T>>> LifecycleOwner.observeEvent(liveData: L, crossinline body: (T?) -> Unit) {
    liveData.observe(this, Observer { body(it.getContentIfNotHandled()) })
}