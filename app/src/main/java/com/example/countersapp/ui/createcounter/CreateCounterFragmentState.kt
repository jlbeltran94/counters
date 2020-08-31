package com.example.countersapp.ui.createcounter

import com.example.countersapp.ui.models.Counter

sealed class CreateCounterFragmentState {
    object Loading : CreateCounterFragmentState()
    data class Error(val throwable: Throwable) : CreateCounterFragmentState()
    data class Success(val data: List<Counter>) : CreateCounterFragmentState()
}