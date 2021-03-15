package com.rowicka.newthings.todoapp.taskdetail.data.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.rowicka.newthings.todoapp.data.Result
import com.rowicka.newthings.todoapp.data.Task
import com.rowicka.newthings.todoapp.data.source.TasksRepository
import kotlinx.coroutines.runBlocking

class FakeAndroidTestRepository : TasksRepository {

    var tasksServiceData: LinkedHashMap<String, Task> = LinkedHashMap()
    private var observableTasks = MutableLiveData<Result<List<Task>>>()

    override suspend fun getTasks(forceUpdate: Boolean): Result<List<Task>> {
        return Result.Success(tasksServiceData.values.toList())
    }

    override suspend fun refreshTasks() {
        observableTasks.value = getTasks()
    }

    override fun observeTasks(): LiveData<Result<List<Task>>> {
        return observableTasks
    }

    override suspend fun refreshTask(taskId: String) {
        val tasks = (observableTasks.value as Result.Success).data

        deleteAllTasks()

        val newListTask = arrayListOf<Task>()
        newListTask.addAll(tasks)

        observableTasks = MutableLiveData(Result.Success(newListTask.toList()))
    }

    override fun observeTask(taskId: String): LiveData<Result<Task>> {
        val task = (observableTasks.value as Result.Success).data.find { it.id == taskId }
        task?.let {
            return MutableLiveData(Result.Success(task))
        }
        return MutableLiveData(Result.Error(Exception("Task not found!")))
    }

    override suspend fun getTask(taskId: String, forceUpdate: Boolean): Result<Task> {
        val tasks = (observableTasks.value as Result.Success).data
        val task = tasks.find { it.id == taskId }

        return task?.let { Result.Success(it) } ?: Result.Error(Exception("Task not found!"))
    }

    override suspend fun saveTask(task: Task) {
        addTask(task)
    }

    override suspend fun completeTask(task: Task) {
        val index = (observableTasks.value as Result.Success).data.indexOf(task)
        (observableTasks.value as Result.Success).data[index].isCompleted = true
    }

    override suspend fun completeTask(taskId: String) {
        val task = (observableTasks.value as Result.Success).data.find { it.id == taskId }
        task?.isCompleted = true
    }

    override suspend fun activateTask(task: Task) {
        val index = (observableTasks.value as Result.Success).data.indexOf(task)
        (observableTasks.value as Result.Success).data[index].isCompleted = false
    }

    override suspend fun activateTask(taskId: String) {
        val task = (observableTasks.value as Result.Success).data.find { it.id == taskId }
        task?.isCompleted = false
    }

    override suspend fun clearCompletedTasks() {
        val tasks = (observableTasks.value as Result.Success).data
        val newListTask = arrayListOf<Task>()
        newListTask.addAll(tasks.filter { !it.isCompleted })

        observableTasks = MutableLiveData(Result.Success(newListTask.toList()))
    }

    override suspend fun deleteAllTasks() {
        observableTasks = MutableLiveData()
    }

    override suspend fun deleteTask(taskId: String) {
        val tasks = (observableTasks.value as Result.Success).data
        val newListTask = arrayListOf<Task>()
        newListTask.addAll(tasks.filter { it.id != taskId })

        observableTasks = MutableLiveData(Result.Success(newListTask.toList()))
    }

    fun addTask(vararg tasks: Task) {
        for (task in tasks) {
            tasksServiceData[task.id] = task
        }

        runBlocking { refreshTasks() }
    }
}