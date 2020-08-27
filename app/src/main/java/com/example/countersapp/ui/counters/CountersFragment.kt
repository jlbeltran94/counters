package com.example.countersapp.ui.counters

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewbinding.ViewBinding
import com.example.countersapp.databinding.FragmentCountersBinding
import com.example.countersapp.ui.counters.adapter.CountersAdapter
import com.example.countersapp.ui.counters.adapter.ItemActionsListener
import com.example.countersapp.ui.models.Counter
import com.example.countersapp.util.ButtonAction
import com.example.countersapp.util.SimpleDialogFactory
import com.example.countersapp.util.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_counters.view.*
import kotlinx.android.synthetic.main.fragment_create_counter.view.icClose
import kotlinx.android.synthetic.main.toolbar_selecting_counters.view.*

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
    private var isSearching = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCountersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupCountersAdapter()
        setupViewModel()
        setupErrorState()
        initListeners()
        setupSearchView()
        setupSelectingToolbar()
        countersViewModel.getCounters()
    }

    private fun setupViewModel() {
        lifecycle.addObserver(countersViewModel)
        countersViewModel.countersStateLiveData.observe(viewLifecycleOwner) {
            handleState(it)
        }
    }

    private fun initListeners() {
        binding.createCounterButton.setOnClickListener {
            countersViewModel.navigateToCreateCounter()
        }
    }

    private fun setupErrorState() {
        binding.errorEstate.retryButton.setOnClickListener {
            countersViewModel.getCounters()
        }
    }

    private fun setupCountersAdapter() {
        countersAdapter.isSelectionEnabled = false
        countersAdapter.listener = this
        binding.successEstate.countersRecycler.adapter = countersAdapter
    }

    private fun setupSearchView() {
        binding.searchBarView.setOnQueryTextFocusChangeListener { _, hasFocus ->
            isSearching = hasFocus
        }
        binding.searchBarView.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (isSearching) countersViewModel.searchCounter(query)
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if (isSearching) countersViewModel.searchCounter(newText)
                return false
            }
        })
    }

    private fun setupSelectingToolbar() {
        binding.toolbar.toolbarSelectingCounters.icClose.setOnClickListener {
            countersAdapter.isSelectionEnabled = false
        }
        binding.toolbar.toolbarSelectingCounters.shareCountersBtn.setOnClickListener {
            startActivity(getShareIntent())
        }
        binding.toolbar.toolbarSelectingCounters.deleteCountersBtn.setOnClickListener {
            onClickDeleteCounters()
        }
    }

    private fun onClickDeleteCounters() {
        val countersToDelete = countersAdapter.getSelectedCounters()
        val countersToDeleteTitle = countersToDelete.joinToString { "\"${it.title}\"" }
        val deleteAction: ButtonAction = { _, _ ->
            countersViewModel.deleteCounters(countersToDelete)
        }
        SimpleDialogFactory.createDialog(
            requireContext(),
            message = "Delete $countersToDeleteTitle?",
            positiveButton = "Delete" to deleteAction,
            negativeButton = "Cancel" to SimpleDialogFactory.noAction
        ).show()
    }

    private fun getShareIntent(): Intent {
        val data = countersAdapter.getSelectedCounters().joinToString {
            "${it.count}x${it.title}"
        }
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, data)
            type = "text/plain"
        }
        return Intent.createChooser(sendIntent, data)

    }

    private fun handleState(state: CountersFragmentState) {
        binding.successEstate.swipeToRefreshCounters.isRefreshing = false
        when (state) {
            is CountersFragmentState.Loading -> {
                setViewStatesVisibility(binding.loadingEstate)
            }
            is CountersFragmentState.Success -> {
                handleSuccessState(state)
            }
            is CountersFragmentState.ErrorAction -> {
                createErrorActionDialog(state)
                Log.e("COUNTERS_FRAGMENT", state.throwable.message)
            }
            is CountersFragmentState.Search -> {
                handleSuccessState(CountersFragmentState.Success(state.data))
            }
            is CountersFragmentState.DeleteError -> {
                showErrorDeletingDialog()
                Log.e("COUNTERS_FRAGMENT", state.throwable.message)
            }
            is CountersFragmentState.Error -> {
                setViewStatesVisibility(binding.errorEstate)
                Log.e("COUNTERS_FRAGMENT", state.throwable.message)
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
        val retryAction: ButtonAction = { _, _ ->
            when (errorAction.action) {
                CountersAction.INCREASE -> countersViewModel.incCounter(counter)
                CountersAction.DECREASE -> countersViewModel.decCounter(counter)
            }
        }
        SimpleDialogFactory.createDialog(
            requireContext(),
            title = "Couldn't update the \"${counter.title}\" to $expectedQty",
            message = "The internet connection appears to be offline",
            positiveButton = "Dismiss" to SimpleDialogFactory.noAction,
            negativeButton = "Retry" to retryAction
        ).show()
    }

    private fun showErrorDeletingDialog() {
        SimpleDialogFactory.createDialog(
            requireContext(),
            message = "Couldn't delete the counter",
            positiveButton = "Ok" to SimpleDialogFactory.noAction
        ).show()
    }

    override fun onIncCounterAction(counter: Counter) {
        countersViewModel.incCounter(counter)
    }

    override fun onDecCounterAction(counter: Counter) {
        countersViewModel.decCounter(counter)
    }

    override fun onSelectionModeChanged(isSelectedEnabled: Boolean) {
        binding.toolbar.searchBarContainer.visible(isSelectedEnabled.not())
        binding.toolbar.toolbarSelectingCounters.visible(isSelectedEnabled)
    }

    override fun onSelectionChanges(selectedItems: Int) {
        binding.toolbar.toolbarSelectingCounters.selectedCounters.text = "$selectedItems selected"
    }
}