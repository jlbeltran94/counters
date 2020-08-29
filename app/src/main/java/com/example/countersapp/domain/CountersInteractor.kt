package com.example.countersapp.domain

import com.example.countersapp.data.api.CountersService
import com.example.countersapp.data.api.models.CreateCounterRequestModel
import com.example.countersapp.data.api.models.ModifyCounterRequestModel
import com.example.countersapp.ui.models.Counter
import com.example.countersapp.ui.models.CounterMapper
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class CountersInteractor @Inject constructor(
    private val countersService: CountersService,
    private val counterMapper: CounterMapper
) {

    fun getCounters(): Single<List<Counter>> {
        return countersService.getCounters().map {
            counterMapper.fromResponseModel(it)
        }
    }

    fun createCounter(title: String): Single<List<Counter>> {
        return countersService.createCounter(CreateCounterRequestModel(title)).map {
            counterMapper.fromResponseModel(it)
        }
    }

    fun deleteCounter(id: String): Single<List<Counter>> {
        return countersService.deleteCounter(ModifyCounterRequestModel(id)).map {
            counterMapper.fromResponseModel(it)
        }
    }

    fun increaseCounter(id: String): Single<List<Counter>> {
        return countersService.increaseCounter(ModifyCounterRequestModel(id)).map {
            counterMapper.fromResponseModel(it)
        }
    }

    fun decreaseCounter(id: String): Single<List<Counter>> {
        return countersService.decreaseCounter(ModifyCounterRequestModel(id)).map {
            counterMapper.fromResponseModel(it)
        }
    }
}