package com.college.attendance.management

import android.text.TextUtils
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

const val PW_DATE_PATTERN = "yyyy-MM-dd"
const val PW_APP_DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"

fun getCurrentServerDate(): Date {
    val date = Date(System.currentTimeMillis() + 1000)
    return date
}

fun getTodayDate(): String {
    val c = getCurrentServerDate()
    val df = SimpleDateFormat(PW_DATE_PATTERN, Locale.getDefault())
    return df.format(c)
}

fun String.toISTFormat(currentFmt: String, outputFmt: String = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"): String {
    return try {
        val parsedDate = SimpleDateFormat(currentFmt, Locale.getDefault()).parse(this)
        parsedDate?.let {
            val outputFormat = SimpleDateFormat(outputFmt, Locale.getDefault())
            outputFormat.timeZone = TimeZone.getTimeZone("Asia/Kolkata")
            outputFormat.format(it)
        } ?: ""
    } catch (e: ParseException) {
        e.printStackTrace()
        ""
    }
}

fun getIndianTimeZone(serverDate: String?): String {
    if (TextUtils.isEmpty(serverDate)) return ""
    var ourDate = ""
    try {
        val simpleDateFormat =
            SimpleDateFormat(PW_APP_DATE_PATTERN, Locale.getDefault())
        simpleDateFormat.timeZone = TimeZone.getTimeZone("Asia/Kolkata")

        val indianDateFormat =
            SimpleDateFormat(PW_APP_DATE_PATTERN, Locale.getDefault())
        indianDateFormat.timeZone = TimeZone.getDefault()

        val timeStamp = simpleDateFormat.parse(serverDate)
        ourDate = indianDateFormat.format(timeStamp)
    } catch (e: Exception) {
        ourDate = "00-00-0000"
        e.printStackTrace()
    }
    return ourDate
}

fun getDateTimeInMs(serverDate: String?): Long {
    val date: String = getIndianTimeZone(serverDate)

    val originalFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())

    val d: Date?
    try {
        d = originalFormat.parse(date)
        if (d != null) return d.time
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return 0
}