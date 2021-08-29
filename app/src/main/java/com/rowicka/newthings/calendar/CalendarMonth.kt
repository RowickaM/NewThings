package com.rowicka.newthings.calendar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import java.time.DayOfWeek
import java.time.LocalDate
import kotlin.math.abs

const val AMOUNT_DAYS_IN_WEEK = 7

private fun getTotalWeeks(
    offset: Int,
    monthLength: Int
): Int {
    val firstWeekDaysAmount = if (offset > 0) {
        AMOUNT_DAYS_IN_WEEK - offset
    } else {
        0
    }

    val fullWeeksAmount = (monthLength - firstWeekDaysAmount) / AMOUNT_DAYS_IN_WEEK
    val lastDaysCount = (monthLength - firstWeekDaysAmount) % AMOUNT_DAYS_IN_WEEK

    val weekWithPrevMonth = if (offset > 0) 1 else 0
    val weekWithNextMonth = if (lastDaysCount > 0) 1 else 0
    return fullWeeksAmount + weekWithPrevMonth + weekWithNextMonth
}

private fun checkLeapYear(year: Int): Boolean {
    return (year % 4 == 0 && year % 100 != 0) || year % 400 == 0
}

@Composable
fun CalendarMonth(
    date: LocalDate,
    onClickItem: (LocalDate) -> Unit,
    columnWidth: Dp = 35.dp,
    dayHeight: Dp = 35.dp,
    colorDayInMonth: Color = Color.Black,
    colorDayOutMonth: Color = Color.LightGray,
    dayStart: DayOfWeek = DayOfWeek.MONDAY
) {
    val prevMonthLength = date.minusMonths(1).month.length(checkLeapYear(date.year))
    val firstDayOfMonth = date.withDayOfMonth(1)
    val offset = firstDayOfMonth.dayOfWeek.value - dayStart.value
    val monthLength = date.month.length(checkLeapYear(date.year))

    val totalWeeks = getTotalWeeks(offset, monthLength)

    Column {
        DayIndicator(
            columnWidth = columnWidth,
            dayStart = dayStart
        )

        Spacer(modifier = Modifier.height(8.dp))

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

                        DisplayDay(
                            date = date,
                            onClickItem = onClickItem,
                            columnWidth = columnWidth,
                            dayHeight = dayHeight,
                            colorDayInMonth = colorDayInMonth,
                            colorDayOutMonth = colorDayOutMonth,
                            item = item
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DisplayDay(
    modifier: Modifier = Modifier,
    date: LocalDate,
    onClickItem: (LocalDate) -> Unit,
    columnWidth: Dp = 35.dp,
    dayHeight: Dp = 35.dp,
    colorDayInMonth: Color = Color.Black,
    colorDayOutMonth: Color = Color.LightGray,
    item: Day
) {
    val colorText = if (item.type == DayType.IN_MONTH) colorDayInMonth else colorDayOutMonth

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .width(columnWidth)
            .height(dayHeight)
            .clickable {
                onClickDay(
                    item = item,
                    date = date,
                    setDate = onClickItem
                )
            },
    ) {
        Text(
            text = item.value.toString(),
            textAlign = TextAlign.Center,
            color = colorText
        )
    }
}

private fun onClickDay(item: Day, date: LocalDate, setDate: (LocalDate) -> Unit) {
    when (item.type) {
        DayType.IN_MONTH -> setDateInMonth(
            item.value,
            date,
            setDate
        )
        DayType.NEXT_MONTH -> setDateNextMonth(
            item.value,
            date,
            setDate
        )
        DayType.PREV_MONTH -> setDatePrevMonth(
            item.value,
            date,
            setDate
        )
    }
}

private fun setDateInMonth(newDay: Int, date: LocalDate, setDate: (LocalDate) -> Unit) {
    setDate(date.withDayOfMonth(newDay))
}

private fun setDateNextMonth(newDay: Int, date: LocalDate, setDate: (LocalDate) -> Unit) {
    var newDate = date.plusMonths(1)
    newDate = newDate.withDayOfMonth(newDay)

    setDate(newDate)
}

private fun setDatePrevMonth(newDay: Int, date: LocalDate, setDate: (LocalDate) -> Unit) {
    var newDate = date.minusMonths(1)
    newDate = newDate.withDayOfMonth(newDay)

    setDate(newDate)
}

private fun getDaysForRow(
    week: Int,
    offset: Int,
    daysOfLastMonth: Int,
    monthLength: Int,
): List<Day> {
    val list = arrayListOf<Day>()
    val absoluteOffset = abs(offset)

    if (week == 0) {
        for (i in 1..AMOUNT_DAYS_IN_WEEK) {
            if (i > absoluteOffset) {
                list.add(Day(i - absoluteOffset, DayType.IN_MONTH))
            } else {
                list.add(Day(daysOfLastMonth - absoluteOffset + i, DayType.PREV_MONTH))
            }
        }
    } else {
        var positionOnLastDay = 0
        for (i in 1..AMOUNT_DAYS_IN_WEEK) {
            val day = i + week * AMOUNT_DAYS_IN_WEEK - absoluteOffset
            list.add(
                if (day > monthLength) {
                    Day(i - positionOnLastDay, DayType.NEXT_MONTH)
                } else {
                    positionOnLastDay += 1
                    Day(day, DayType.IN_MONTH)
                }
            )
        }
    }
    return list
}

private data class Day(
    val value: Int,
    val type: DayType
)

private enum class DayType {
    IN_MONTH, PREV_MONTH, NEXT_MONTH
}