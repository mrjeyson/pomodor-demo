package com.newage.feature.pomodoro.initial
/**
 * Created by Janak on 27/11/23.
 */
sealed class ValidationResult {
    data object Valid : ValidationResult()
    data class ValidHourMinute(val hour: Int, val minute: Int) : ValidationResult()
    data class Invalid(val message: String) : ValidationResult()
    data object Error : ValidationResult()
}
