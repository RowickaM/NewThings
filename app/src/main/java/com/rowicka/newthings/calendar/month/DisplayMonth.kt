package com.rowicka.newthings.calendar.month

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.rowicka.newthings.calendar.calendar.AMOUNT_DAYS_IN_WEEK
import com.rowicka.newthings.calendar.day.DisplayDay
import com.rowicka.newthings.calendar.day.model.Day
import com.rowicka.newthings.calendar.day.model.DayType
import java.time.LocalDate
import kotlin.math.abs

@Composable
fun DisplayMonth(
    currentDate: LocalDate,
    date: LocalDate,
    onClickItem: (LocalDate) -> Unit,
    selectBackground: Color,
    selectRoundCorner: Dp,
    columnWidth: Dp,
    dayHeight: Dp,
    colorDayInMonth: Color,
    colorDayOutMonth: Color,
    offset: Int,
) {
    val prevMonthLength = date.minusMonths(1).month.length(checkLeapYear(date.year))
    val monthLength = date.month.length(checkLeapYear(date.year))

    val totalWeeks = getTotalWeeks(offset, monthLength)

    LazyColumn {
        itemsIndexed(items = (1..totalWeeks).toList()) { index, _ ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                getDaysForRow(
                    week = index,
                    offset = offset,
                    daysOfLastMonth = prevMonthLength,
                    monthLength = monthLength
                ).forEach { item ->
                    DisplayDay(
                        currentDate = currentDate,
                        date = date,
                        onClickItem = onClickItem,
                        columnWidth = columnWidth,
                        dayHeight = dayHeight,
                        colorDayInMonth = colorDayInMonth,
                        colorDayOutMonth = colorDayOutMonth,
                        item = item,
                        selectBackground = selectBackground,
                        selectRoundCorner = selectRoundCorner
                    )
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

private fun getTotalWeeks(
    offset: Int,
    monthLength: Int,
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