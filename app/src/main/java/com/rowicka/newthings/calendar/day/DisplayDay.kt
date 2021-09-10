package com.rowicka.newthings.calendar.day

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.rowicka.newthings.calendar.day.model.Day
import com.rowicka.newthings.calendar.day.model.DayType
import java.time.LocalDate

@Composable
fun DisplayDay(
    modifier: Modifier = Modifier,
    selectBackground: Color,
    selectRoundCorner: Dp,
    currentDate: LocalDate,
    date: LocalDate,
    onClickItem: (LocalDate) -> Unit,
    columnWidth: Dp = 35.dp,
    dayHeight: Dp = 35.dp,
    colorDayInMonth: Color = Color.Black,
    colorDayOutMonth: Color = Color.LightGray,
    item: Day,
) {
    val colorText = if (item.type == DayType.IN_MONTH) colorDayInMonth else colorDayOutMonth
    val backgroundColor =
        if (item.type == DayType.IN_MONTH && item.value == currentDate.dayOfMonth && currentDate.month == date.month)
            selectBackground
        else
            Color.Transparent

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .width(columnWidth)
            .height(dayHeight)
            .background(color = backgroundColor, shape = RoundedCornerShape(selectRoundCorner))
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
