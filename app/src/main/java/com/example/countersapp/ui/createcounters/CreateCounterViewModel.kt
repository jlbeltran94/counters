package com.example.countersapp.ui.createcounters

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.countersapp.domain.CountersInteractor
import com.example.countersapp.ui.navigation.NavigationDispatcher
import com.example.countersapp.util.applySchedulers
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
        navigator.emit { it.navigateUp() }
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