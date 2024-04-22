package com.newage.feature.pomodoro.timer

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.newage.feature.pomodoro.base.CountTimer
import com.timers.stopwatch.core.common.android.StopwatchViewModel
import com.timers.stopwatch.core.common.android.extensions.MILLISECONDS
import com.timers.stopwatch.core.common.android.extensions.toMinutes
import com.timers.stopwatch.core.common.android.extensions.toPercentage
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Created by Janak.
 */
@HiltViewModel
class PomodoroTimerViewModel @Inject constructor() : StopwatchViewModel() {
    val completedCount: LiveData<Int> = MutableLiveData<Int>(0)

    private var pomodoroCount = 1
    private var pomodoroRound = 1

    private var startedAtTime = "00:00 AM"

    private var focusDuration = 0L
    private var sortBreakDuration = 0L
    private var longBreakDuration = 0L
    private var longBreakAfter = 0

    private var percentageDuration = focusDuration

    private val _timerMode = MutableLiveData(TimerMode.FOCUS)
    val timerMode: LiveData<TimerMode> = _timerMode

    private val _currentProgress = MutableLiveData(0)
    val currentProgress: LiveData<Int> = _currentProgress

    private val _currentMinutes = MutableLiveData("00:00")
    val currentMinutes: LiveData<String> = _currentMinutes

    private val _tomatoCount = MutableLiveData<Int>(0)
    val tomatoCount: LiveData<Int> = _tomatoCount

    private val _isTimerFinished = MutableLiveData(false)
    val isTimerFinished: LiveData<Boolean> = _isTimerFinished

    private val _pomodoroCount = MutableLiveData(1)
    var pomodoroCountLive: LiveData<Int> = _pomodoroCount

    private val _pomodoroRound = MutableLiveData(1)
    var pomodoroRoundLive: LiveData<Int> = _pomodoroRound

    var timer: CountTimer = object : CountTimer() {
        override fun onTimerTick(currentProgress: Long) {
            _currentProgress.value = (currentProgress.toPercentage(percentageDuration) + 1)
            _currentMinutes.value = (currentProgress + MILLISECONDS).toMinutes()
        }

        override fun onTimerFinish() {
            _isTimerFinished.value = true

            _currentProgress.value = 0L.toPercentage(percentageDuration)
            _currentMinutes.value = 0L.toMinutes()

            updateTimerMode()
        }

        override fun onTimerStart() {
            _isTimerFinished.value = false
        }
    }

    fun setStartTimerMode(mode: TimerMode) {
        when (mode) {
            TimerMode.FOCUS -> {
                _timerMode.value = TimerMode.FOCUS
                percentageDuration = focusDuration
                timer.setTime(focusDuration, MILLISECONDS)
                timer.start()
            }

            TimerMode.SORT_BREAK -> {
                _timerMode.value = TimerMode.SORT_BREAK
                percentageDuration = sortBreakDuration
                timer.setTime(sortBreakDuration, MILLISECONDS)
                timer.start()
            }

            TimerMode.LONG_BREAK -> {
                _timerMode.value = TimerMode.LONG_BREAK
                percentageDuration = longBreakDuration
                timer.setTime(longBreakDuration, MILLISECONDS)
                timer.start()
            }

            TimerMode.DONE -> {
            }
        }
    }

    fun setStartedAtTime(time: String) {
        startedAtTime = time
    }

    fun getStartedAtTime(): String = startedAtTime

    fun handleArgs(requireArguments: Bundle) {
        this.focusDuration = requireArguments.getLong("focusDuration")
        this.sortBreakDuration = requireArguments.getLong("sortBreakDuration")
        this.longBreakDuration = requireArguments.getLong("longBreakDuration")
        this.longBreakAfter = requireArguments.getInt("longBreakAfterDuration")
    }

    fun getCompletedCount(): Int = pomodoroCount

    fun startNextPomodoro() {
        timer.pause()
        updateTimerMode()
    }

    private fun updateTimerMode() {
        when (_timerMode.value) {
            TimerMode.FOCUS -> {
                _timerMode.value = TimerMode.SORT_BREAK
                percentageDuration = sortBreakDuration
                timer.setTime(sortBreakDuration, MILLISECONDS)
                timer.start()
            }

            TimerMode.SORT_BREAK -> {
                if (pomodoroCount % longBreakAfter == 0) {
                    _timerMode.value = TimerMode.LONG_BREAK
                    percentageDuration = longBreakDuration
                    timer.setTime(longBreakDuration, MILLISECONDS)
                    timer.start()
                } else {
                    _timerMode.value = TimerMode.FOCUS
                    percentageDuration = focusDuration
                    timer.setTime(focusDuration, MILLISECONDS)
                    timer.start()
                }
                pomodoroCount += 1
                _pomodoroCount.value = pomodoroCount
            }

            TimerMode.LONG_BREAK -> {
                pomodoroRound += 1
                _pomodoroRound.value = pomodoroRound

                _timerMode.value = TimerMode.FOCUS
                percentageDuration = focusDuration
                timer.setTime(focusDuration, MILLISECONDS)
                timer.start()
            }

            TimerMode.DONE -> {
                _timerMode.value = TimerMode.FOCUS
                percentageDuration = sortBreakDuration
                timer.setTime(sortBreakDuration, MILLISECONDS)
                timer.start()
            }

            else -> {}
        }
    }

    enum class TimerMode {
        FOCUS, SORT_BREAK, LONG_BREAK, DONE
    }
}
