package com.rowicka.newthings.calendar

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.rowicka.newthings.calendar.header.Header
import com.rowicka.newthings.calendar.swipeToChange.SwipeToChange
import java.time.LocalDate

@ExperimentalMaterialApi
class CalendarActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CalendarComponent()
        }

    }
}

@ExperimentalMaterialApi
@Composable
fun CalendarComponent() {
    val (dateTime, setDate) = remember { mutableStateOf(LocalDate.now()) }

    Column {
        Header(dateTime)
        SwipeToChange(
            onLeft = { setDate(dateTime.minusMonths(1)) },
            onRight = { setDate(dateTime.plusMonths(1)) }
        ) {
            Calendar(
                date = dateTime,
                onClickItem = setDate
            )
        }
    }
}