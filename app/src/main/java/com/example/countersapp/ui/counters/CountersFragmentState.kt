package com.example.countersapp.ui.counters

import com.example.countersapp.ui.models.Counter

sealed class CountersFragmentState {
    object Loading : CountersFragmentState()
    data class Error(val throwable: Throwable) : CountersFragmentState()
    data class ErrorAction(
        val counter: Counter,
        val action: CountersAction,
        val throwable: Throwable
    ) : CountersFragmentState()

    data class DeleteError(val throwable: Throwable) : CountersFragmentState()
    data class Success(val data: List<Counter>) : CountersFragmentState()
    data class Search(val data: List<Counter>) : CountersFragmentState()
}