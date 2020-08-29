package com.example.countersapp.data.api

import com.example.countersapp.utils.TestUtils
import com.example.countersapp.utils.TestUtils.makeValidPath
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest

object WebServerDispatcher : Dispatcher() {
    @Throws(InterruptedException::class)
    override fun dispatch(request: RecordedRequest): MockResponse {
        return when (request.path) {
            makeValidPath(Endpoints.GET_COUNTERS),
            makeValidPath(Endpoints.CREATE_COUNTER),
            makeValidPath(Endpoints.DELETE_COUNTER),
            makeValidPath(Endpoints.DECREASE_COUNTER),
            makeValidPath(Endpoints.INCREASE_COUNTER) -> {
                MockResponse()
                    .setResponseCode(200)
                    .addHeader("Content-Type", "application/json; charset=utf-8")
                    .setBody(TestUtils.getJson("counters.json"))
            }
            else -> MockResponse().setResponseCode(404)
        }
    }
}