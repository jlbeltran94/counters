package com.example.countersapp.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.example.countersapp.data.api.models.CounterResponseModel
import com.example.countersapp.ui.models.Counter
import com.example.countersapp.domain.CounterMapper
import com.example.countersapp.ui.models.Example
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.mockito.Mockito
import java.io.File
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

object TestUtils {
    private var mapper: CounterMapper = CounterMapper()
    private val examples = listOf(
        Example(
            "Drinks",
            listOf("Cups of coffee", "Glasses of water", "Glasses of milk", "Cups of wine")
        ),
        Example(
            "Food",
            listOf(
                "Hot-dogs",
                "Cupcakes eaten",
                "Chicken salad",
                "Burgers",
                "Meat sandwich"
            )
        ),
        Example(
            "Hobbies",
            listOf("Songs played", "Goals on FIFA", "Kills in COD", "Warzone wins")
        ),
        Example(
            "Mis",
            listOf("Times sneezed", "Naps", "Day dreaming", "Lines of code", "App Crashes")
        )
    )

    fun getJson(path: String): String {
        val uri = this.javaClass.classLoader?.getResource(path)
        return uri?.let {
            val file = File(it.path)
            String(file.readBytes())
        }.orEmpty()
    }

    fun getCounterResponseModels(): List<CounterResponseModel> {
        val listType = object : TypeToken<List<CounterResponseModel>>() {}.type
        return Gson().fromJson(getJson("counters.json"), listType)
    }

    fun getCounters(): List<Counter> {
        return getCounterResponseModels().map {
            mapper.fromResponseModel(it)
        }
    }

    fun makeValidPath(path: String): String {
        return "/$path"
    }

    fun <T> mAnyObject(): T {
        return Mockito.any<T>()
    }

    fun getExamples() = examples
}

fun <T> LiveData<T>.getOrAwaitValue(
    time: Long = 2,
    timeUnit: TimeUnit = TimeUnit.SECONDS
): T {
    var data: T? = null
    val latch = CountDownLatch(1)
    val observer = object : Observer<T> {
        override fun onChanged(o: T?) {
            data = o
            latch.countDown()
            this@getOrAwaitValue.removeObserver(this)
        }
    }

    this.observeForever(observer)

    // Don't wait indefinitely if the LiveData is not set.
    if (!latch.await(time, timeUnit)) {
        throw TimeoutException("LiveData value was never set.")
    }

    @Suppress("UNCHECKED_CAST")
    return data as T
}
