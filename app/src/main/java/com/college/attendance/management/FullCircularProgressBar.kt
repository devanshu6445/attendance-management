package com.college.attendance.management

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInteropFilter

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun FullCircularProgressBar(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .pointerInteropFilter {
                return@pointerInteropFilter true
            }
            .background(
                color = MaterialTheme.colorScheme.background.copy(alpha = .6f)
            ), contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}