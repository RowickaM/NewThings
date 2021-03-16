package com.rowicka.newthings.todoapp.taskdetail.data.source.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.filters.SmallTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.rowicka.newthings.todoapp.data.Task
import com.rowicka.newthings.todoapp.data.source.local.ToDoDatabase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4ClassRunner::class)
@SmallTest
class TasksDaoTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: ToDoDatabase

    @Before
    fun initDb() {
        // Using an in-memory database so that the information stored here disappears when the
        // process is killed.
        database = Room.inMemoryDatabaseBuilder(
            getApplicationContext(),
            ToDoDatabase::class.java
        ).build()
    }

    @After
    fun closeDb() = database.close()

    @Test
    fun insertTaskAndGetById() = runBlockingTest {
        // GIVEN - Insert a task.
        val task = Task("title", "description")
        database.taskDao().insertTask(task)

        // WHEN - Get the task by id from the database.
        val loaded = database.taskDao().getTaskById(task.id)

        // THEN - The loaded data contains the expected values.
        assertNotNull(loaded as Task)
        assertEquals(task.id, loaded.id)
        assertEquals(task.title, loaded.title)
        assertEquals(task.description, loaded.description)
        assertEquals(task.isCompleted, loaded.isCompleted)
    }

    @Test
    fun updateTaskAndGetById() = runBlockingTest {
        // 1. Insert a task into the DAO.
        val originalTask = Task("title", "description")
        database.taskDao().insertTask(originalTask)

        // 2. Update the task by creating a new task with the same ID but different attributes.
        val updatedTask = Task("new title", "new description", true, originalTask.id)
        database.taskDao().updateTask(updatedTask)

        // 3. Check that when you get the task by its ID, it has the updated values.
        val loaded = database.taskDao().getTaskById(originalTask.id)

        assertEquals(originalTask.id, loaded?.id)
        assertEquals("new title", loaded?.title)
        assertEquals("new description", loaded?.description)
        assertEquals(true, loaded?.isCompleted)
    }
}