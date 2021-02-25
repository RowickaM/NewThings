package com.rowicka.newthings.utils

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.TextView
import android.widget.Toast

fun View.show(){
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