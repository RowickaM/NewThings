package com.rowicka.newthings.utils

import android.view.View

fun View.show(){
    this.visibility = View.VISIBLE
}

fun View.invisible(){
    this.visibility = View.INVISIBLE
}

fun View.hide(){
    this.visibility = View.GONE
}