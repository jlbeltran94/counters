package com.example.countersapp.domain

import com.example.countersapp.data.api.CountersService
import com.example.countersapp.data.api.models.CreateCounterRequestModel
import com.example.countersapp.data.api.models.ModifyCounterRequestModel
import com.example.countersapp.utils.TestUtils
import io.reactivex.rxjava3.core.Single
import org.junit.Test

import org.junit.Before
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class CountersInteractorTest {

    @Mock
    lateinit var countersService: CountersService
    @Mock
    lateinit var counterMapper: CounterMapper
    lateinit var countersInteractor: CountersInteractor
    private val counters by lazy { TestUtils.getCounters() }
    private val countersResponse by lazy { TestUtils.getCounterResponseModels() }
    private val id = "qwer"
    private val title = "test 1"

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        val modifyCounterRequestModel = ModifyCounterRequestModel(id)
        val createCounterRequestModel = CreateCounterRequestModel(title)
        `when`(countersService.getCounters()).thenReturn(Single.just(countersResponse))
        `when`(countersService.createCounter(createCounterRequestModel)).thenReturn(
            Single.just(
                countersResponse
            )
        )
        `when`(countersService.increaseCounter(modifyCounterRequestModel)).thenReturn(
            Single.just(
                countersResponse
            )
        )
        `when`(countersService.decreaseCounter(modifyCounterRequestModel)).thenReturn(
            Single.just(
                countersResponse
            )
        )
        `when`(countersService.deleteCounter(modifyCounterRequestModel)).thenReturn(
            Single.just(
                countersResponse
            )
        )
        `when`(counterMapper.fromResponseModel(countersResponse)).thenReturn(
            counters
        )
        countersInteractor = CountersInteractor(countersService, counterMapper)
    }

    @Test
    fun `get counters`() {
        countersInteractor.getCounters()
            .test()
            .assertNoErrors()
            .assertValue(counters)
            .dispose()
    }

    @Test
    fun createCounter() {
        countersInteractor.createCounter(title)
            .test()
            .assertNoErrors()
            .assertValue(counters)
            .dispose()
    }

    @Test
    fun deleteCounter() {
        countersInteractor.deleteCounter(id)
            .test()
            .assertNoErrors()
            .assertValue(counters)
            .dispose()
    }

    @Test
    fun increaseCounter() {
        countersInteractor.increaseCounter(id)
            .test()
            .assertNoErrors()
            .assertValue(counters)
            .dispose()
    }

    @Test
    fun decreaseCounter() {
        countersInteractor.decreaseCounter(id)
            .test()
            .assertNoErrors()
            .assertValue(counters)
            .dispose()
    }
}