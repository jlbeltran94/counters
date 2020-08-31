package com.example.countersapp.ui.counters

import androidx.annotation.VisibleForTesting
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.countersapp.R
import com.example.countersapp.domain.CountersInteractor
import com.example.countersapp.ui.models.Counter
import com.example.countersapp.ui.navigation.NavigationDispatcher
import com.example.countersapp.util.Constants.EMPTY
import com.example.countersapp.util.applySchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.plusAssign
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.kotlin.toObservable

class CountersViewModel @ViewModelInject constructor(
    private val countersInteractor: CountersInteractor,
    private val navigator: NavigationDispatcher
) : ViewModel() {

    private var query = EMPTY
    private val composite = CompositeDisposable()
    private val _countersStateLiveData: MutableLiveData<CountersFragmentState> = MutableLiveData()
    val countersStateLiveData: LiveData<CountersFragmentState> = _countersStateLiveData
    private var cachedCounters: List<Counter> = listOf()

    init {
        getCounters()
    }

    fun getCounters(loading: Boolean = true) {
        if (loading) _countersStateLiveData.value = CountersFragmentState.Loading
        composite += countersInteractor.getCounters()
            .doOnSuccess { cachedCounters = it }
            .flatMapObservable { it.toObservable() }
            .filter { getFilterCondition(it) }
            .toList()
            .applySchedulers()
            .subscribeBy(
                onSuccess = {
                    _countersStateLiveData.value = CountersFragmentState.Success(it)
                }, onError = {
                    _countersStateLiveData.value = CountersFragmentState.Error(it)
                }
            )
    }

    fun searchCounter(query: String) {
        this.query = query
        composite += cachedCounters.toObservable()
            .filter { getFilterCondition(it) }
            .toList()
            .applySchedulers()
            .subscribeBy(
                onSuccess = {
                    if (it.isNotEmpty()) {
                        _countersStateLiveData.value = CountersFragmentState.Success(it)
                    } else {
                        _countersStateLiveData.value = CountersFragmentState.NoSearchResults
                    }
                }, onError = {
                    _countersStateLiveData.value = CountersFragmentState.NoSearchResults
                }
            )
    }

    fun incCounter(counter: Counter) {
        _countersStateLiveData.value = CountersFragmentState.LoadingAction
        composite += countersInteractor.increaseCounter(counter.id)
            .doOnSuccess { cachedCounters = it }
            .flatMapObservable { it.toObservable() }
            .filter { getFilterCondition(it) }
            .toList()
            .applySchedulers()
            .subscribeBy(
                onSuccess = {
                    _countersStateLiveData.value = CountersFragmentState.Success(it)
                }, onError = {
                    _countersStateLiveData.value =
                        CountersFragmentState.ErrorAction(counter, CounterAction.INCREASE, it)
                }
            )
    }

    fun decCounter(counter: Counter) {
        _countersStateLiveData.value = CountersFragmentState.LoadingAction
        composite += countersInteractor.decreaseCounter(counter.id)
            .doOnSuccess { cachedCounters = it }
            .flatMapObservable { it.toObservable() }
            .filter { getFilterCondition(it) }
            .toList()
            .applySchedulers()
            .subscribeBy(
                onSuccess = {
                    _countersStateLiveData.value = CountersFragmentState.Success(it)
                }, onError = {
                    _countersStateLiveData.value =
                        CountersFragmentState.ErrorAction(counter, CounterAction.DECREASE, it)
                }
            )
    }

    fun deleteCounters(counters: List<Counter>) {
        _countersStateLiveData.value = CountersFragmentState.LoadingAction
        composite += counters.toObservable()
            .flatMapSingle { countersInteractor.deleteCounter(it.id) }
            .lastOrError()
            .doOnSuccess { cachedCounters = it }
            .flatMapObservable { it.toObservable() }
            .filter { getFilterCondition(it) }
            .toList()
            .applySchedulers()
            .subscribeBy(
                onSuccess = {
                    if (query.isNotEmpty() && it.isEmpty()) {
                        _countersStateLiveData.value = CountersFragmentState.NoSearchResults
                    } else {
                        _countersStateLiveData.value = CountersFragmentState.Success(it)
                    }

                }, onError = {
                    _countersStateLiveData.value =
                        CountersFragmentState.DeleteError(it)
                }
            )
    }

    private fun getFilterCondition(counter: Counter) =
        query.isEmpty() || counter.title.startsWith(query, true)

    fun clearQuery() {
        query = EMPTY
    }

    @VisibleForTesting
    fun setQuery(query: String) {
        this.query = query
    }

    fun navigateToCreateCounter() {
        navigator.emit { it.navigate(R.id.action_countersFragment_to_createCounterFragment) }
    }

    override fun onCleared() {
        composite.clear()
        super.onCleared()
    }

}