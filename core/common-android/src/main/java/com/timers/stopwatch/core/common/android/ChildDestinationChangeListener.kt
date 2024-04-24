package com.timers.stopwatch.core.common.android

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.NavDestination

interface ChildDestinationChangeListener {
    fun onChildDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?,
    )
}
