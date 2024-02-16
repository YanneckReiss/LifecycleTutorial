@file:OptIn(ExperimentalMaterial3Api::class)

package com.yanneckreiss.lifecycletutorial.ui.screens.timer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yanneckreiss.lifecycletutorial.ui.theme.TutorialTheme
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun TimerScreen(
    viewModel: TimerScreenViewModel = viewModel { TimerScreenViewModel() }
) {

    val state: TimerScreenState by viewModel.state.collectAsStateWithLifecycle()

    LifecycleResumeEffect(viewModel) {
        viewModel.startTimer()
        onPauseOrDispose {
            viewModel.stopTimer()
        }
    }

    TimerScreenContent(
        passedTimeInMillis = { state.passedMilliseconds }
    )
}

@Composable
private fun TimerScreenContent(
    passedTimeInMillis: () -> Long
) {

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = "Screen Time Tracker") }
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Active screen time")
            TimeDisplayText(timeInMillis = passedTimeInMillis)
        }
    }
}

@Composable
fun TimeDisplayText(timeInMillis: () -> Long) {
    val duration: Duration = timeInMillis().milliseconds

    val hours: Long = duration.inWholeHours % 24
    val minutes: Long = (duration.inWholeMinutes % 60)
    val seconds: Long = (duration.inWholeSeconds % 60)
    val milliseconds: Long = (duration.inWholeMilliseconds % 1000)

    val formattedTime = String.format("%02d:%02d:%02d:%03d", hours, minutes, seconds, milliseconds)

    Text(
        text = "⌛ $formattedTime",
        style = MaterialTheme.typography.headlineLarge
    )
}

@Composable
@Preview
private fun Preview_TimerScreen() {
    TutorialTheme {
        TimerScreenContent(
            passedTimeInMillis = { 2_300L }
        )
    }
}
