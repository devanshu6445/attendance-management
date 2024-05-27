package com.college.attendance.management.subjectListing

import com.college.attendance.management.BaseViewModel
import com.college.attendance.management.UserDetailsDataStore
import com.college.attendance.management.UserRole
import com.college.attendance.management.toObjectList
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

@HiltViewModel
class SubjectListingViewModel @Inject constructor() : BaseViewModel<SubjectListingState>() {
    override val initialState: SubjectListingState
        get() = SubjectListingState()

    override val uiFlow: MutableStateFlow<SubjectListingState> = MutableStateFlow(initialState)

    suspend fun initState() {
        val userDetails = userDetailsDataStore.value.firstOrNull()
        uiFlow.updateState {
            copy(
                isLoading = true
            )
        }

        when (userDetails?.role) {
            UserRole.Student -> {
                Firebase.firestore.collection("subjects")
                    .whereIn("code", userDetails.enrolledIn ?: emptyList())
                    .get()
                    .addOnSuccessListener {
                        val subjects = it.toObjectList<Subject>()

                        uiFlow.updateState {
                            copy(
                                isLoading = false,
                                subjects = subjects
                            )
                        }
                    }
            }

            UserRole.Teacher -> {
                Firebase.firestore.collection("subjects")
                    .whereEqualTo("teacherId", userDetails.uid)
                    .get()
                    .addOnSuccessListener {
                        val subjects = it.toObjectList<Subject>()

                        uiFlow.updateState {
                            copy(
                                isLoading = false,
                                subjects = subjects
                            )
                        }
                    }
            }

            else -> {
                uiFlow.updateState {
                    copy(
                        isLoading = false
                    )
                }
            }
        }
    }

    suspend fun getCurrentUserRole() = userDetailsDataStore.value.firstOrNull()?.role
}