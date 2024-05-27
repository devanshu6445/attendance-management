package com.college.attendance.management.subjectListing

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.college.attendance.management.UserDetails
import com.college.attendance.management.UserRole

@Composable
fun AttendanceSubmission(modifier: Modifier = Modifier, subject: Subject) {
    val viewModel = hiltViewModel<AttendanceSubmissionViewModel>()

    val state = viewModel.uiState.collectAsState().value

    LaunchedEffect(key1 = Unit) {
        viewModel.initState(subject.code)
    }
    AttendanceSubmissionUI(
        modifier = modifier,
        students = state.students,
        subject = subject
    ) {
        viewModel.uploadAttendance(absentStudents = it, subjectCode = subject.code)
    }
}

@Composable
fun AttendanceSubmissionUI(
    modifier: Modifier = Modifier,
    subject: Subject,
    students: List<UserDetails>,
    uploadAttendanceRecord: (List<String>) -> Unit
) {
    val absentStudents = remember {
        mutableStateListOf<String>()
    }
    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .shadow(
                    elevation = 8.dp
                )
                .background(color = MaterialTheme.colorScheme.background)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.Start),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Students enrolled in ${subject.name}",
                style = MaterialTheme.typography.titleMedium
            )
        }

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(8.dp)
        ) {
            items(students) {
                Row(
                    modifier = Modifier
                        .background(
                            color = if (absentStudents.contains(it.uid)) {
                                MaterialTheme.colorScheme.errorContainer
                            } else {
                                MaterialTheme.colorScheme.surfaceContainer
                            },
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable {
                            if (absentStudents.contains(it.uid)) {
                                absentStudents.remove(it.uid)
                            } else {
                                absentStudents.add(it.uid)
                            }

                        }
                ) {
                    Text(
                        text = it.userName,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    )
                }
            }
        }

        Button(
            onClick = {
                uploadAttendanceRecord(absentStudents.toList())
            },
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(0.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.onTertiaryContainer
            )
        ) {
            Text(text = "Submit")
        }
    }
}

@Preview
@Composable
private fun PrevAttendance() {
    AttendanceSubmissionUI(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        subject = Subject(name = "Android", code = "Android", teacherId = ""),
        students = listOf(
            UserDetails(userName = "Abhishek", role = UserRole.Student, uid = "1"),
            UserDetails(userName = "Abhishek", role = UserRole.Student, uid = "2"),
            UserDetails(userName = "Abhishek", role = UserRole.Student, uid = "3"),
        )
    ) {}
}