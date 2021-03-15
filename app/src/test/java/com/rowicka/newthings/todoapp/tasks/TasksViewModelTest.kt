package com.rowicka.newthings.todoapp.tasks

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.rowicka.newthings.getOrAwaitValue
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TasksViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: TasksViewModel

    @Before
    fun setup() {
        viewModel = TasksViewModel(ApplicationProvider.getApplicationContext())
    }

    @Test
    fun addNewTask_setsNewTaskEvent() {
        viewModel.addNewTask()

        val value = viewModel.newTaskEvent.getOrAwaitValue()
        assertNotNull(value.getContentIfNotHandled())
    }

    @Test
    fun setFilterAllTasks_tasksAddViewVisible() {
        viewModel.setFiltering(TasksFilterType.ALL_TASKS)

        val value = viewModel.tasksAddViewVisible.getOrAwaitValue()
        assertEquals(true, value)
    }
}