package com.timers.stopwatch

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.timers.stopwatch.core.common.android.ChildDestinationChangeListener
import com.timers.stopwatch.databinding.ActivityAppBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by Andriy Deputat email(andriy.deputat@gmail.com) on 21.11.2022.
 */
@AndroidEntryPoint
class StopwatchActivity : AppCompatActivity(), ChildDestinationChangeListener {

    lateinit var binding: ActivityAppBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = ActivityAppBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment?
        val navController = navHostFragment!!.navController
        binding.bottomNavView.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            handleDestinationChange(controller, destination, arguments)
        }
    }

    private fun handleDestinationChange(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?,
    ) {
        supportActionBar?.title = destination.label
        when (destination.id) {
            R.id.home,
            R.id.stopwatch,
            R.id.timer,
            R.id.intervals,
            R.id.pomodoro,
            com.timers.stopwatch.feature.pomodoro.initial.R.id.pomodoro_initial,
            -> {
                supportActionBar?.setDisplayHomeAsUpEnabled(false)
                showHideBottomNav(true)
            }

            else -> {
                supportActionBar?.setDisplayHomeAsUpEnabled(true)
                showHideBottomNav(false)
            }
        }
    }

    fun showHideBottomNav(isShow: Boolean) {
        if (isShow) {
            binding.bottomNavView.visibility = View.VISIBLE
        } else {
            binding.bottomNavView.visibility = View.GONE
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onChildDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?,
    ) {
        handleDestinationChange(controller, destination, arguments)
    }
}
