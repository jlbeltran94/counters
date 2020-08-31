package com.example.countersapp.ui.examples

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.countersapp.databinding.FragmentExamplesBinding
import com.example.countersapp.ui.examples.listeners.ChipClickListener
import com.example.countersapp.ui.examples.adapters.ExamplesAdapter
import com.example.countersapp.ui.models.Example
import com.example.countersapp.util.navigateBackWithResult
import com.example.countersapp.util.observe
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExamplesFragment : Fragment(), ChipClickListener {

    private lateinit var binding: FragmentExamplesBinding
    private val examplesAdapter by lazy { ExamplesAdapter() }
    private val examplesViewModel by viewModels<ExamplesViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentExamplesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.recyclerExamples.adapter = examplesAdapter
        examplesAdapter.listener = this
        observe(examplesViewModel.examplesLiveData) {
            examplesAdapter.data = it
        }
        examplesViewModel.loadExamples()
    }

    override fun onChipSelected(chip: String) {
        examplesViewModel.navBackWithSelectedExample(chip)
    }
}