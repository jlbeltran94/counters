package com.example.countersapp.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.example.countersapp.R
import com.example.countersapp.databinding.ActivityMainBinding
import com.example.countersapp.ui.navigation.NavigationDispatcher
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels()

    @Inject
    lateinit var navigationDispatcher: NavigationDispatcher

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navigationDispatcher.navigationCommands.observe(this) { command ->
            command.invoke(findNavController(R.id.nav_host_fragment))
        }
    }

    override fun onStart() {
        super.onStart()
        mainViewModel.initStartDestination()
    }
}