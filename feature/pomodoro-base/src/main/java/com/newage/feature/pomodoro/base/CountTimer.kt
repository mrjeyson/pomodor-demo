package com.newage.feature.pomodoro.base

import android.os.CountDownTimer

abstract class CountTimer {

    private lateinit var countDownTimer: CountDownTimer
    private var remainingTime: Long = 0
    private var isTimerPaused: Boolean = true
    private var duration: Long = 0
    private var interval: Long = 0

    fun setTime(duration: Long, interval: Long) {
        this.duration = duration
        this.interval = interval
        this.remainingTime = duration
        isTimerPaused = true
    }

    fun start() {
        if (isTimerPaused) {
            countDownTimer = object : CountDownTimer(remainingTime, interval) {
                override fun onFinish() {
                    onTimerFinish()
                }

                override fun onTick(millisUntilFinished: Long) {
                    remainingTime = millisUntilFinished
                    onTimerTick(millisUntilFinished)
                }
            }.apply {
                start()
            }

            isTimerPaused = false
        }
    }

    fun pause() {
        if (!isTimerPaused) {
            countDownTimer.cancel()
        }
        isTimerPaused = true
    }

    fun restart() {
        countDownTimer.cancel()
        remainingTime = duration
        isTimerPaused = true
        start()
    }

    protected abstract fun onTimerTick(currentProgress: Long)
    protected abstract fun onTimerFinish()
    protected abstract fun onTimerStart()
}
