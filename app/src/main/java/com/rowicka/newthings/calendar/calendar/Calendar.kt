package com.rowicka.newthings.calendar.calendar

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.rowicka.newthings.calendar.month.DisplayMonth
import java.time.DayOfWeek
import java.time.LocalDate

const val AMOUNT_DAYS_IN_WEEK = 7

@Composable
fun Calendar(
    currentDate: LocalDate,
    displayedDate: LocalDate,
    onClickItem: (LocalDate) -> Unit,
    selectBackground: Color = Color(191, 212, 230, 255),
    selectRoundCorner: Dp = 8.dp,
    columnWidth: Dp = 35.dp,
    dayHeight: Dp = 35.dp,
    colorDayInMonth: Color = Color.Black,
    colorDayOutMonth: Color = Color.LightGray,
    dayStart: DayOfWeek = DayOfWeek.MONDAY,
) {

    val firstDayOfMonth = currentDate.withDayOfMonth(1)
    val offset = firstDayOfMonth.dayOfWeek.value - dayStart.value


    Column {
        DayIndicator(
            columnWidth = columnWidth,
            dayStart = dayStart
        )

        Spacer(modifier = Modifier.height(8.dp))

        DisplayMonth(
            currentDate = currentDate,
            date = displayedDate,
            onClickItem = onClickItem,
            selectBackground = selectBackground,
            selectRoundCorner = selectRoundCorner,
            columnWidth = columnWidth,
            dayHeight = dayHeight,
            colorDayInMonth = colorDayInMonth,
            colorDayOutMonth = colorDayOutMonth,
            offset = offset,
        )
    }
}