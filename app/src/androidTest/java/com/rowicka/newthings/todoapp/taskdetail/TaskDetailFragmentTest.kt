package com.rowicka.newthings.todoapp.taskdetail

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.rowicka.newthings.R
import com.rowicka.newthings.todoapp.ServiceLocator
import com.rowicka.newthings.todoapp.data.Task
import com.rowicka.newthings.todoapp.data.source.TasksRepository
import com.rowicka.newthings.todoapp.taskdetail.data.source.FakeAndroidTestRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.Matchers.not
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@MediumTest
@ExperimentalCoroutinesApi
class TaskDetailFragmentTest {

    private lateinit var repository: TasksRepository

    @Before
    fun initRepository() {
        repository = FakeAndroidTestRepository()
        ServiceLocator.tasksRepository = repository
    }

    @After
    fun cleanupDb() = runBlockingTest {
        ServiceLocator.resetRepository()
    }

    @Test
    fun activeTaskDetails_DisplayInUi() = runBlockingTest {
        val activeTask = Task("Title Active task", "AndroidX Rocks", false)

        repository.saveTask(activeTask)

        val bundle = TaskDetailFragmentArgs(activeTask.id).toBundle()
        launchFragmentInContainer<TaskDetailFragment>(bundle, R.style.AppTheme)

        //check title task
        onView(withId(R.id.task_detail_title_text)).apply {
            check(matches(isDisplayed()))
            check(matches(withText(activeTask.title)))
        }

        //check description task
        onView(withId(R.id.task_detail_description_text)).apply {
            check(matches(isDisplayed()))
            check(matches(withText(activeTask.description)))
        }

        //check checkbox task
        onView(withId(R.id.task_detail_complete_checkbox)).apply {
            check(matches(isDisplayed()))
            check(matches(not(isChecked())))
        }
    }
}