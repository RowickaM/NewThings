package com.rowicka.newthings.todoapp.tasks

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.rowicka.newthings.getOrAwaitValue
import com.rowicka.newthings.todoapp.data.Task
import com.rowicka.newthings.todoapp.data.source.FakeTestRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test

//pobranie contextu z roboletric
//@RunWith(AndroidJUnit4::class)
class TasksViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var tasksRepository: FakeTestRepository

    private lateinit var viewModel: TasksViewModel

    @Before
    fun setup() {
        //pobranie contextu z roboletric
//        viewModel = TasksViewModel(ApplicationProvider.getApplicationContext())

        tasksRepository = FakeTestRepository()
        val task1 = Task("Title1", "Description1")
        val task2 = Task("Title2", "Description2")
        val task3 = Task("Title3", "Description3")
        tasksRepository.addTask(task1, task2, task3)

        viewModel = TasksViewModel(tasksRepository)
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