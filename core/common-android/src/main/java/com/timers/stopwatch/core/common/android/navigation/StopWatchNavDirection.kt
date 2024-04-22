package com.timers.stopwatch.core.common.android.navigation

import android.os.Bundle
import androidx.navigation.NavDirections

/**
 * Created by Janak on 05/12/23.
 */
data class StopWatchNavDirection(override val actionId: Int, override val arguments: Bundle = Bundle()) : NavDirections
