package com.example.countersapp.ui.counters

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.countersapp.domain.CountersInteractor
import com.example.countersapp.ui.navigation.NavigationDispatcher
import com.example.countersapp.utils.TestUtils
import com.example.countersapp.utils.TrampolineSchedulerRule
import com.example.countersapp.utils.getOrAwaitValue
import io.reactivex.rxjava3.core.Single
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class CountersViewModelTest {

    @Mock
    lateinit var countersInteractor: CountersInteractor

    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val trampolineSchedulerRule = TrampolineSchedulerRule()

    @Mock
    lateinit var navigationDispatcher: NavigationDispatcher
    lateinit var countersViewModel: CountersViewModel

    private val counters by lazy { TestUtils.getCounters() }
    private val id1 = "qwer"
    private val id2 = "zxcv"
    private val title = "test 1"

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        `when`(countersInteractor.getCounters()).thenReturn(Single.just(counters))
        `when`(countersInteractor.increaseCounter(id1)).thenReturn(
            Single.just(
                counters
            )
        )
        `when`(countersInteractor.decreaseCounter(id1)).thenReturn(
            Single.just(
                counters
            )
        )
        `when`(countersInteractor.deleteCounter(id1)).thenReturn(
            Single.just(
                counters
            )
        )
        `when`(countersInteractor.deleteCounter(id2)).thenReturn(
            Single.just(
                counters
            )
        )
        doNothing().`when`(navigationDispatcher).emit(anyObject())
        countersViewModel = CountersViewModel(countersInteractor, navigationDispatcher)
    }

    @After
    fun teardown() {
        countersViewModel.clearQuery()
    }

    @Test
    fun `get counters when query is empty`() {
        countersViewModel.getCounters()
        val state =
            countersViewModel.countersStateLiveData.getOrAwaitValue() as? CountersFragmentState.Success
        assertNotNull(state)
        assertEquals(counters, state?.data)
    }

    @Test
    fun `get counters when query is not empty`() {
        val query = title
        countersViewModel.setQuery(query)
        countersViewModel.getCounters()
        val state =
            countersViewModel.countersStateLiveData.getOrAwaitValue() as? CountersFragmentState.Success
        val filteredCounters = counters.filter { it.title == query }
        assertNotNull(state)
        assertEquals(filteredCounters, state?.data)
    }

    @Test
    fun searchCounter() {
        val query = title
        countersViewModel.getCounters()
        countersViewModel.searchCounter(query)
        val state =
            countersViewModel.countersStateLiveData.getOrAwaitValue() as? CountersFragmentState.Success
        val filteredCounters = counters.filter { it.title == query }
        assertNotNull(state)
        assertEquals(filteredCounters, state?.data)
    }

    @Test
    fun incCounter() {
        countersViewModel.incCounter(counters.first())
        val state =
            countersViewModel.countersStateLiveData.getOrAwaitValue() as? CountersFragmentState.Success
        assertNotNull(state)
        assertEquals(counters, state?.data)
    }

    @Test
    fun decCounter() {
        countersViewModel.decCounter(counters.first())
        val state =
            countersViewModel.countersStateLiveData.getOrAwaitValue() as? CountersFragmentState.Success
        assertNotNull(state)
        assertEquals(counters, state?.data)
    }

    @Test
    fun deleteCounters() {
        countersViewModel.deleteCounters(counters)
        val state =
            countersViewModel.countersStateLiveData.getOrAwaitValue() as? CountersFragmentState.Success
        assertNotNull(state)
        assertEquals(counters, state?.data)
    }

    @Test
    fun navigateToCreateCounter() {
        countersViewModel.navigateToCreateCounter()
        verify(
            navigationDispatcher,
            times(1)
        ).emit(anyObject())
    }

    private fun <T> anyObject(): T {
        return Mockito.any<T>()
    }

}