package com.example.countersapp.ui.createcounters

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.countersapp.databinding.FragmentCreateCounterBinding
import com.example.countersapp.util.SimpleDialogFactory
import com.example.countersapp.util.invisible
import dagger.hilt.android.AndroidEntryPoint


/**
 * A simple [Fragment] subclass.
 * Use the [CreateCounterFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class CreateCounterFragment : Fragment() {


    private lateinit var binding: FragmentCreateCounterBinding
    private val createCountersViewModel by viewModels<CreateCounterViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCreateCounterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        createCountersViewModel.stateLiveData.observe(viewLifecycleOwner) {
            handleState(it)
        }
        binding.btnSave.setOnClickListener { _ ->
            binding.createCounterTextInputLayout.error = ""
            binding.createCounterTextInputLayout.editText?.text.toString().takeIf {
                it.isNotEmpty()
            }?.let {
                createCountersViewModel.createCounter(it)
            } ?: kotlin.run {
                binding.createCounterTextInputLayout.error = "Please give it a name!"
            }
        }
        binding.icClose.setOnClickListener {
            createCountersViewModel.navigateBack()
        }
    }

    private fun handleState(createCounterFragmentState: CreateCounterFragmentState) {
        when (createCounterFragmentState) {
            is CreateCounterFragmentState.Loading -> {
                setSavingVisibility(btnInvisible = true, progressBarInvisible = false)
            }
            is CreateCounterFragmentState.Success -> {
                createCountersViewModel.navigateBack()
            }
            is CreateCounterFragmentState.Error -> {
                setSavingVisibility(btnInvisible = false, progressBarInvisible = true)
                showErrorDialog()
                Log.e("COUNTERS_FRAGMENT", createCounterFragmentState.throwable.message)
            }
        }
    }

    private fun showErrorDialog() {
        SimpleDialogFactory.createDialog(
            requireContext(),
            title = "Couldn't crate the counter",
            message = "The internet connection appears to be offline",
            positiveButton = "Ok" to SimpleDialogFactory.noAction
        ).show()
    }

    private fun setSavingVisibility(btnInvisible: Boolean, progressBarInvisible: Boolean) {
        binding.saveProgressBar.invisible(progressBarInvisible)
        binding.btnSave.invisible(btnInvisible)
    }
}