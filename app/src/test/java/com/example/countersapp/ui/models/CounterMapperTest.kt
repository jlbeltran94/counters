package com.example.countersapp.ui.models

import com.example.countersapp.utils.TestUtils
import org.junit.Test

import org.junit.Assert.*

class CounterMapperTest {
    private val counterMapper by lazy { CounterMapper() }
    private val counters = listOf(
        Counter("qwer", "test 1", 3),
        Counter("zxcv", "test 2", 0)
    )
    private val countersResponseModel = TestUtils.getCounterResponseModels()

    @Test
    fun `verify map a element`() {
        assertEquals(
            counters.first(),
            counterMapper.fromResponseModel(countersResponseModel.first())
        )
    }

    @Test
    fun `verify map a list of elements`() {
        assertEquals(counters, counterMapper.fromResponseModel(countersResponseModel))
    }

}