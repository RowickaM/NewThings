package com.rowicka.newthings.calendar

import java.util.*

fun String.upperFirst(): String {
    return this.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
}