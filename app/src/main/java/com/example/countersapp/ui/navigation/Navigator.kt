package com.example.countersapp.ui.navigation

import androidx.annotation.IdRes
import androidx.navigation.NavController

interface Navigator {

    val navController: NavController?
    fun initStartDestination(firstTime: Boolean)
    fun navigateToCounters()
    fun navigateToCreateCounter()
    fun navigateUp()
}