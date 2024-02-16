package com.yanneckreiss.lifecycletutorial.ui.screens.timer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield

private const val TIMER_DELAY_MS = 10L

class TimerScreenViewModel : ViewModel() {

    private val _state = MutableStateFlow(TimerScreenState())
    val state: StateFlow<TimerScreenState> = _state.asStateFlow()

    private var timerJob: Job? = null
    private var startTime = 0L

    fun startTimer() {
        if (timerJob?.isActive != true) {
            // Resume from last paused time
            startTime = System.currentTimeMillis() - _state.value.passedMilliseconds
            timerJob = viewModelScope.launch {
                while (isActive) {
                    val currentTime: Long = System.currentTimeMillis()
                    val elapsedTime: Long = currentTime - startTime
                    _state.value = TimerScreenState(elapsedTime)
                    delay(TIMER_DELAY_MS)
                    yield()
                }
            }
        }
    }

    fun stopTimer() {
        timerJob?.cancel()
        timerJob = null
    }
}
