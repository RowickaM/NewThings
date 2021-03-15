package com.rowicka.newthings.todoapp

import android.content.Context
import androidx.room.Room
import com.rowicka.newthings.todoapp.data.source.DefaultTasksRepository
import com.rowicka.newthings.todoapp.data.source.TasksDataSource
import com.rowicka.newthings.todoapp.data.source.TasksRepository
import com.rowicka.newthings.todoapp.data.source.local.TasksLocalDataSource
import com.rowicka.newthings.todoapp.data.source.local.ToDoDatabase
import com.rowicka.newthings.todoapp.data.source.remote.TasksRemoteDataSource

object ServiceLocator {

    private var database: ToDoDatabase? = null

    @Volatile
    var tasksRepository: TasksRepository? = null

    fun provideTaskRepository(context: Context): TasksRepository {
        synchronized(this) {
            return tasksRepository ?: createTasksRepository(context)
        }
    }

    private fun createTasksRepository(context: Context): TasksRepository {
        val newRepo =
            DefaultTasksRepository(TasksRemoteDataSource, createTaskLocalDataSource(context))
        tasksRepository = newRepo
        return newRepo
    }

    private fun createTaskLocalDataSource(context: Context): TasksDataSource {
        val database = database ?: createDataBase(context)
        return TasksLocalDataSource(database.taskDao())
    }

    private fun createDataBase(context: Context): ToDoDatabase {
        val result = Room.databaseBuilder(
            context.applicationContext,
            ToDoDatabase::class.java, "Tasks.db"
        ).build()
        database = result
        return result
    }
}