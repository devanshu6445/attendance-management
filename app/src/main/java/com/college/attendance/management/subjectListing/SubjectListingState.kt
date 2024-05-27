package com.college.attendance.management.subjectListing

import kotlinx.serialization.Serializable

data class SubjectListingState(
    val subjects: List<Subject> = emptyList(),
    val isLoading: Boolean = false,
)

@Serializable
data class Subject(
    val imageUrl: String? = null,
    val name: String,
    val code: String,
    val teacherId: String
)