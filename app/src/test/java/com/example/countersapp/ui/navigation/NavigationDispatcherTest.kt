package com.example.countersapp.ui.navigation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.countersapp.utils.getOrAwaitValue
import org.junit.After
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*
import org.junit.Rule

class NavigationDispatcherTest {

    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    val navigationDispatcher by lazy { NavigationDispatcher() }

    @Test
    fun emit() {
        val command: NavigationCommand = { it.popBackStack() }
        navigationDispatcher.emit(command)
        assertEquals(command, navigationDispatcher.navigationCommands.getOrAwaitValue())
    }
}