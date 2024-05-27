package com.college.attendance.management.attendanceViewer

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.college.attendance.management.R
import com.college.attendance.management.calendar.SimpleCalendarTitle
import com.college.attendance.management.displayText
import com.college.attendance.management.rememberFirstVisibleMonthAfterScroll
import com.college.attendance.management.subjectListing.Subject
import com.college.attendance.management.ui.theme.darkColors
import com.kizitonwose.calendar.compose.ContentHeightMode
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.OutDateStyle
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.nextMonth
import com.kizitonwose.calendar.core.previousMonth
import com.kizitonwose.calendar.core.yearMonth
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate

@Composable
fun AttendanceViewerUI(modifier: Modifier = Modifier, subject: Subject, navController: NavController) {
    val today = remember { LocalDate.now() }
    val currentMonth = remember(today) { today.yearMonth }
    val startMonth = remember { currentMonth.minusMonths(500) }
    val endMonth = remember { currentMonth.plusMonths(500) }

    val viewModel = hiltViewModel<AttendanceViewerViewModel>()
    val state = viewModel.uiState.collectAsState().value

    LaunchedEffect(key1 = Unit) {
        viewModel.loadAttendance(currentMonth)
    }
    val daysOfWeek = remember { daysOfWeek() }

    Column(
        modifier = modifier
            .background(colorResource(id = R.color.example_1_bg_light))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .shadow(
                    elevation = 8.dp
                )
                .background(colorResource(id = R.color.example_1_bg_light))
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.Start),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.baseline_arrow_back_ios_new_24),
                contentDescription = null,
                modifier = Modifier.clickable {
                    navController.popBackStack()
                }
            )
            Text(
                text = "Your Attendance for ${subject.name}",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(start = 16.dp)
            )
        }

        val calendarState = rememberCalendarState(
            startMonth = startMonth,
            endMonth = endMonth,
            firstVisibleMonth = currentMonth,
            firstDayOfWeek = daysOfWeek.first(),
            outDateStyle = OutDateStyle.EndOfGrid,
        )
        val coroutineScope = rememberCoroutineScope()
        val visibleMonth = rememberFirstVisibleMonthAfterScroll(calendarState)
        // Draw light content on dark background.
        CompositionLocalProvider(LocalContentColor provides darkColors().onSurface) {
            SimpleCalendarTitle(
                modifier = Modifier.padding(vertical = 10.dp, horizontal = 8.dp),
                currentMonth = visibleMonth.yearMonth,
                goToPrevious = {
                    coroutineScope.launch {
                        val previousMonth = calendarState.firstVisibleMonth.yearMonth.previousMonth

                        viewModel.loadAttendance(previousMonth)
                        calendarState.animateScrollToMonth(previousMonth)
                    }
                },
                goToNext = {
                    coroutineScope.launch {
                        val nextMonth = calendarState.firstVisibleMonth.yearMonth.nextMonth

                        viewModel.loadAttendance(nextMonth)
                        calendarState.animateScrollToMonth(nextMonth)
                    }
                },
            )
            HorizontalCalendar(
                modifier = Modifier
                    .fillMaxSize()
                    .background(colorResource(id = R.color.example_1_bg)),
                state = calendarState,
                contentHeightMode = ContentHeightMode.Fill,
                dayContent = { day ->
                    Day(
                        day = day,
                        isSelected = state.presentList.firstOrNull { it.first == day.date }?.second == true,
                        isOutOfBound = day.date > today,
                    )
                },
                monthHeader = {
                    MonthHeader(daysOfWeek = daysOfWeek)
                },
            )
        }
    }
}

@Composable
private fun MonthHeader(daysOfWeek: List<DayOfWeek>) {
    Row(
        Modifier
            .fillMaxWidth()
            .testTag("MonthHeader")
            .background(colorResource(id = R.color.example_1_bg_secondary))
            .padding(vertical = 8.dp),
    ) {
        for (dayOfWeek in daysOfWeek) {
            Text(
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                fontSize = 15.sp,
                text = dayOfWeek.displayText(),
            )
        }
    }
}

@Composable
private fun Day(
    day: CalendarDay,
    isSelected: Boolean,
    isOutOfBound: Boolean,
) {
    Box(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .clip(RectangleShape)
            .background(
                color = when {
                    isSelected -> Color(0xFFADCFB7)
                    isOutOfBound -> colorResource(id = R.color.example_1_white_light)
                    else -> MaterialTheme.colorScheme.error
                },
            ),
        contentAlignment = Alignment.Center,
    ) {
        val textColor = when (day.position) {
            // Color.Unspecified will use the default text color from the current theme
            DayPosition.MonthDate -> if (isSelected) colorResource(R.color.example_1_bg) else Color.Unspecified
            DayPosition.InDate, DayPosition.OutDate -> colorResource(R.color.example_1_white_light)
        }
        Text(
            text = day.date.dayOfMonth.toString(),
            color = textColor,
            fontSize = 15.sp,
        )
    }
}