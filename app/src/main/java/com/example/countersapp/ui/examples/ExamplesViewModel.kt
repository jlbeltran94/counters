package com.example.countersapp.ui.examples

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.countersapp.data.local.ExamplesDataSource
import com.example.countersapp.ui.models.Example
import com.example.countersapp.ui.navigation.NavigationDispatcher
import com.example.countersapp.util.Constants.SELECTED_EXAMPLE_KEY
import com.example.countersapp.util.applySchedulers
import com.example.countersapp.util.navigateBackWithResult
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.plusAssign
import io.reactivex.rxjava3.kotlin.subscribeBy

class ExamplesViewModel @ViewModelInject constructor(
    private val examplesDataSource: ExamplesDataSource,
    private val navigator: NavigationDispatcher
) : ViewModel() {

    private val _examplesLiveData: MutableLiveData<List<Example>> = MutableLiveData()
    val examplesLiveData: LiveData<List<Example>> = _examplesLiveData
    private val composite = CompositeDisposable()

    fun loadExamples() {
        composite += examplesDataSource.getExamples()
            .applySchedulers()
            .subscribeBy(
                onSuccess = {
                    _examplesLiveData.value = it
                }
            )
    }

    fun navBackWithSelectedExample(example: String) {
        navigator.emit {
            it.navigateBackWithResult(SELECTED_EXAMPLE_KEY, example)
        }
    }

    override fun onCleared() {
        composite.dispose()
        super.onCleared()
    }
}
