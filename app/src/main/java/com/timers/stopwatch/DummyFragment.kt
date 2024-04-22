package com.timers.stopwatch

import androidx.fragment.app.viewModels
import com.timers.stopwatch.core.common.android.StopwatchFragment
import com.timers.stopwatch.databinding.FragmentDummyBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by Andriy Deputat email(andriy.deputat@gmail.com) on 01.11.2023.
 */
@AndroidEntryPoint
class DummyFragment : StopwatchFragment<FragmentDummyBinding, DummyViewModel>(FragmentDummyBinding::inflate) {
    override val viewModel: DummyViewModel by viewModels<DummyViewModel>()
}
