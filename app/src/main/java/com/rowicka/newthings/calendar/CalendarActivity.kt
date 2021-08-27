package com.rowicka.newthings.calendar

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.Month
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*
import androidx.compose.ui.text.TextStyle as TextStyleCompose

class CalendarActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {
            val (dateTime, setDate) = remember { mutableStateOf(LocalDate.now()) }

            Column {
                Spacer(modifier = Modifier.height(150.dp))
                CalendarHeader(
                    date = dateTime,
                    onPrev = { setDate(dateTime.minusMonths(1)) },
                    onNext = { setDate(dateTime.plusMonths(1)) }
                )

                Spacer(modifier = Modifier.height(16.dp))

                CalendarMonth(date = dateTime, onClickItem = setDate)

                if (dateTime != LocalDate.now()) {
                    Button(onClick = { setDate(LocalDate.now()) }) {
                        Text(text = "Wybierz dzisiaj")
                    }
                }
            }
        }
    }

    @Composable
    fun CalendarHeader(
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
            DisplaySelectedDate(date = date)
            IconButton(onClick = onNext, icon = Icons.Default.ChevronRight)
        }
    }

    @Composable
    fun DisplaySelectedDate(date: LocalDate) {
        val year = date.year.toString()
        val day = date.format(DateTimeFormatter.ofPattern("dd", Locale.getDefault()))
        val month = date.format(DateTimeFormatter.ofPattern("LLL", Locale.getDefault()))
        val dayOfWeek = date.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())

        Column(
            modifier = Modifier.fillMaxWidth(0.5f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                style = TextStyleCompose(
                    fontSize = 18.sp
                ),
                text = year
            )
            Text(
                style = TextStyleCompose(
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                ),
                text = "$day ${month.upperFirst()}"
            )
            Text(
                style = TextStyleCompose(
                    fontSize = 14.sp
                ),
                text = dayOfWeek.upperFirst()
            )
        }
    }

    @Preview(showBackground = true)
    @Composable
    private fun CalendarHeaderPrev() {
        CalendarHeader(
            LocalDate.now()
        )
    }

    @Composable
    fun CalendarMonth(
        date: LocalDate,
        onClickItem: (LocalDate) -> Unit
    ) {
        CalendarMonth(
            date = date,
            onClickItem = onClickItem,
            columnWidth = 35.dp,
            dayHeight = 35.dp
        )
    }

    @Preview(showBackground = true)
    @Composable
    private fun CalendarMonthPrev() {
        val firstDayOfJuly = LocalDate.of(2021, Month.JULY, 1)
        val firstDayOfAugust = LocalDate.of(2021, Month.AUGUST, 1)

        Column {
            Text(
                text = firstDayOfJuly.toString(),
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentWidth()
                    .padding(top = 16.dp, bottom = 8.dp)
            )
            CalendarMonth(firstDayOfJuly, {})

            Text(
                text = firstDayOfAugust.toString(),
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentWidth()
                    .padding(top = 16.dp, bottom = 8.dp)
            )
            CalendarMonth(firstDayOfAugust, {})
        }
    }
}