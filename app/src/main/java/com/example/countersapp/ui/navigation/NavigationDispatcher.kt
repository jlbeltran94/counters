package com.example.countersapp.ui.navigation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

typealias NavigationCommand = (NavController) -> Unit

@ActivityRetainedScoped
class NavigationDispatcher @Inject constructor() {

    private val _navigationCommands: MutableLiveData<NavigationCommand> = MutableLiveData()
    val navigationCommands: LiveData<NavigationCommand> = _navigationCommands

    fun emit(navigationCommand: NavigationCommand) {
        _navigationCommands.value = navigationCommand
    }
}