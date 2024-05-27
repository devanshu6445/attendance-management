package com.college.attendance.management.attendanceViewer

import androidx.compose.runtime.snapshots.SnapshotStateList
import java.time.LocalDate

data class AttendanceViewerState(
    val presentList: SnapshotStateList<Pair<LocalDate,Boolean>>
)
