package com.newage.feature.pomodoro.result

import android.os.Bundle
import com.timers.stopwatch.core.common.android.StopwatchViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Created by Janak.
 */
@HiltViewModel
class PomodoroResultViewModel @Inject constructor() : StopwatchViewModel() {
    var completed: Int = 0
    var startedAt: String = ""
    fun handleArgs(requireArguments: Bundle) {
        completed = requireArguments.getInt("completed")
        startedAt = requireArguments.getString("startedAt") ?: ""
    }
}
