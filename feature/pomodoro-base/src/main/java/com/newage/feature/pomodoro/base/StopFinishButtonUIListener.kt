package com.newage.feature.pomodoro.base

/**
 * Created by Janak on 27/11/23.
 */
interface StopFinishButtonUIListener {
    fun changeStopFinishIcon(isFinish: Boolean) {
        // Not Needed
    }

    fun changeStopFinishVisibility(visibility: Int) {
        // Not Needed
    }

    fun changePlayPauseVisibility(visibility: Int) {
        // Not Needed
    }

    fun stopButtonText(text: String)

    fun resetButtonText(text: String)
}
