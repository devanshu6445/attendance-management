package com.college.attendance.management.login

import com.college.attendance.management.BaseViewModel
import com.college.attendance.management.UserDetails
import com.college.attendance.management.UserDetailsDataStore
import com.college.attendance.management.toObject
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() : BaseViewModel<LoginState>() {

    override val initialState: LoginState
        get() = LoginState(isLoading = false)


    override val uiFlow: MutableStateFlow<LoginState> = MutableStateFlow(initialState)


    fun login(email: String, password: String) {
        uiFlow.updateState {
            copy(
                isLoading = true
            )
        }

        FirebaseAuth.getInstance()
            .signInWithEmailAndPassword(
                email, password
            ).addOnSuccessListener {
                if (it.user != null) {
                    loginSuccess()
                }
            }.addOnFailureListener {
                loginFailed(it.message.orEmpty())
            }
    }

    private fun loginSuccess() {
        Firebase.firestore
            .collection("users")
            .document(
                FirebaseAuth.getInstance().currentUser?.uid
                    ?: throw IllegalStateException("User id can't be null")
            )
            .get()
            .addOnSuccessListener {
                val userDetails = it.toObject<UserDetails>()

                userDetailsDataStore.value =
                    flowOf(userDetails)

                uiFlow.updateState {
                    copy(
                        isLoading = false,
                        isLoggedIn = true
                    )
                }
            }
    }

    private fun loginFailed(message: String) {
        uiFlow.updateState {
            copy(
                isLoading = false,
                error = message
            )
        }
    }
}