package com.example.countersapp.ui.navigation

import androidx.navigation.NavController
import com.example.countersapp.R
import javax.inject.Inject

class NavigatorImp @Inject constructor(navActivity: NavActivity?) : Navigator {

    override val navController: NavController? = navActivity?.getNavController()

    override fun initStartDestination(firstTime: Boolean) {
        val startDestination = if (firstTime) R.id.welcomeFragment else R.id.countersFragment
        navController?.let {
            val graph = navController.navInflater.inflate(R.navigation.app_navigation)
            graph.startDestination = startDestination
            it.graph = graph
        }
    }

    override fun navigateToCounters() {
        navController?.navigate(R.id.action_welcomeFragment_to_countersFragment)
    }

    override fun navigateToCreateCounter() {
        navController?.navigate(R.id.createCounterFragment)
    }

    override fun navigateUp() {
        navController?.navigateUp()
    }
}