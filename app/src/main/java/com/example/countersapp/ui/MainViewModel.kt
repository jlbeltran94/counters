package com.example.countersapp.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel

class MainViewModel @ViewModelInject constructor(
    private val navigator: Navigator
) : ViewModel() {

    fun sayHello() {
        navigator.sayHello()
    }
}