package com.newage.feature.pomodoro.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.timers.stopwatch.core.common.android.StopwatchViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Created by Janak on 27/11/23.
 */
@HiltViewModel
class PomodoroSharedViewModel @Inject constructor() : StopwatchViewModel() {

    private val _isPlaying = MutableLiveData(false)
    val isPlaying: LiveData<Boolean> = _isPlaying

    fun togglePlayState() {
        _isPlaying.value = !(_isPlaying.value ?: false)
    }

    fun setPlayState(isPlay: Boolean) {
        _isPlaying.value = isPlay
    }
}
