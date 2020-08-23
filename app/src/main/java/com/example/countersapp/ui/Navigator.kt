package com.example.countersapp.ui

import android.util.Log
import androidx.navigation.NavController
import javax.inject.Inject

class NavigatorImp @Inject constructor(navActivity: NavActivity?): Navigator {

    override fun sayHello() {
        Log.d("NAVIGATOR", "HELLO")
    }
}

interface NavActivity {
    fun getNavController():NavController
}

interface Navigator {
    fun sayHello()
}