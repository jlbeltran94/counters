package com.example.countersapp.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.example.countersapp.R
import com.example.countersapp.data.preferences.UserPreferences
import com.example.countersapp.ui.navigation.NavigationDispatcher

class MainViewModel @ViewModelInject constructor(
    private val navigator: NavigationDispatcher,
    private val userPreferences: UserPreferences
) : ViewModel() {

    fun initStartDestination() {
        val startDestination =
            if (userPreferences.firstTime) R.id.welcomeFragment else R.id.countersFragment
        navigator.emit { navController ->
            val graph = navController.navInflater.inflate(R.navigation.app_navigation)
            graph.startDestination = startDestination
            navController.graph = graph
        }
    }

    fun onGetStarted() {
        userPreferences.firstTime = false
        navigator.emit { navController ->
            navController.navigate(R.id.action_welcomeFragment_to_countersFragment)
        }
    }
}