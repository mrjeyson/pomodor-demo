package com.newage.feature.pomodoro.base

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by Janak on 27/11/23.
 */

@Parcelize
data class PomodoroResult(val completedCount: Int, val startedAtTime: String) : Parcelable
