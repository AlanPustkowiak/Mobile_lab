package com.example.mobile_lab.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CocktailTimerViewModel : ViewModel() {
    private val _timeLeft = MutableStateFlow(0)
    val timeLeft: StateFlow<Int> = _timeLeft

    private val _isRunning = MutableStateFlow(false)
    val isRunning: StateFlow<Boolean> = _isRunning

    private val _isPaused = MutableStateFlow(false)
    val isPaused: StateFlow<Boolean> = _isPaused

    private var timerJob: Job? = null
    private var initialDuration = 0

    fun initialize(durationSeconds: Int){
        if (initialDuration == 0) {
         initialDuration = durationSeconds
         _timeLeft.value = durationSeconds
        }
    }

    fun startTimer() {
        if (!_isRunning.value || _isPaused.value){
            _isRunning.value = true
            _isPaused.value = false

            timerJob?.cancel()
            timerJob = viewModelScope.launch {
                while (_timeLeft.value > 0 && _isRunning.value) {
                    delay(1000)
                    _timeLeft.value -= 1

                    if (_timeLeft.value == 0) {
                        _isRunning.value = false
                    }
                }
            }

        }
    }

    fun pauseTimer() {
        if (_isRunning.value && !_isPaused.value){
            _isRunning.value = false
            _isPaused.value = true
            timerJob?.cancel()
        }
    }

    fun resetTimer() {
        timerJob?.cancel()
        _timeLeft.value = initialDuration
        _isRunning.value = false
        _isPaused.value = false
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}