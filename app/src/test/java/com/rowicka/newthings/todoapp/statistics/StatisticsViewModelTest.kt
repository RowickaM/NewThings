package com.rowicka.newthings.todoapp.statistics

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.rowicka.newthings.MainCoroutineRule
import com.rowicka.newthings.todoapp.data.source.FakeTestRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule

class StatisticsViewModelTest{
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: StatisticsViewModel

    private lateinit var tasksRepository: FakeTestRepository

    @Before
    fun setupStatisticViewModel(){
        tasksRepository = FakeTestRepository()

        viewModel = StatisticsViewModel(tasksRepository)
    }
}