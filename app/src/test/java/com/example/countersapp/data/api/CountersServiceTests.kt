package com.example.countersapp.data.api

import com.example.countersapp.utils.TestUtils
import com.example.countersapp.utils.TrampolineSchedulerRule
import com.example.countersapp.data.api.models.CreateCounterRequestModel
import com.example.countersapp.data.api.models.ModifyCounterRequestModel
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.mockwebserver.MockWebServer
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


class CountersServiceTests {
    @get:Rule
    val mockServer by lazy { MockWebServer() }

    @get:Rule
    val trampolineSchedulerRule = TrampolineSchedulerRule()
    private val gson by lazy { Gson() }
    private lateinit var countersService: CountersService

    @Before
    fun setup() {
        countersService = Retrofit.Builder()
            .baseUrl(mockServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
            .create(CountersService::class.java)
        mockServer.dispatcher = WebServerDispatcher
    }

    @Test
    fun `verify counters are obtained`() {
        countersService.getCounters()
            .test()
            .apply { await() }
            .assertNoErrors()
            .assertValue(TestUtils.getCounterResponseModels())
            .dispose()
        val takeRequest = mockServer.takeRequest()
        Assert.assertEquals(
            TestUtils.makeValidPath(Endpoints.GET_COUNTERS),
            takeRequest.path
        )
        Assert.assertEquals(
            "GET",
            takeRequest.method
        )
    }

    @Test
    fun `verify counter creation is called as expected`() {
        val title = "test 2"
        val body = CreateCounterRequestModel(title)
        countersService.createCounter(body)
            .test()
            .apply { await() }
            .assertNoErrors()
            .assertValue(TestUtils.getCounterResponseModels())
            .dispose()
        val takeRequest = mockServer.takeRequest()
        Assert.assertEquals(
            TestUtils.makeValidPath(Endpoints.CREATE_COUNTER),
            takeRequest.path
        )
        Assert.assertEquals(
            gson.toJson(body),
            takeRequest.body.readUtf8()
        )
        Assert.assertEquals(
            "POST",
            takeRequest.method
        )
    }

    @Test
    fun `verify counter is deleted`() {
        val id = "qwer"
        val body = ModifyCounterRequestModel(id)
        countersService.deleteCounter(body)
            .test()
            .apply { await() }
            .assertNoErrors()
            .assertValue(TestUtils.getCounterResponseModels())
            .dispose()
        val takeRequest = mockServer.takeRequest()
        Assert.assertEquals(
            TestUtils.makeValidPath(Endpoints.DELETE_COUNTER),
            takeRequest.path
        )
        Assert.assertEquals(
            gson.toJson(body),
            takeRequest.body.readUtf8()
        )
        Assert.assertEquals(
            "DELETE",
            takeRequest.method
        )
    }

    @Test
    fun `verify counter is increased`() {
        val id = "qwer"
        val body = ModifyCounterRequestModel(id)
        countersService.increaseCounter(body)
            .test()
            .apply { await() }
            .assertNoErrors()
            .assertValue(TestUtils.getCounterResponseModels())
            .dispose()

        val takeRequest = mockServer.takeRequest()
        Assert.assertEquals(
            TestUtils.makeValidPath(Endpoints.INCREASE_COUNTER),
            takeRequest.path
        )
        Assert.assertEquals(
            gson.toJson(body),
            takeRequest.body.readUtf8()
        )
        Assert.assertEquals(
            "POST",
            takeRequest.method
        )
    }

    @Test
    fun `verify counter is decreased`() {
        val id = "qwer"
        val body = ModifyCounterRequestModel(id)
        countersService.decreaseCounter(body)
            .test()
            .apply { await() }
            .assertNoErrors()
            .assertValue(TestUtils.getCounterResponseModels())
            .dispose()

        val takeRequest = mockServer.takeRequest()
        Assert.assertEquals(
            TestUtils.makeValidPath(Endpoints.DECREASE_COUNTER),
            takeRequest.path
        )
        Assert.assertEquals(
            gson.toJson(body),
            takeRequest.body.readUtf8()
        )
        Assert.assertEquals(
            "POST",
            takeRequest.method
        )
    }
}