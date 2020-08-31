package com.example.countersapp.ui.createcounter

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.countersapp.R
import com.example.countersapp.databinding.FragmentCreateCounterBinding
import com.example.countersapp.util.Constants.SELECTED_EXAMPLE_KEY
import com.example.countersapp.util.SimpleDialogFactory
import com.example.countersapp.util.getSavedStateLiveData
import com.example.countersapp.util.invisible
import com.example.countersapp.util.observe
import dagger.hilt.android.AndroidEntryPoint

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
        observe(createCountersViewModel.stateLiveData) {
            handleState(it)
        }
        binding.btnSave.setOnClickListener { _ ->
            binding.createCounterTextInputLayout.error = null
            binding.createCounterTextInputLayout.editText?.text.toString().takeIf {
                it.isNotEmpty()
            }?.let {
                createCountersViewModel.createCounter(it)
            } ?: kotlin.run {
                binding.createCounterTextInputLayout.error = getString(R.string.no_name_error)
            }
        }
        binding.icClose.setOnClickListener {
            createCountersViewModel.navigateBack()
        }
        binding.textViewSeeExamples.setOnClickListener {
            createCountersViewModel.navigateToExamples()
        }
        observe(getSavedStateLiveData<String>(SELECTED_EXAMPLE_KEY)) {
            binding.createCounterTextInputEdit.setText(it)
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
                val throwable = createCounterFragmentState.throwable
                Log.e(TAG, throwable.message.orEmpty(), throwable)
            }
        }
    }

    private fun showErrorDialog() {
        SimpleDialogFactory.createDialog(
            requireContext(),
            title = getString(R.string.error_creating),
            message = getString(R.string.no_internet_message),
            positiveButton = getString(R.string.ok) to SimpleDialogFactory.noAction
        ).show()
    }

    private fun setSavingVisibility(btnInvisible: Boolean, progressBarInvisible: Boolean) {
        binding.saveProgressBar.invisible(progressBarInvisible)
        binding.btnSave.invisible(btnInvisible)
    }

    companion object {
        private val TAG = CreateCounterFragment::class.java.simpleName
    }
}