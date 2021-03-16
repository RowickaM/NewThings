package com.rowicka.newthings.todoapp.taskdetail.data.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.rowicka.newthings.todoapp.data.Result
import com.rowicka.newthings.todoapp.data.Task
import com.rowicka.newthings.todoapp.data.source.TasksRepository
import kotlinx.coroutines.runBlocking

class FakeAndroidTestRepository : TasksRepository {

    var tasksServiceData: LinkedHashMap<String, Task> = LinkedHashMap()

    private var shouldReturnError = false

    private var observableTasks = MutableLiveData<Result<List<Task>>>()

    fun setReturnError(value: Boolean){
        shouldReturnError = value
    }

    override suspend fun getTasks(forceUpdate: Boolean): Result<List<Task>> {
        if (shouldReturnError){
            return Result.Error(Exception("Test exception"))
        }

        return Result.Success(tasksServiceData.values.toList())
    }

    override suspend fun refreshTasks() {
        observableTasks.value = getTasks()
    }

    override fun observeTasks(): LiveData<Result<List<Task>>> {
        runBlocking { refreshTasks() }
        return observableTasks
    }

    override suspend fun refreshTask(taskId: String) {
        refreshTasks()
    }

    override fun observeTask(taskId: String): LiveData<Result<Task>> {
        runBlocking { refreshTasks() }
        return observableTasks.map { tasks ->
            when(tasks) {
                is Result.Loading -> Result.Loading
                is Result.Error -> Result.Error(tasks.exception)
                is Result.Success -> {
                    val task = tasks.data.firstOrNull(){ it.id == taskId}
                        ?: return@map Result.Error(Exception("Not found"))
                    Result.Success(task)
                }
            }
        }
    }

    override suspend fun getTask(taskId: String, forceUpdate: Boolean): Result<Task> {
        if (shouldReturnError){
            return Result.Error(Exception("Test exception"))
        }

        tasksServiceData[taskId]?.let {
            return Result.Success(it)
        }

        return Result.Error(Exception("Could not find task!"))
    }

    override suspend fun saveTask(task: Task) {
        tasksServiceData[task.id] = task
    }

    override suspend fun completeTask(task: Task) {
        tasksServiceData[task.id] = task.copy(isCompleted = true)
    }

    override suspend fun completeTask(taskId: String) {
        throw NotImplementedError()
    }

    override suspend fun activateTask(task: Task) {
        tasksServiceData[task.id] = task.copy(isCompleted = false)
    }

    override suspend fun activateTask(taskId: String) {
        throw NotImplementedError()
    }

    override suspend fun clearCompletedTasks() {
        tasksServiceData = tasksServiceData.filterValues {
            !it.isCompleted
        } as LinkedHashMap<String, Task>
    }

    override suspend fun deleteAllTasks() {
        tasksServiceData.clear()
        refreshTasks()
    }

    override suspend fun deleteTask(taskId: String) {
        tasksServiceData.remove(taskId)
        refreshTasks()
    }

    fun addTask(vararg tasks: Task) {
        for (task in tasks) {
            tasksServiceData[task.id] = task
        }

        runBlocking { refreshTasks() }
    }
}