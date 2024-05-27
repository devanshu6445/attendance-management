package com.college.attendance.management.login

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.college.attendance.management.FullCircularProgressBar
import com.college.attendance.management.R
import com.college.attendance.management.SubjectList
import com.college.attendance.management.findActivity

@Composable
fun Login(modifier: Modifier = Modifier, navController: NavController) {
    val context = LocalContext.current

    val viewModel = hiltViewModel<LoginViewModel>()
    val state = viewModel.uiState.collectAsState().value
    LoginUI(modifier = modifier, onLogin = viewModel::login)

    LaunchedEffect(state.error) {
        if (state.error != null)
            Toast.makeText(context, state.error, Toast.LENGTH_SHORT).show()
    }

    when {
        state.isLoading -> {
            FullCircularProgressBar()
        }

        state.isLoggedIn -> {
            navController.navigate(SubjectList)
        }
    }
}

@Composable
fun LoginUI(modifier: Modifier = Modifier, onLogin: (String, String) -> Unit) {
    Column(
        modifier = modifier
            .padding(32.dp),
    ) {
        val context = LocalContext.current
        Image(
            imageVector = ImageVector.vectorResource(id = R.drawable.baseline_arrow_back_ios_new_24),
            contentDescription = null,
            modifier = Modifier
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.surfaceDim,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(8.dp)
                .clickable { context.findActivity()?.finish() }
        )


        Text(
            text = "Welcome back! Glad \nto see you, Again!",
            style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(top = 32.dp)
        )
        var email by remember { mutableStateOf("") }

        BasicTextField(
            value = email,
            onValueChange = {
                email = it
            },
            modifier = Modifier
                .height(IntrinsicSize.Max)
                .padding(top = 32.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(
                    color = MaterialTheme.colorScheme.surfaceContainerLow
                )
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.surfaceDim,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(8.dp),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .padding(8.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (email.isEmpty()) {
                        Text(text = "Email", color = MaterialTheme.colorScheme.tertiary)
                    }
                    innerTextField()
                }
            },
            textStyle = LocalTextStyle.current,
        )

        var password by remember { mutableStateOf("") }
        BasicTextField(
            value = password,
            onValueChange = {
                password = it
            },
            modifier = Modifier
                .height(IntrinsicSize.Max)
                .padding(top = 16.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(
                    color = MaterialTheme.colorScheme.surfaceContainerLow
                )
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.surfaceDim,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(8.dp),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .padding(8.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (password.isEmpty()) {
                        Text(text = "Password", color = MaterialTheme.colorScheme.tertiary)
                    }
                    innerTextField()
                }
            },
            textStyle = LocalTextStyle.current,
            visualTransformation = PasswordVisualTransformation()
        )

        Button(
            onClick = {
                onLogin(email, password)
            },
            enabled = email.isNotEmpty() && password.isNotEmpty(),
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.onTertiaryContainer
            )
        ) {
            Text(text = "Login")
        }
    }
}

@Preview
@Composable
fun LoginPreview() {
    LoginUI(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = MaterialTheme.colorScheme.background
            )
    ) { _, _ -> }
}