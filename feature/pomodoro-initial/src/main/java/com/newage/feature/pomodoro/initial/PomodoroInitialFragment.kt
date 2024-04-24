package com.newage.feature.pomodoro.initial

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.newage.feature.pomodoro.base.PomodoroButtonClickListener
import com.newage.feature.pomodoro.base.StopFinishButtonUIListener
import com.timers.stopwatch.core.common.android.StopwatchFragment
import com.timers.stopwatch.core.common.android.extensions.showToastShort
import com.timers.stopwatch.core.common.android.extensions.toMilliseconds
import com.timers.stopwatch.feature.pomodoro.initial.databinding.FragmentPomodoroInitialBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by Janak.
 */

@AndroidEntryPoint
class PomodoroInitialFragment :
    StopwatchFragment<
        FragmentPomodoroInitialBinding,
        PomodoroInitialViewModel,
        >(FragmentPomodoroInitialBinding::inflate),
    PomodoroButtonClickListener {

    override val viewModel: PomodoroInitialViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setStopFinishButtonUIListeners()
    }
    override fun onReset() {
        binding.focusDuration.reset()
        binding.shortBreakDuration.reset()
        binding.longBreakDuration.reset()
        binding.longBreakAfter.reset()
    }

    override fun onStartPause() {
        val isFocusDurationValid = validateFocusDuration()
        val isBreakDurationValid = validateBreakDuration()
        val isLongBreakDurationValid = validateLongBreakDuration()
        val isLongBreakAfterDurationValid = validateLongBreakAfter()
        val isValid = isFocusDurationValid && isBreakDurationValid &&
            isLongBreakDurationValid && isLongBreakAfterDurationValid
        if (isValid) {
            viewModel.navigateToTimer(
                binding.focusDuration.getTimeText().toMilliseconds(),
                binding.shortBreakDuration.getTimeText().toMilliseconds(),
                binding.longBreakDuration.getTimeText().toMilliseconds(),
                binding.longBreakAfter.getText().toInt(),
            )
        }
    }

    private fun setStopFinishButtonUIListeners() {
        if (parentFragment?.parentFragment is StopFinishButtonUIListener) {
            (parentFragment?.parentFragment as StopFinishButtonUIListener).changeStopFinishVisibility(
                View.INVISIBLE,
            )
            (parentFragment?.parentFragment as StopFinishButtonUIListener).changePlayPauseVisibility(
                View.VISIBLE,
            )
        }
    }

    private fun validateFocusDuration(): Boolean {
        return when (val result = binding.focusDuration.validateInputs()) {
            is ValidationResult.Invalid -> {
                showToastShort(result.message)
                false
            }

            is ValidationResult.ValidHourMinute -> {
                true
            }

            else -> {
                false
            }
        }
    }
    private fun validateBreakDuration(): Boolean {
        return when (val result = binding.shortBreakDuration.validateInputs()) {
            is ValidationResult.Invalid -> {
                showToastShort(result.message)
                false
            }
            is ValidationResult.ValidHourMinute -> {
                true
            }
            else -> {
                false
            }
        }
    }
    private fun validateLongBreakDuration(): Boolean {
        return when (val result = binding.longBreakDuration.validateInputs()) {
            is ValidationResult.Invalid -> {
                showToastShort(result.message)
                false
            }
            is ValidationResult.ValidHourMinute -> {
                true
            }
            else -> {
                false
            }
        }
    }
    private fun validateLongBreakAfter(): Boolean {
        return when (val result = binding.longBreakAfter.validateInputs()) {
            is ValidationResult.Invalid -> {
                showToastShort(result.message)
                false
            }
            is ValidationResult.ValidHourMinute -> {
                true
            }
            else -> {
                false
            }
        }
    }
}
