package com.newage.feature.pomodoro.result

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.newage.feature.pomodoro.base.PomodoroButtonClickListener
import com.newage.feature.pomodoro.base.StopFinishButtonUIListener
import com.timers.stopwatch.core.common.android.StopwatchFragment
import com.timers.stopwatch.core.common.android.navigation.NavigationCommand
import com.timers.stopwatch.core.common.android.navigation.StopWatchNavDirection
import com.timers.stopwatch.feature.pomodoro.result.R
import com.timers.stopwatch.feature.pomodoro.result.databinding.FragmentPomodoroResultBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by Janak.
 */
@AndroidEntryPoint
class PomodoroResultFragment :
    StopwatchFragment<FragmentPomodoroResultBinding, PomodoroResultViewModel>(
        FragmentPomodoroResultBinding::inflate,
    ),
    PomodoroButtonClickListener {
    override val viewModel: PomodoroResultViewModel by viewModels()

    override fun onReset() {
        viewModel.navigate(
            NavigationCommand.To(
                StopWatchNavDirection(
                    com.timers.stopwatch.feature.pomodoro.base.R.id.action_reset_to_initial,
                ),
            ),
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.handleArgs(requireArguments())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvTime.text = viewModel.startedAt

        setStopFinishButtonUIListeners()

        if (viewModel.completed > 0) {
            binding.tvTitle.text = resources.getString(R.string.well_done)
            binding.ivResultIcon.setImageResource(R.drawable.ic_complete_check_mark)
            binding.rvTomatoList.visibility = View.VISIBLE
            binding.rvTomatoList.adapter = TomatoAdapter(viewModel.completed)
            binding.tvPomodoroCount.text = resources.getQuantityString(
                R.plurals.completed_pomodoros_count,
                viewModel.completed,
                viewModel.completed,
            )
        } else {
            binding.tvTitle.text = resources.getString(R.string.uh_no)
            binding.rvTomatoList.visibility = View.INVISIBLE
            binding.ivResultIcon.setImageResource(R.drawable.ic_not_complete_cross_mark)
            binding.tvPomodoroCount.text =
                resources.getString(R.string.you_have_not_completed_any_pomodoros)
        }
    }

    private fun setStopFinishButtonUIListeners() {
        if (parentFragment?.parentFragment is StopFinishButtonUIListener) {
            (parentFragment?.parentFragment as StopFinishButtonUIListener).changeStopFinishVisibility(
                View.INVISIBLE,
            )
            (parentFragment?.parentFragment as StopFinishButtonUIListener).changePlayPauseVisibility(
                View.INVISIBLE,
            )
            (parentFragment?.parentFragment as StopFinishButtonUIListener).resetButtonText("Restart")

        }
    }
}
