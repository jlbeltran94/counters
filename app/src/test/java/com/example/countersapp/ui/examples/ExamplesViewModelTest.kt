package com.example.countersapp.ui.examples

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.countersapp.data.local.ExamplesDataSource
import com.example.countersapp.ui.models.Example
import com.example.countersapp.ui.navigation.NavigationDispatcher
import com.example.countersapp.utils.TestUtils.getExamples
import com.example.countersapp.utils.TestUtils.mAnyObject
import com.example.countersapp.utils.TrampolineSchedulerRule
import com.example.countersapp.utils.getOrAwaitValue
import io.reactivex.rxjava3.core.Single
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ExamplesViewModelTest {

    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val trampolineSchedulerRule = TrampolineSchedulerRule()

    @Mock
    lateinit var examplesDataSource: ExamplesDataSource

    @Mock
    lateinit var navigator: NavigationDispatcher
    lateinit var examplesViewModel: ExamplesViewModel

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        Mockito.`when`(examplesDataSource.getExamples()).thenReturn(
            Single.just(
                getExamples()
            )
        )
        Mockito.doNothing().`when`(navigator).emit(mAnyObject())
        examplesViewModel = ExamplesViewModel(examplesDataSource, navigator)
    }

    @Test
    fun loadExamples() {
        examplesViewModel.loadExamples()
        val examples = examplesViewModel.examplesLiveData.getOrAwaitValue()
        Assert.assertEquals(getExamples(), examples)
    }

    @Test
    fun navBackWithSelectedExample() {
        val selected = "selected"
        examplesViewModel.navBackWithSelectedExample(selected)
        Mockito.verify(
            navigator,
            Mockito.times(1)
        ).emit(mAnyObject())
    }
}