package com.example.countersapp.ui.counters

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.viewbinding.ViewBinding
import com.example.countersapp.databinding.FragmentCountersBinding
import com.example.countersapp.ui.counters.adapter.CountersAdapter
import com.example.countersapp.ui.counters.adapter.ItemActionsListener
import com.example.countersapp.ui.models.Counter
import com.example.countersapp.util.invisible
import com.example.countersapp.util.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CountersFragment : Fragment(), ItemActionsListener {

    private lateinit var binding: FragmentCountersBinding
    private val countersViewModel by viewModels<CountersViewModel>()
    private val viewStates: List<ViewBinding>
        get() = listOf(
            binding.errorEstate,
            binding.successEstate,
            binding.loadingEstate
        )

    private val countersAdapter by lazy { CountersAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCountersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        lifecycle.addObserver(countersViewModel)
        countersAdapter.listener = this
        binding.successEstate.countersRecycler.adapter = countersAdapter
        binding.errorEstate.button2.setOnClickListener {
            countersViewModel.getCounters()
        }
        countersViewModel.countersStateLiveData.observe(viewLifecycleOwner) {
            handleState(it)
        }
        countersViewModel.getCounters()
        binding.createCounterButton.setOnClickListener {
            countersViewModel.navigateToCreateCounter()
        }
        binding.searchBarView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                p0?.let { countersViewModel.searchCounter(it) }
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                p0?.let { countersViewModel.searchCounter(it) }
                return false
            }

        })
    }

    private fun handleState(countersFragmentState: CountersFragmentState) {
        binding.successEstate.swipeToRefreshCounters.isRefreshing = false
        when (countersFragmentState) {
            is CountersFragmentState.Loading -> {
                setViewStatesVisibility(binding.loadingEstate)
            }
            is CountersFragmentState.Success -> {
                handleSuccessState(countersFragmentState)
            }
            is CountersFragmentState.ErrorAction -> {
                createErrorActionDialog(countersFragmentState)
                Log.e("COUNTERS_FRAGMENT", countersFragmentState.throwable.message)
            }
            is CountersFragmentState.Search -> {
                handleSuccessState(CountersFragmentState.Success(countersFragmentState.data))
            }

            is CountersFragmentState.Error -> {
                setViewStatesVisibility(binding.errorEstate)
                Log.e("COUNTERS_FRAGMENT", countersFragmentState.throwable.message)
            }
        }
    }

    private fun handleSuccessState(countersFragmentState: CountersFragmentState.Success) {
        val data = countersFragmentState.data
        setViewStatesVisibility(binding.successEstate)
        val times = data.fold(0) { count, counter ->
            count + counter.count
        }
        binding.successEstate.numberOfCounters.text =
            if (data.isNotEmpty()) "${data.size} items" else ""
        binding.successEstate.totalTimes.text = if (data.isNotEmpty()) "$times times" else ""
        binding.successEstate.noDataGroup.visible(data.isEmpty())
        binding.successEstate.hasDataGroup.visible(data.isNotEmpty())
        binding.successEstate.swipeToRefreshCounters.setOnRefreshListener {
            countersViewModel.getCounters()
        }
        countersAdapter.counters = data
    }

    private fun setViewStatesVisibility(viewBinding: ViewBinding) {
        viewStates.forEach {
            it.visible(it == viewBinding)
        }
    }

    private fun createErrorActionDialog(errorAction: CountersFragmentState.ErrorAction) {
        val counter = errorAction.counter
        val expectedQty = when (errorAction.action) {
            CountersAction.INCREASE -> counter.count + 1
            CountersAction.DECREASE -> counter.count - 1
        }
        AlertDialog.Builder(requireContext()).create().apply {
            setTitle("Couldn't update the \"${counter.title}\" to $expectedQty ")
            setMessage("The internet connection appears to be offline")
            setButton(AlertDialog.BUTTON_NEGATIVE, "Retry") { _, _ ->
                when (errorAction.action) {
                    CountersAction.INCREASE -> countersViewModel.incCounter(counter)
                    CountersAction.DECREASE -> countersViewModel.decCounter(counter)
                }
            }
            setButton(AlertDialog.BUTTON_POSITIVE, "Dismiss") { _, _ -> }
            show()
        }
    }

    override fun onIncCounterAction(counter: Counter) {
        countersViewModel.incCounter(counter)
    }

    override fun onDecCounterAction(counter: Counter) {
        countersViewModel.decCounter(counter)
    }

    override fun onSelectionModeChanged(isSelectedEnabled: Boolean) {
        binding.searchBarViewContainer.invisible(isSelectedEnabled)
    }
}