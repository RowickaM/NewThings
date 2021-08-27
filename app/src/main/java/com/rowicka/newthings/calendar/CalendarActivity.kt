package com.rowicka.newthings.calendar

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.DateFormatSymbols
import java.time.DayOfWeek
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

                CalendarMonth(date = dateTime)

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
    fun CalendarMonth(date: LocalDate) {
        val days = DateFormatSymbols(Locale.getDefault()).shortWeekdays.filter { it.isNotEmpty() }
        Column {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                items(items = days) { weekday ->
                    if (weekday.isNotEmpty()) {
                        Text(
                            modifier = Modifier.width(35.dp),
                            text = weekday
                                .dropLast(if (weekday.length <= 4) 1 else weekday.length - 3)
                                .uppercase(),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            val prevMonthLength = date.minusMonths(1).month.length(true)

            val firstDayOfMonth = LocalDate.of(date.year, date.month, 1)
            val offset = (DayOfWeek.SUNDAY.value - firstDayOfMonth.dayOfWeek.value)

            val monthLength = date.month.length(true)

            val weeks = (offset + monthLength) / 7
            val lastDaysCount = (monthLength - weeks * 7) + offset

            val totalWeeks = weeks + if (lastDaysCount > 0) 1 else 0
            LazyColumn {
                itemsIndexed(items = (1..totalWeeks).toList()) { index, _ ->
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        items(
                            items = getDaysForRow(
                                week = index,
                                offset = offset,
                                daysOfLastMonth = prevMonthLength,
                                monthLength = monthLength
                            )
                        ) { item ->
                            Text(
                                modifier = Modifier.width(35.dp),
                                text = item.value.toString(),
                                textAlign = TextAlign.Center,
                                color = if (item.type == DayType.IN_MONTH) Color.Black else Color.LightGray
                            )
                        }
                    }
                }
            }
        }
    }

    private fun getDaysForRow(
        week: Int,
        offset: Int,
        daysOfLastMonth: Int,
        monthLength: Int,
    ): List<Day> {
        val list = arrayListOf<Day>()

        if (week == 0) {
            for (i in 1..7) {
                if (i > offset) {
                    list.add(Day(i - offset, DayType.IN_MONTH))
                } else {
                    list.add(Day(daysOfLastMonth - offset + i, DayType.OUT_MONTH))
                }
            }
        } else {
            var positionOnLastDay = 0
            for (i in 1..7) {
                val day = i + week * 7 - offset
                list.add(
                    if (day > monthLength) {
                        Day(i - positionOnLastDay, DayType.OUT_MONTH)
                    } else {
                        positionOnLastDay += 1
                        Day(day, DayType.IN_MONTH)
                    }
                )
            }
        }
        return list
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
            CalendarMonth(firstDayOfJuly)

            Text(
                text = firstDayOfAugust.toString(),
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentWidth()
                    .padding(top = 16.dp, bottom = 8.dp)
            )
            CalendarMonth(firstDayOfAugust)
        }
    }
}

data class Day(
    val value: Int,
    val type: DayType
)

enum class DayType {
    IN_MONTH, OUT_MONTH
}