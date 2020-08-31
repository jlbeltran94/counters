package com.example.countersapp.ui.createcounter

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.countersapp.R
import com.example.countersapp.domain.CountersInteractor
import com.example.countersapp.ui.models.Counter
import com.example.countersapp.ui.navigation.NavigationDispatcher
import com.example.countersapp.util.Constants
import com.example.countersapp.util.applySchedulers
import com.example.countersapp.util.navigateBackWithResult
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.plusAssign
import io.reactivex.rxjava3.kotlin.subscribeBy

class CreateCounterViewModel @ViewModelInject constructor(
    private val countersInteractor: CountersInteractor,
    private val navigator: NavigationDispatcher
) : ViewModel() {

    private val composite = CompositeDisposable()
    private val _stateLiveData: MutableLiveData<CreateCounterFragmentState> =
        MutableLiveData()
    val stateLiveData: LiveData<CreateCounterFragmentState> = _stateLiveData

    fun createCounter(title: String) {
        _stateLiveData.value = CreateCounterFragmentState.Loading
        composite += countersInteractor.createCounter(title)
            .applySchedulers()
            .subscribeBy(
                onSuccess = {
                    _stateLiveData.value = CreateCounterFragmentState.Success(it)
                }, onError = {
                    _stateLiveData.value = CreateCounterFragmentState.Error(it)
                }
            )
    }

    fun navigateBack() {
        navigator.emit { it.popBackStack() }
    }

    fun navigateBack(counters: List<Counter>) {
        navigator.emit { it.navigateBackWithResult(Constants.COUNTERS_KEY, counters) }
    }

    fun navigateToExamples() {
        navigator.emit { it.navigate(R.id.examplesFragment) }
    }

    override fun onCleared() {
        composite.clear()
        super.onCleared()
    }
}