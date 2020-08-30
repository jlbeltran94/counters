package com.example.countersapp.ui.models

import com.example.countersapp.data.api.models.CounterResponseModel
import javax.inject.Inject

class CounterMapper @Inject constructor() {
    fun fromResponseModel(counterResponseModel: CounterResponseModel): Counter {
        return with(counterResponseModel) {
            Counter(id, title, count)
        }
    }

    fun fromResponseModel(counterResponseModels: List<CounterResponseModel>): List<Counter> {
        return counterResponseModels.map { fromResponseModel(it) }
    }
}