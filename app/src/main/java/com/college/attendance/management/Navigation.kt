package com.college.attendance.management

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.college.attendance.management.attendanceViewer.AttendanceViewerUI
import com.college.attendance.management.login.Login
import com.college.attendance.management.subjectListing.AttendanceSubmission
import com.college.attendance.management.subjectListing.SubjectListing

const val Login = "login"
const val SubjectList = "subject_list"
const val AttendanceSubmission = "attendance?subject={subject}"
const val AttendanceViewer = "attendance-viewer?subject={subject}"


@Composable
fun AttendanceNavHost(userDetailsDataStore: UserDetailsDataStore) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Login) {

        composable(Login) {

            val userDetails =
                userDetailsDataStore.value.collectAsState(initial = UserDetails.default()).value

            if (userDetails != UserDetails.default()) {
                navController.safeNavigate(
                    currentDestination = Login,
                    targetDestination = when (userDetails.role) {
                        UserRole.Student -> SubjectList
                        UserRole.Teacher -> SubjectList
                        UserRole.None -> return@composable
                    },
                ) {
                    popUpTo(Login) {
                        inclusive = true
                    }
                }

                return@composable
            }

            Login(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                navController = navController
            )
        }

        composable(SubjectList) {
            SubjectListing(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                navController = navController
            )
        }

        composable(
            route = AttendanceSubmission,
            arguments = listOf(
                navArgument("subject") {
                    type = NavType.StringType
                }
            )
        ) {
            AttendanceSubmission(
                modifier = Modifier.fillMaxSize(),
                subject = JsonDecoder.decodeFromString(it.arguments?.getString("subject") ?: "")
            )
        }

        composable(
            route = AttendanceViewer,
            arguments = listOf(
                navArgument("subject") {
                    type = NavType.StringType
                }
            )
        ) {
            AttendanceViewerUI(
                modifier = Modifier.fillMaxSize(),
                subject = JsonDecoder.decodeFromString(it.arguments?.getString("subject") ?: "")
            )
        }
    }
}