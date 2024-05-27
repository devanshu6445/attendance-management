package com.college.attendance.management.subjectListing

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.college.attendance.management.FullCircularProgressBar
import com.college.attendance.management.R
import com.college.attendance.management.SubjectList
import com.college.attendance.management.UserRole
import com.college.attendance.management.safeNavigate
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Composable
fun SubjectListing(modifier: Modifier = Modifier, navController: NavController) {
    val viewModel = hiltViewModel<SubjectListingViewModel>()
    val state = viewModel.uiState.collectAsState().value

    val scope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        viewModel.initState()
    }
    SubjectListingUI(modifier = modifier, state = state) {
        scope.launch {
            when (viewModel.getCurrentUserRole()!!) {
                UserRole.Student -> {
                    navController.safeNavigate(
                        currentDestination = SubjectList,
                        targetDestination = "attendance-viewer?subject=${Json.encodeToString(it)}"
                    )
                }

                UserRole.Teacher -> {
                    navController.safeNavigate(
                        currentDestination = SubjectList,
                        targetDestination = "attendance?subject=${Json.encodeToString(it)}"
                    )
                }

                UserRole.None -> {
                    viewModel.logout()
                }
            }

        }
    }
    when {
        state.isLoading -> {
            FullCircularProgressBar()
        }
    }
}

@Composable
fun SubjectListingUI(
    modifier: Modifier = Modifier,
    state: SubjectListingState,
    uploadAttendance: (Subject) -> Unit
) {
    Column(modifier = modifier) {
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
            Text(text = "Your Subjects", style = MaterialTheme.typography.titleMedium)
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(state.subjects) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .shadow(
                            elevation = 10.dp,
                            clip = false
                        )
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.surfaceDim,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .background(color = MaterialTheme.colorScheme.surfaceContainerLow)
                        .clickable(onClick = { uploadAttendance(it) })
                        .padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AsyncImage(
                        model = it.imageUrl,
                        error = painterResource(id = R.drawable.ic_launcher_foreground),
                        contentDescription = null,
                        modifier = Modifier.size(50.dp)
                    )

                    Text(text = it.name, style = MaterialTheme.typography.bodyLarge)

                    Text(text = it.code, style = MaterialTheme.typography.labelMedium)
                }
            }
        }
    }
}