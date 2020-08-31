package com.example.countersapp.data.local

import com.example.countersapp.ui.models.Example
import io.reactivex.rxjava3.core.Single

interface ExamplesDataSource {
    fun getExamples(): Single<List<Example>>
}