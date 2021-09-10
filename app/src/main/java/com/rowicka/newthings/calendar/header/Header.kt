package com.rowicka.newthings.calendar.header

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rowicka.newthings.calendar.misc.IconButton
import com.rowicka.newthings.calendar.upperFirst
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*

@Composable
fun Header(
    date: LocalDate,
    onPrev: () -> Unit = {},
    onNext: () -> Unit = {},
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 26.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onPrev, icon = Icons.Default.ChevronLeft)
        DisplayMonth(date = date)
        IconButton(onClick = onNext, icon = Icons.Default.ChevronRight)
    }
}

@Composable
fun DisplayMonth(date: LocalDate) {
    val year = date.year.toString()
    val month = date.format(DateTimeFormatter.ofPattern("LLL", Locale.getDefault()))

    Column(
        modifier = Modifier.fillMaxWidth(0.5f),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            style = androidx.compose.ui.text.TextStyle(
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            ),
            text = "${month.upperFirst()} $year"
        )
    }
}

@Composable
private fun DisplaySelectedDate(date: LocalDate) {
    val year = date.year.toString()
    val day = date.format(DateTimeFormatter.ofPattern("dd", Locale.getDefault()))
    val month = date.format(DateTimeFormatter.ofPattern("LLL", Locale.getDefault()))
    val dayOfWeek = date.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())

    Column(
        modifier = Modifier.fillMaxWidth(0.5f),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            style = androidx.compose.ui.text.TextStyle(
                fontSize = 18.sp
            ),
            text = year
        )
        Text(
            style = androidx.compose.ui.text.TextStyle(
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            ),
            text = "$day ${month.upperFirst()}"
        )
        Text(
            style = androidx.compose.ui.text.TextStyle(
                fontSize = 14.sp
            ),
            text = dayOfWeek.upperFirst()
        )
    }
}