package com.newage.feature.pomodoro.initial

import android.os.Bundle
import com.timers.stopwatch.core.common.android.StopwatchViewModel
import com.timers.stopwatch.core.common.android.navigation.NavigationCommand
import com.timers.stopwatch.core.common.android.navigation.StopWatchNavDirection
import com.timers.stopwatch.feature.pomodoro.base.R
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Created by Janak.
 */
@HiltViewModel
class PomodoroInitialViewModel @Inject constructor() : StopwatchViewModel() {
    fun navigateToTimer(
        focusDuration: Long,
        shortBreakDuration: Long,
        longBreakDuration: Long,
        longBreakAfter: Int,
    ) {
        val bundle = Bundle()
        bundle.putLong("focusDuration", focusDuration)
        bundle.putLong("sortBreakDuration", shortBreakDuration)
        bundle.putLong("longBreakDuration", longBreakDuration)
        bundle.putInt("longBreakAfterDuration", longBreakAfter)
        navigate(
            NavigationCommand.To(
                StopWatchNavDirection(
                    R.id.action_initial_to_timer,
                    bundle,
                ),
            ),
        )
    }
}
