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
    val prevMonthLength = date.minusMonths(1).month.length(true)
    val firstDayOfMonth = date.withDayOfMonth(1)
    val offset = (dayStart.value - firstDayOfMonth.dayOfWeek.value)
    val monthLength = date.month.length(true)
    val weeks = (offset + monthLength) / 7
    val lastDaysCount = (monthLength - weeks * 7) + offset
    val totalWeeks = weeks + if (lastDaysCount > 0) 1 else 0

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

    if (week == 0) {
        for (i in 1..7) {
            if (i > offset) {
                list.add(Day(i - offset, DayType.IN_MONTH))
            } else {
                list.add(Day(daysOfLastMonth - offset + i, DayType.PREV_MONTH))
            }
        }
    } else {
        var positionOnLastDay = 0
        for (i in 1..7) {
            val day = i + week * 7 - offset
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