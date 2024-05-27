package com.college.attendance.management.subjectListing

import com.college.attendance.management.BaseViewModel
import com.college.attendance.management.PW_APP_DATE_PATTERN
import com.college.attendance.management.getDateTimeInMs
import com.college.attendance.management.getTodayDate
import com.college.attendance.management.toObjectList
import com.college.attendance.management.toUTCFormat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class AttendanceSubmissionViewModel @Inject constructor() :
    BaseViewModel<AttendanceSubmissionState>() {

    override val initialState: AttendanceSubmissionState = AttendanceSubmissionState()

    override val uiFlow: MutableStateFlow<AttendanceSubmissionState> =
        MutableStateFlow(initialState)

    fun initState(subjectCode: String) {
        uiFlow.updateState {
            copy(
                isLoading = true
            )
        }

        Firebase.firestore.collection("users")
            .where(
                Filter.arrayContains(
                    "enrolledIn",
                    subjectCode
                )
            )
            .get()
            .addOnSuccessListener {
                uiFlow.updateState {
                    copy(
                        isLoading = false,
                        students = it.toObjectList()
                    )
                }
            }.addOnFailureListener {
                uiFlow.updateState {
                    copy(
                        isLoading = false,
                        error = it.message
                    )
                }
            }
    }

    fun uploadAttendance(absentStudents: List<String>, subjectCode: String) {
        uiFlow.updateState {
            copy(
                isLoading = true
            )
        }

        val documentId = "${getTodayDate()}T00:00:01.000Z".toUTCFormat(PW_APP_DATE_PATTERN)

        Firebase.firestore
            .collection("attendance")
            .document(documentId)
            .set(
                AttendanceRecord(
                    uid = documentId,
                    teacherId = FirebaseAuth.getInstance().currentUser?.uid ?: "",
                    absentStudents = absentStudents,
                    timestamp = getDateTimeInMs(documentId),
                    subjectCode = subjectCode
                )
            )
            .addOnSuccessListener {
                uiFlow.updateState {
                    copy(
                        isLoading = false,
                        error = null
                    )
                }
            }
            .addOnFailureListener {
                uiFlow.updateState {
                    copy(
                        isLoading = false,
                        error = it.message
                    )
                }
            }
    }
}