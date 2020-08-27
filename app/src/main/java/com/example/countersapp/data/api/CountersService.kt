package com.example.countersapp.data.api

import com.example.countersapp.data.api.Endpoints.CREATE_COUNTER
import com.example.countersapp.data.api.Endpoints.DECREASE_COUNTER
import com.example.countersapp.data.api.Endpoints.DELETE_COUNTER
import com.example.countersapp.data.api.Endpoints.GET_COUNTERS
import com.example.countersapp.data.api.Endpoints.INCREASE_COUNTER
import com.example.countersapp.data.api.models.CounterResponseModel
import com.example.countersapp.data.api.models.CreateCounterRequestModel
import com.example.countersapp.data.api.models.ModifyCounterRequestModel
import io.reactivex.rxjava3.core.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.POST

interface CountersService {

    @GET(GET_COUNTERS)
    fun getCounters(): Single<List<CounterResponseModel>>

    @POST(CREATE_COUNTER)
    fun createCounter(
        @Body createCounterRequestModel: CreateCounterRequestModel
    ): Single<List<CounterResponseModel>>

    @HTTP(method = "DELETE", path = DELETE_COUNTER, hasBody = true)
    fun deleteCounter(
        @Body modifyCounterRequestModel: ModifyCounterRequestModel
    ): Single<List<CounterResponseModel>>

    @POST(INCREASE_COUNTER)
    fun increaseCounter(
        @Body modifyCounterRequestModel: ModifyCounterRequestModel
    ): Single<List<CounterResponseModel>>

    @POST(DECREASE_COUNTER)
    fun decreaseCounter(
        @Body modifyCounterRequestModel: ModifyCounterRequestModel
    ): Single<List<CounterResponseModel>>
}