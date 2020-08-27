package com.example.countersapp.ui.counters

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.countersapp.domain.CountersInteractor
import com.example.countersapp.ui.models.Counter
import com.example.countersapp.ui.navigation.Navigator
import com.example.countersapp.util.applySchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.plusAssign
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.kotlin.toObservable

class CountersViewModel @ViewModelInject constructor(
    private val countersInteractor: CountersInteractor,
    private val navigator: Navigator
) : ViewModel(), LifecycleObserver {

    private var query = ""
    private val composite = CompositeDisposable()
    private val _countersStateLiveData: MutableLiveData<CountersFragmentState> = MutableLiveData()
    val countersStateLiveData: LiveData<CountersFragmentState> = _countersStateLiveData
    private var cachedCounters: List<Counter> = listOf()

    fun getCounters() {
        _countersStateLiveData.value = CountersFragmentState.Loading
        composite += countersInteractor.getCounters()
            .doOnSuccess { cachedCounters = it }
            .flatMapObservable { it.toObservable() }
            .filter { query.isEmpty() || it.title.startsWith(query) }
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
            .filter { query.isEmpty() || it.title.startsWith(query) }
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
        composite += countersInteractor.increaseCounter(counter.id)
            .doOnSuccess { cachedCounters = it }
            .flatMapObservable { it.toObservable() }
            .filter { query.isEmpty() || it.title.startsWith(query) }
            .toList()
            .applySchedulers()
            .subscribeBy(
                onSuccess = {
                    _countersStateLiveData.value = CountersFragmentState.Success(it)
                }, onError = {
                    _countersStateLiveData.value =
                        CountersFragmentState.ErrorAction(counter, CountersAction.INCREASE, it)
                }
            )
    }

    fun decCounter(counter: Counter) {
        composite += countersInteractor.decreaseCounter(counter.id)
            .doOnSuccess { cachedCounters = it }
            .flatMapObservable { it.toObservable() }
            .filter { query.isEmpty() || it.title.startsWith(query) }
            .toList()
            .applySchedulers()
            .subscribeBy(
                onSuccess = {
                    _countersStateLiveData.value = CountersFragmentState.Success(it)
                }, onError = {
                    _countersStateLiveData.value =
                        CountersFragmentState.ErrorAction(counter, CountersAction.DECREASE, it)
                }
            )
    }

    fun deleteCounters(counters: List<Counter>) {
        composite += counters.toObservable()
            .flatMapSingle { countersInteractor.deleteCounter(it.id) }
            .lastOrError()
            .doOnSuccess { cachedCounters = it }
            .flatMapObservable { it.toObservable() }
            .filter { query.isEmpty() || it.title.startsWith(query) }
            .toList()
            .applySchedulers()
            .subscribeBy(
                onSuccess = {
                    _countersStateLiveData.value = CountersFragmentState.Success(it)
                }, onError = {
                    _countersStateLiveData.value =
                        CountersFragmentState.DeleteError(it)
                }
            )
    }

    fun clearQuery() {
        query = ""
    }

    fun navigateToCreateCounter() {
        navigator.navigateToCreateCounter()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        composite.clear()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        composite.dispose()
    }

}