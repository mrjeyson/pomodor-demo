package com.newage.feature.pomodoro.timer

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.timers.stopwatch.core.common.android.StopwatchViewModel
import com.timers.stopwatch.core.common.android.extensions.MILLISECONDS
import com.timers.stopwatch.core.common.android.extensions.POMODORO_START_SEC
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Created by Janak.
 */
@HiltViewModel
class PomodoroStartTimerViewModel @Inject constructor() : StopwatchViewModel() {

    private val _timeCounter = MutableLiveData(POMODORO_START_SEC)
    val timeCounter: LiveData<Int> = _timeCounter

    var handlerBase = Handler(Looper.getMainLooper())
    val countDownRunnable = Runnable {
        var count = _timeCounter.value ?: 0
        count--
        _timeCounter.postValue(count)
        if (count > 0) {
            startCountDown()
        }
    }

    fun startCountDown() {
        handlerBase.postDelayed(countDownRunnable, MILLISECONDS)
    }
    fun pauseCountDown() {
        handlerBase.removeCallbacks(countDownRunnable)
    }
    fun stopCountDown() {
        _timeCounter.value = POMODORO_START_SEC
        handlerBase.removeCallbacks(countDownRunnable)
    }
}
