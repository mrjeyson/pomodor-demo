package com.timers.stopwatch.core.common.android.extensions

import java.util.Locale

const val MILLISECONDS = 1000L
const val SIXTY_SECOND = 60
const val PERCENT_FACTOR = 100
const val POMODORO_START_SEC = 5

const val MAX_MINUTE = 59
const val MAX_HOUR = 11

fun Long.toMinutes(): String {
    val totalSeconds = this / MILLISECONDS
    val minutes = totalSeconds / SIXTY_SECOND
    val seconds = totalSeconds % SIXTY_SECOND
    return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
}

fun Long.toPercentage(total: Long): Int {
    return ((this / total.toFloat()) * PERCENT_FACTOR).toInt()
}
fun String.toMilliseconds(): Long {
    val timeParts = this.split(":")

    if (timeParts.size == 2) {
        try {
            val hours = timeParts[0].trim().toLong()
            val minutes = timeParts[1].trim().toLong()

            val totalMilliseconds = (hours * SIXTY_SECOND + minutes) * SIXTY_SECOND * MILLISECONDS
            return totalMilliseconds
        } catch (e: NumberFormatException) {
            // e.printStackTrace()
        }
    }

    return -1
}
