package com.example.countersapp.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.example.countersapp.data.preferences.UserPreferences
import com.example.countersapp.ui.navigation.Navigator

class MainViewModel @ViewModelInject constructor(
    private val navigator: Navigator,
    private val userPreferences: UserPreferences
) : ViewModel() {

    fun initStartDestination(){
        navigator.initStartDestination(userPreferences.firstTime)
    }

    fun setNotFirstTime(){
        userPreferences.firstTime = false
    }

    fun navToCounters(){
        navigator.navigateToCounters()
    }
}