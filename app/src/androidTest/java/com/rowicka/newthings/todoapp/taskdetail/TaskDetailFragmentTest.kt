package com.rowicka.newthings.todoapp.taskdetail

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.rowicka.newthings.R
import com.rowicka.newthings.todoapp.data.Task
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@MediumTest
class TaskDetailFragmentTest {

    @Test
    fun activeTaskDetails_DisplayInUi() {
        val activeTask = Task("Title Active task", "AndroidX Rocks", false)
        val bundle = TaskDetailFragmentArgs(activeTask.id).toBundle()

        launchFragmentInContainer<TaskDetailFragment>(bundle, R.style.AppTheme)


    }
}