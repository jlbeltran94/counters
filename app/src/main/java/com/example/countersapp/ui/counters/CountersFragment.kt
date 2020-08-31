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
import com.example.countersapp.R
import com.example.countersapp.databinding.FragmentCountersBinding
import com.example.countersapp.ui.counters.adapters.CountersAdapter
import com.example.countersapp.ui.counters.listeners.ItemActionsListener
import com.example.countersapp.ui.models.Counter
import com.example.countersapp.util.ButtonAction
import com.example.countersapp.util.Constants.EMPTY
import com.example.countersapp.util.SimpleDialogFactory
import com.example.countersapp.util.observe
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
            binding.errorState,
            binding.successState,
            binding.loadingState,
            binding.noSearchResultsState
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
        observe(countersViewModel.countersStateLiveData) {
            handleState(it)
        }
    }

    private fun initListeners() {
        binding.createCounterButton.setOnClickListener {
            countersViewModel.navigateToCreateCounter()
            clearSearch()
        }
    }

    private fun clearSearch() {
        countersViewModel.clearQuery()
        binding.toolbar.searchBarView.setQuery(EMPTY, false)
        binding.toolbar.searchBarView.clearFocus()
    }

    private fun setupErrorState() {
        binding.errorState.retryButton.setOnClickListener {
            countersViewModel.getCounters()
        }
    }

    private fun setupCountersAdapter() {
        countersAdapter.isSelectionEnabled = false
        countersAdapter.listener = this
        binding.successState.countersRecycler.adapter = countersAdapter
    }

    private fun setupSearchView() {
        binding.searchBarView.findViewById<View>(androidx.appcompat.R.id.search_close_btn)
            .setOnClickListener {
                isSearching = true
                clearSearch()
            }

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
        val countersToDeleteTitle = countersToDelete.joinToString {
            getString(R.string.quoted_string, it.title)
        }
        val deleteAction: ButtonAction = { _, _ ->
            countersViewModel.deleteCounters(countersToDelete)
        }
        SimpleDialogFactory.createDialog(
            requireContext(),
            message = getString(R.string.delete_counter_alert_title, countersToDeleteTitle),
            positiveButton = getString(R.string.delete) to deleteAction,
            negativeButton = getString(R.string.cancel) to SimpleDialogFactory.noAction
        ).show()
    }

    private fun getShareIntent(): Intent {
        val data = countersAdapter.getSelectedCounters().joinToString {
            getString(R.string.share_counter, it.count, it.title)
        }
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, data)
            type = getString(R.string.plain_text)
        }
        return Intent.createChooser(sendIntent, data)

    }

    private fun handleState(state: CountersFragmentState) {
        binding.successState.swipeToRefreshCounters.isRefreshing = false
        when (state) {
            is CountersFragmentState.Loading -> {
                setViewStatesVisibility(binding.loadingState)
            }
            is CountersFragmentState.Success -> {
                handleSuccessState(state)
            }
            is CountersFragmentState.ErrorAction -> {
                createErrorActionDialog(state)
                Log.e(TAG, state.throwable.message.orEmpty(), state.throwable)
            }
            is CountersFragmentState.NoSearchResults -> {
                countersAdapter.counters = emptyList()
                setViewStatesVisibility(binding.noSearchResultsState)
            }
            is CountersFragmentState.DeleteError -> {
                showErrorDeletingDialog()
                Log.e(TAG, state.throwable.message.orEmpty(), state.throwable)
            }
            is CountersFragmentState.Error -> {
                setViewStatesVisibility(binding.errorState)
                Log.e(TAG, state.throwable.message.orEmpty(), state.throwable)
            }
        }
    }

    private fun handleSuccessState(countersFragmentState: CountersFragmentState.Success) {
        val data = countersFragmentState.data
        setViewStatesVisibility(binding.successState)
        val times = data.fold(0) { count, counter ->
            count + counter.count
        }
        binding.successState.numberOfCounters.text =
            resources.getQuantityString(R.plurals.items_found, data.size, data.size)
                .takeIf { data.isNotEmpty() }
                .orEmpty()

        binding.successState.totalTimes.text =
            resources.getQuantityString(R.plurals.count_times, times, times)
                .takeIf { data.isNotEmpty() }
                .orEmpty()

        binding.successState.noDataGroup.visible(data.isEmpty())
        binding.successState.hasDataGroup.visible(data.isNotEmpty())
        binding.successState.swipeToRefreshCounters.setOnRefreshListener {
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
            CounterAction.INCREASE -> counter.count + 1
            CounterAction.DECREASE -> counter.count - 1
        }
        val retryAction: ButtonAction = { _, _ ->
            when (errorAction.action) {
                CounterAction.INCREASE -> countersViewModel.incCounter(counter)
                CounterAction.DECREASE -> countersViewModel.decCounter(counter)
            }
        }
        SimpleDialogFactory.createDialog(
            requireContext(),
            title = getString(R.string.error_update_counter, counter.title, expectedQty),
            message = getString(R.string.no_internet_message),
            positiveButton = getString(R.string.dismiss) to SimpleDialogFactory.noAction,
            negativeButton = getString(R.string.retry) to retryAction
        ).show()
    }

    private fun showErrorDeletingDialog() {
        SimpleDialogFactory.createDialog(
            requireContext(),
            message = getString(R.string.error_delete),
            positiveButton = getString(R.string.ok) to SimpleDialogFactory.noAction
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
        binding.toolbar.toolbarSelectingCounters.selectedCounters.text =
            getString(R.string.items_selected, selectedItems)
    }

    companion object {
        private val TAG = CountersFragment::class.java.simpleName
    }
}