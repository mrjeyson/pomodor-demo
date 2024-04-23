package com.newage.feature.pomodoro

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.newage.feature.pomodoro.base.PomodoroButtonClickListener
import com.newage.feature.pomodoro.base.PomodoroSharedViewModel
import com.newage.feature.pomodoro.base.StopFinishButtonUIListener
import com.timers.stopwatch.core.common.android.ChildDestinationChangeListener
import com.timers.stopwatch.core.common.android.StopwatchFragment
import com.timers.stopwatch.feature.pomodoro.R
import com.timers.stopwatch.feature.pomodoro.databinding.FragmentPomodoroBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by Andriy Deputat email(andriy.deputat@gmail.com) on 01.11.2023.
 */
@AndroidEntryPoint
class PomodoroFragment :
    StopwatchFragment<FragmentPomodoroBinding, PomodoroViewModel>(
        FragmentPomodoroBinding::inflate,
    ),
    StopFinishButtonUIListener {

    override val viewModel: PomodoroViewModel by viewModels()
    private val pomodoroSharedViewModel: PomodoroSharedViewModel by activityViewModels()

    private var navController: NavController? = null
    private lateinit var navHostFragment: NavHostFragment
    private var childDestinationChangeListener: ChildDestinationChangeListener? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        childDestinationChangeListener = activity as ChildDestinationChangeListener?
        navHostFragment =
            childFragmentManager.findFragmentById(R.id.nav_host_pomodoro_fragment) as NavHostFragment
        navController = navHostFragment.navController
        navController?.addOnDestinationChangedListener { controller, destination, arguments ->
            childDestinationChangeListener?.onChildDestinationChanged(
                controller,
                destination,
                arguments,
            )
            when (destination.id) {
                com.timers.stopwatch.feature.pomodoro.initial.R.id.pomodoro_initial -> {
                    pomodoroSharedViewModel.setPlayState(false)
                    changeStopFinishVisibility(View.INVISIBLE)
                }

                else -> {
                    // Not needed
                }
            }
        }
        pomodoroSharedViewModel.isPlaying.observe(viewLifecycleOwner) {
            if (it) {
                binding.fabPlayPause.setImageResource(R.drawable.ic_pomodoro_pause)
            } else {
                binding.fabPlayPause.setImageResource(R.drawable.ic_pomodoro_play)
            }
        }

        setClickListeners(
            binding.fabReset,
            binding.fabPlayPause,
            binding.fabStopFinish,
        )
    }

    override fun onClick(p0: View?) {
        when (p0) {
            binding.fabReset -> {
                val fragment = navHostFragment.childFragmentManager.fragments.first()
                if (fragment is PomodoroButtonClickListener) {
                    fragment.onReset()
                }
            }

            binding.fabPlayPause -> {
                val fragment = navHostFragment.childFragmentManager.fragments.first()
                if (fragment is PomodoroButtonClickListener) {
                    fragment.onStartPause()
                }
            }

            binding.fabStopFinish -> {
                val fragment = navHostFragment.childFragmentManager.fragments.first()
                if (fragment is PomodoroButtonClickListener) {
                    fragment.onStopFinish()
                }
            }
        }
    }

    override fun changeStopFinishIcon(isFinish: Boolean) {
        super.changeStopFinishIcon(isFinish)
        binding.fabStopFinish.setImageResource(
            if (isFinish) {
                R.drawable.ic_pomodoro_finish
            } else {
                R.drawable.ic_pomodoro_stop
            },
        )
    }

    override fun changeStopFinishVisibility(visibility: Int) {
        super.changeStopFinishVisibility(visibility)
        binding.groupStopFinish.visibility = visibility
    }

    override fun changePlayPauseVisibility(visibility: Int) {
        super.changePlayPauseVisibility(visibility)
        binding.groupPlayPause.visibility = visibility
    }

    override fun stopButtonText(text: String) {
        binding.tvStopFinish.text = text
    }

    override fun resetButtonText(text: String) {
        binding.tvReset.text = text
    }
}
