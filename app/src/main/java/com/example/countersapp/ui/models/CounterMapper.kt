package com.example.countersapp.ui.models

import com.example.countersapp.data.api.models.CounterResponseModel
import javax.inject.Inject

interface CounterMapper {
    fun fromResponseModel(counterResponseModel: CounterResponseModel): Counter
    fun fromResponseModel(counterResponseModels: List<CounterResponseModel>): List<Counter>
}

class CounterMapperImp @Inject constructor() : CounterMapper {
    override fun fromResponseModel(counterResponseModel: CounterResponseModel): Counter {
        return with(counterResponseModel) {
            Counter(id, title, count)
        }
    }

    override fun fromResponseModel(counterResponseModels: List<CounterResponseModel>): List<Counter> {
        return counterResponseModels.map { fromResponseModel(it) }
    }
}