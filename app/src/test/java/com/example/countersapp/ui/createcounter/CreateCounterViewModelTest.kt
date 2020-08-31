package com.example.countersapp.ui.createcounter

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.countersapp.domain.CountersInteractor
import com.example.countersapp.ui.navigation.NavigationDispatcher
import com.example.countersapp.utils.TestUtils
import com.example.countersapp.utils.TestUtils.mAnyObject
import com.example.countersapp.utils.TrampolineSchedulerRule
import com.example.countersapp.utils.getOrAwaitValue
import io.reactivex.rxjava3.core.Single
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.doNothing
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class CreateCounterViewModelTest {

    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val trampolineSchedulerRule = TrampolineSchedulerRule()

    @Mock
    lateinit var countersInteractor: CountersInteractor

    @Mock
    lateinit var navigator: NavigationDispatcher
    lateinit var createCounterViewModel: CreateCounterViewModel
    private val counters by lazy { TestUtils.getCounters() }
    private val title = "test 1"

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        Mockito.`when`(countersInteractor.createCounter(title)).thenReturn(
            Single.just(
                counters
            )
        )
        doNothing().`when`(navigator).emit(mAnyObject())
        createCounterViewModel = CreateCounterViewModel(countersInteractor, navigator)
    }

    @Test
    fun createCounter() {
        createCounterViewModel.createCounter(title)
        val state =
            createCounterViewModel.stateLiveData.getOrAwaitValue() as? CreateCounterFragmentState.Success
        assertNotNull(state)
        assertEquals(counters, state?.data)
    }

    @Test
    fun navigateBack() {
        createCounterViewModel.navigateBack()
        Mockito.verify(
            navigator,
            Mockito.times(1)
        ).emit(mAnyObject())
    }

    @Test
    fun navigateExample() {
        createCounterViewModel.navigateToExamples()
        Mockito.verify(
            navigator,
            Mockito.times(1)
        ).emit(mAnyObject())
    }
}