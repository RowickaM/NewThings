package com.rowicka.newthings.todoapp.statistics

import com.rowicka.newthings.todoapp.data.Task
import org.junit.Assert.assertEquals
import org.junit.Test

class StatisticsUtilsTest {

    private val taskUnCompleted = Task(
        title = "title",
        description = "description",
        isCompleted = false
    )
    private val taskCompleted = Task(
        title = "title",
        description = "description",
        isCompleted = true
    )

    @Test
    fun getActiveAndCompletedStats_noCompleted_returnsZeroHundred() {
        val tasks = listOf(taskUnCompleted)

        val result = getActiveAndCompletedStats(tasks)

        assertEquals(0f, result.completedTasksPercent)
        assertEquals(100f, result.activeTasksPercent)
    }

    @Test
    fun getActiveAndCompletedStats_both_returnsFortySixty() {
        val expected = StatsResult(60f, 40f)
        val tasks =
            listOf(taskCompleted, taskCompleted, taskUnCompleted, taskUnCompleted, taskUnCompleted)

        val result = getActiveAndCompletedStats(tasks)

        assertEquals(expected, result)
    }

    @Test
    fun getActiveAndCompletedStats_empty_returnsZeros() {
        val expected = StatsResult(0f, 0f)
        val tasks = listOf<Task>()

        val result = getActiveAndCompletedStats(tasks)

        assertEquals(expected, result)
    }

    @Test
    fun getActiveAndCompletedStats_null_returnsZeros() {
        val expected = StatsResult(0f, 0f)
        val tasks = null

        val result = getActiveAndCompletedStats(tasks)

        assertEquals(expected, result)
    }
}