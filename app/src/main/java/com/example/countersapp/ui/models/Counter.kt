package com.example.countersapp.ui.models

import com.example.countersapp.data.api.models.CounterResponseModel

data class Counter(
    val id: String,
    val title: String,
    val count: Int
) {
    companion object {
        fun fromResponseModel(counterResponseModel: CounterResponseModel): Counter {
            return with(counterResponseModel) {
                Counter(id, title, count)
            }
        }

        fun fromResponseModel(counterResponseModels: List<CounterResponseModel>): List<Counter> {
            return counterResponseModels.map { fromResponseModel(it) }
        }
    }
}