package com.college.attendance.management.attendanceViewer

import androidx.compose.runtime.mutableStateListOf
import com.college.attendance.management.BaseViewModel
import com.college.attendance.management.subjectListing.AttendanceRecord
import com.college.attendance.management.toObject
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.kizitonwose.calendar.core.nextMonth
import kotlinx.coroutines.flow.MutableStateFlow
import java.time.Instant
import java.time.YearMonth
import java.time.ZoneId
import java.time.ZoneOffset

class AttendanceViewerViewModel : BaseViewModel<AttendanceViewerState>() {
    override val initialState: AttendanceViewerState =
        AttendanceViewerState(presentList = mutableStateListOf())

    override val uiFlow: MutableStateFlow<AttendanceViewerState> = MutableStateFlow(initialState)


    fun loadAttendance(month: YearMonth) {
        val currentMonthTimeStamp =
            month.atDay(1).atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli()
        val nextMothTimeStamp =
            month.nextMonth.atDay(1).atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli()

        Firebase.firestore
            .collection("attendance")
            .whereGreaterThanOrEqualTo("timestamp", currentMonthTimeStamp)
            .whereLessThan("timestamp", nextMothTimeStamp)
            .get()
            .addOnSuccessListener {
                val attendance = it.map { attendance ->
                    val attendanceRecord = attendance.toObject<AttendanceRecord>()

                    Instant.ofEpochMilli(attendanceRecord.timestamp)
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate() to !attendanceRecord.absentStudents.contains(FirebaseAuth.getInstance().currentUser?.uid)
                }
                uiFlow.value.presentList.addAll(
                    attendance
                )
            }
    }
}