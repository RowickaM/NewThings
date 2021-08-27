package com.rowicka.newthings.calendar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import java.text.DateFormatSymbols
import java.time.DayOfWeek
import java.util.*

@Composable
fun DayIndicator(
    dayStart: DayOfWeek,
    columnWidth: Dp = 35.dp
) {
    var days = DateFormatSymbols(Locale.getDefault()).shortWeekdays.filter { it.isNotEmpty() }

    if (dayStart != DayOfWeek.SUNDAY) {
        val startArray = days.subList(dayStart.value, days.size)
        val endArray = days.subList(0, dayStart.value)

        days = startArray + endArray
    }

    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        items(items = days) { weekday ->
            if (weekday.isNotEmpty()) {
                Text(
                    modifier = Modifier.width(columnWidth),
                    text = weekday
                        .dropLast(if (weekday.length <= 4) 1 else weekday.length - 3)
                        .uppercase(),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}