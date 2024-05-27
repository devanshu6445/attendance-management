package com.college.attendance.management.subjectListing

import com.college.attendance.management.UserDetails

data class AttendanceSubmissionState(
    val isLoading: Boolean = false,
    val students: List<UserDetails> = emptyList(),
    val error: String? = null
)