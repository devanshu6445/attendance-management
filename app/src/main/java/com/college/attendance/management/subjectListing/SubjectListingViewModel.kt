package com.college.attendance.management.subjectListing

import com.college.attendance.management.BaseViewModel
import com.college.attendance.management.UserDetailsDataStore
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

    fun initState() {
        uiFlow.updateState {
            copy(
                isLoading = true
            )
        }

        Firebase.firestore.collection("subjects")
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

    suspend fun getCurrentUserRole() = userDetailsDataStore.value.firstOrNull()?.role
}