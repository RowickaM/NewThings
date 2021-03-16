package com.rowicka.newthings.todoapp.taskdetail.data.source.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.rowicka.newthings.todoapp.data.source.local.TasksLocalDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import com.rowicka.newthings.todoapp.data.Result
import com.rowicka.newthings.todoapp.data.Task
import com.rowicka.newthings.todoapp.data.source.local.ToDoDatabase
import com.rowicka.newthings.todoapp.data.succeeded
import org.junit.Assert.assertEquals

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@MediumTest
class TasksLocalDataSourceTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var localDataSource: TasksLocalDataSource
    private lateinit var database: ToDoDatabase

    @Before
    fun setup(){
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            ToDoDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()

        localDataSource = TasksLocalDataSource(
            database.taskDao(),
            Dispatchers.Main
        )
    }

    @After
    fun cleanUp() {
        database.close()
    }

    @Test
    fun saveTask_retrievesTask() = runBlocking {
        val newTask = Task("title", "description", false)
        localDataSource.saveTask(newTask)

        val result = localDataSource.getTask(newTask.id)

        assertEquals(true, result.succeeded)
        result as Result.Success
        assertEquals("title", result.data.title)
        assertEquals("description", result.data.description)
        assertEquals(false, result.data.isCompleted)
    }

    @Test
    fun completeTask_retrievesTaskIsComplete() = runBlocking {
        val newTask = Task("title")
        localDataSource.saveTask(newTask)

        localDataSource.completeTask(newTask)
        val result = localDataSource.getTask(newTask.id)

        assertEquals(true, result.succeeded)
        result as Result.Success
        assertEquals("title", result.data.title)
        assertEquals(true, result.data.isCompleted)
    }
}