package com.rowicka.newthings

import android.app.Application
import com.rowicka.newthings.todoapp.ServiceLocator
import com.rowicka.newthings.todoapp.data.source.TasksRepository
import timber.log.Timber

class App : Application() {
    val tasksRepository: TasksRepository
        get() = ServiceLocator.provideTaskRepository(this)

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    }
}