package com.college.attendance.management

import android.text.TextUtils
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.kizitonwose.calendar.compose.CalendarState
import com.kizitonwose.calendar.core.CalendarMonth
import kotlinx.coroutines.flow.filter
import kotlinx.serialization.json.Json
import org.json.JSONObject
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.Month
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Date
import java.util.Locale
import java.util.TimeZone

val JsonDecoder = Json {
    ignoreUnknownKeys = true
}

// not so optimized
inline fun <reified T> DocumentSnapshot.toObject(): T = JsonDecoder.decodeFromString<T>(
    JSONObject(
        data ?: throw IllegalStateException("Data can't be null")
    ).toString()
)

// not so optimized
inline fun <reified T> QuerySnapshot.toObjectList(): List<T> {
    return map {
        JsonDecoder.decodeFromString<T>(
            JSONObject(
                it.data
            ).toString()
        )
    }
}

fun NavController.safeNavigate(
    currentDestination: String,
    targetDestination: String,
    builder: NavOptionsBuilder.() -> Unit = {}
) {
    if (currentDestination != targetDestination && this.currentDestination?.route != targetDestination && this.currentDestination?.route == currentDestination)
        navigate(targetDestination, builder)
}

/**
 * Returns the first visible month in a paged calendar **after** scrolling stops.
 *
 * @see [rememberFirstCompletelyVisibleMonth]
 * @see [rememberFirstMostVisibleMonth]
 */
@Composable
fun rememberFirstVisibleMonthAfterScroll(state: CalendarState): CalendarMonth {
    val visibleMonth = remember(state) { mutableStateOf(state.firstVisibleMonth) }
    LaunchedEffect(state) {
        snapshotFlow { state.isScrollInProgress }
            .filter { scrolling -> !scrolling }
            .collect { visibleMonth.value = state.firstVisibleMonth }
    }
    return visibleMonth.value
}

fun YearMonth.displayText(short: Boolean = false): String {
    return "${this.month.displayText(short = short)} ${this.year}"
}

fun Month.displayText(short: Boolean = true): String {
    val style = if (short) TextStyle.SHORT else TextStyle.FULL
    return getDisplayName(style, Locale.ENGLISH)
}

fun DayOfWeek.displayText(uppercase: Boolean = false): String {
    return getDisplayName(TextStyle.SHORT, Locale.ENGLISH).let { value ->
        if (uppercase) value.uppercase(Locale.ENGLISH) else value
    }
}