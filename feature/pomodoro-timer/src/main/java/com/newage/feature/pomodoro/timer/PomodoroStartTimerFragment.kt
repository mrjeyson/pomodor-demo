package com.newage.feature.pomodoro.timer

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.newage.feature.pomodoro.base.PomodoroButtonClickListener
import com.newage.feature.pomodoro.base.PomodoroSharedViewModel
import com.newage.feature.pomodoro.base.StopFinishButtonUIListener
import com.timers.stopwatch.core.common.android.StopwatchFragment
import com.timers.stopwatch.core.common.android.navigation.NavigationCommand
import com.timers.stopwatch.core.common.android.navigation.StopWatchNavDirection
import com.timers.stopwatch.feature.pomodoro.timer.R
import com.timers.stopwatch.feature.pomodoro.timer.databinding.FragmentPomodoroStartTimerBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by Janak.
 */
@AndroidEntryPoint
class PomodoroStartTimerFragment :
    StopwatchFragment<FragmentPomodoroStartTimerBinding, PomodoroStartTimerViewModel>(
        FragmentPomodoroStartTimerBinding::inflate,
    ),
    PomodoroButtonClickListener {

    override val viewModel: PomodoroStartTimerViewModel by viewModels()
    private val pomodoroSharedViewModel: PomodoroSharedViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pomodoroSharedViewModel.setPlayState(true)
        viewModel.startCountDown()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.startingCount.text = viewModel.timeCounter.value.toString()
        setStopFinishButtonUIListeners()
        viewModel.timeCounter.observe(viewLifecycleOwner) {
            binding.startingCount.text = it.toString()
            if (it == 0) {
                viewModel.navigate(
                    NavigationCommand.To(
                        StopWatchNavDirection(
                            R.id.start_timer_to_timer,
                            requireArguments(),
                        ),
                    ),
                )
            }
        }
    }

    private fun setStopFinishButtonUIListeners() {
        if (parentFragment?.parentFragment is StopFinishButtonUIListener) {
            (parentFragment?.parentFragment as StopFinishButtonUIListener).changeStopFinishVisibility(
                View.VISIBLE,
            )
            (parentFragment?.parentFragment as StopFinishButtonUIListener).stopButtonText("Stop")
            (parentFragment?.parentFragment as StopFinishButtonUIListener).changeStopFinishIcon(
                isFinish = false,
            )
        }
    }

    override fun onReset() {
        viewModel.navigate(
            NavigationCommand.To(
                StopWatchNavDirection(
                    com.timers.stopwatch.feature.pomodoro.base.R.id.action_reset_to_initial,
                ),
            ),
        )
    }

    override fun onStartPause() {
        pomodoroSharedViewModel.togglePlayState()
        if (pomodoroSharedViewModel.isPlaying.value == true) {
            viewModel.startCountDown()
        } else {
            viewModel.pauseCountDown()
        }
    }

    override fun onStopFinish() {
        viewModel.navigate(
            NavigationCommand.To(
                StopWatchNavDirection(
                    com.timers.stopwatch.feature.pomodoro.base.R.id.action_reset_to_initial,
                ),
            ),
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.stopCountDown()
    }
}
