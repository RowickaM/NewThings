package com.rowicka.newthings.todoapp.tasks

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.MediumTest
import com.rowicka.newthings.R
import com.rowicka.newthings.todoapp.ServiceLocator
import com.rowicka.newthings.todoapp.data.Task
import com.rowicka.newthings.todoapp.data.source.TasksRepository
import com.rowicka.newthings.todoapp.taskdetail.data.source.FakeAndroidTestRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*

@MediumTest
@ExperimentalCoroutinesApi
class TasksFragmentTest {
    private lateinit var repository: TasksRepository

    private val task1 = Task("Title 1", "Description 1", false, "id1")
    private val task2 = Task("Title 2", "Description 2", true, "id2")

    private lateinit var navController: NavController

    @Before
    fun initRepository() = runBlockingTest {
        repository = FakeAndroidTestRepository()
        ServiceLocator.tasksRepository = repository

        repository.saveTask(task1)
        repository.saveTask(task2)

        val scenario = launchFragmentInContainer<TasksFragment>(Bundle(), R.style.AppTheme)
        navController = mock(NavController::class.java)
        scenario.onFragment {
            Navigation.setViewNavController(it.view!!, navController)
        }
    }

    @After
    fun cleanupDb() = runBlockingTest {
        ServiceLocator.resetRepository()
    }

    @Test
    fun clickTask_navigateToDetailFragment() {
        onView(withId(R.id.tasks_list))
            .perform(
                RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(
                    hasDescendant(withText(task1.title)),
                    click()
                )
            )

        //then
        verify(navController, times(1)).navigate(TasksFragmentDirections.actionTasksFragmentToTaskDetailFragment(task1.id))
    }

    @Test
    fun clickAddTaskButton_navigateToAddEditFragment(){
        val titleNewTask = ApplicationProvider.getApplicationContext<Context>().resources.getString(R.string.add_task)
        val action = TasksFragmentDirections.actionTasksFragmentToAddEditTaskFragment(null, titleNewTask)

        onView(withId(R.id.add_task_fab))
            .perform(click())

        verify(navController, times(1)).navigate(action)
    }
}