package com.college.attendance.management.subjectListing

import kotlinx.serialization.Serializable

@Serializable
data class AttendanceRecord(
    val uid: String,
    val teacherId: String,
    val absentStudents: List<String>,
    val timestamp: Long,
    val subjectCode: String
)
