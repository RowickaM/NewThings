package com.rowicka.newthings.calendar

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.rowicka.newthings.calendar.calendar.Calendar
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
    val (displayMonth, setDisplayMonth) = remember { mutableStateOf(LocalDate.now()) }

    val listOfEvents = listOf<LocalDate>(
        LocalDate.now(),
        LocalDate.now().minusDays(10),
        LocalDate.now().minusMonths(1),
        LocalDate.now().plusDays(12),
        LocalDate.now().minusDays(9).plusMonths(1),
    )

    Column {
        Header(
            date = displayMonth,
            onPrev = { setDisplayMonth(displayMonth.minusMonths(1)) },
            onNext = { setDisplayMonth(displayMonth.plusMonths(1)) },
        )
        SwipeToChange(
            onLeft = { setDisplayMonth(displayMonth.minusMonths(1)) },
            onRight = { setDisplayMonth(displayMonth.plusMonths(1)) }
        ) {
            Calendar(
                currentDate = dateTime,
                displayedDate = displayMonth,
                onClickItem = setDate,
                onClickWithEvent = {},
                listOfEvents = listOfEvents,
            )
        }
    }
}