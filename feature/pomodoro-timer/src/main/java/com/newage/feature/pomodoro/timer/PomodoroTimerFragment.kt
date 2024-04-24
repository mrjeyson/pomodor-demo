package com.newage.feature.pomodoro.timer

import android.os.Bundle
import android.text.format.DateFormat
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.newage.feature.pomodoro.base.PomodoroButtonClickListener
import com.newage.feature.pomodoro.base.PomodoroSharedViewModel
import com.newage.feature.pomodoro.base.StopFinishButtonUIListener
import com.timers.stopwatch.core.common.android.StopwatchFragment
import com.timers.stopwatch.core.common.android.navigation.NavigationCommand
import com.timers.stopwatch.core.common.android.navigation.StopWatchNavDirection
import com.timers.stopwatch.feature.pomodoro.timer.R
import com.timers.stopwatch.feature.pomodoro.timer.databinding.FragmentPomodoroTimerBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar

/**
 * Created by Janak.
 */
@AndroidEntryPoint
class PomodoroTimerFragment :
    StopwatchFragment<FragmentPomodoroTimerBinding, PomodoroTimerViewModel>(
        FragmentPomodoroTimerBinding::inflate,
    ),
    PomodoroButtonClickListener {

    override val viewModel: PomodoroTimerViewModel by viewModels()
    private val pomodoroSharedViewModel: PomodoroSharedViewModel by activityViewModels()

    private lateinit var tomatoAdapter: TomatoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.handleArgs(requireArguments())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setStopFinishButtonUIListeners()
        tomatoAdapter = TomatoAdapter(0)
        binding.rvTomatoList.adapter = tomatoAdapter

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    viewModel.timer.pause()
                    viewModel.navigate(
                        NavigationCommand.To(
                            StopWatchNavDirection(
                                com.timers.stopwatch.feature.pomodoro.base.R.id.action_reset_to_initial,
                            ),
                        ),
                    )
                }
            },
        )

        timerSetup()
        observe()
        observeTimeMode()
        binding.ibNext.setOnClickListener {
            viewModel.startNextPomodoro()
        }
    }

    private fun timerSetup() {
        val delegate = "hh:mm aaa"
        viewModel.setStartedAtTime(
            DateFormat.format(delegate, Calendar.getInstance().time).toString(),
        )
        viewModel.setStartTimerMode(PomodoroTimerViewModel.TimerMode.FOCUS)
        viewModel.timer.start()
    }

    private fun observe() {
        viewModel.completedCount.observe(viewLifecycleOwner) {
            tomatoAdapter.count = it
            tomatoAdapter.notifyDataSetChanged()
        }

        viewModel.pomodoroRoundLive.observe(viewLifecycleOwner) {
            binding.tvRoundCount.text =
                getString(R.string.round_dynamic, it)
        }

        viewModel.currentMinutes.observe(viewLifecycleOwner) {
            binding.tvTimer.text = it
        }

        viewModel.currentProgress.observe(viewLifecycleOwner) {
            binding.progressBar.progress = it
        }

        viewModel.pomodoroCountLive.observe(viewLifecycleOwner) {
            binding.tvPomodoroCount.text = getString(R.string.pomodoro_round_dynamic, it)
            binding.ibPrevious.visibility = if (it > 1) View.VISIBLE else View.INVISIBLE
        }
    }

    private fun observeTimeMode() {
        viewModel.timerMode.observe(viewLifecycleOwner) {
            tomatoAdapter.count = (viewModel.getCompletedCount() - 1)
            tomatoAdapter.notifyDataSetChanged()
            when (it) {
                PomodoroTimerViewModel.TimerMode.FOCUS -> {
                    onFocusMode()
                }

                PomodoroTimerViewModel.TimerMode.SHORT_BREAK -> {
                    binding.tvTitle.setTextAppearance(
                        requireContext(),
                        R.style.TextAppearance_Pomodoro_Content_Timer_Text_Red,
                    )
                    binding.tvPomodoroCount.setTextAppearance(
                        requireContext(),
                        R.style.TextAppearance_Pomodoro_Content_Timer_Text_Red,
                    )
                    binding.progressBar.setIndicatorColor(
                        ContextCompat.getColor(
                            requireContext(),
                            com.timers.stopwatch.core.common.android.R.color.progress_track_red_color,
                        ),
                    )
                    binding.tvTitle.text = getString(R.string.sort_break)
                }

                PomodoroTimerViewModel.TimerMode.LONG_BREAK -> {
                    binding.tvTitle.setTextAppearance(
                        requireContext(),
                        R.style.TextAppearance_Pomodoro_Content_Timer_Text_Blue,
                    )
                    binding.tvPomodoroCount.setTextAppearance(
                        requireContext(),
                        R.style.TextAppearance_Pomodoro_Content_Timer_Text_Blue,
                    )
                    binding.progressBar.setIndicatorColor(
                        ContextCompat.getColor(
                            requireContext(),
                            com.timers.stopwatch.core.common.android.R.color.contents_main_background_color,
                        ),
                    )
                    binding.tvTitle.text = getString(R.string.long_break)
                }

                PomodoroTimerViewModel.TimerMode.DONE -> {
                    // No implementation
                }
            }
        }
    }

    private fun onFocusMode() {
        binding.tvTitle.setTextAppearance(
            requireContext(),
            R.style.TextAppearance_Pomodoro_Content_Timer_Text_Green,
        )
        binding.tvPomodoroCount.setTextAppearance(
            requireContext(),
            R.style.TextAppearance_Pomodoro_Content_Timer_Text_Green,
        )
        binding.progressBar.setIndicatorColor(
            ContextCompat.getColor(
                requireContext(),
                com.timers.stopwatch.core.common.android.R.color.pomodoro_timer_content_color,
            ),
        )
        binding.tvTitle.text = getString(R.string.focus)
    }

    private fun setStopFinishButtonUIListeners() {
        if (parentFragment?.parentFragment is StopFinishButtonUIListener) {
            (parentFragment?.parentFragment as StopFinishButtonUIListener).changeStopFinishVisibility(
                View.VISIBLE,
            )
            (parentFragment?.parentFragment as StopFinishButtonUIListener).stopButtonText("Finish")
            (parentFragment?.parentFragment as StopFinishButtonUIListener).changeStopFinishIcon(
                isFinish = true,
            )
        }
    }

    override fun onReset() {
        pomodoroSharedViewModel.setPlayState(true)
        viewModel.timer.restart()
    }

    override fun onStartPause() {
        if (pomodoroSharedViewModel.isPlaying.value == true) {
            viewModel.timer.pause()
        } else {
            viewModel.timer.start()
        }
        pomodoroSharedViewModel.togglePlayState()
    }

    override fun onStopFinish() {
        viewModel.timer.pause()
        val bundle = Bundle()
        bundle.putInt("completed", (viewModel.getCompletedCount() - 1))
        bundle.putString("startedAt", viewModel.getStartedAtTime())
        viewModel.navigate(
            NavigationCommand.To(
                StopWatchNavDirection(
                    com.timers.stopwatch.feature.pomodoro.base.R.id.action_timer_to_result,
                    bundle,
                ),
            ),
        )
    }
}
