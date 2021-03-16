package com.rowicka.newthings.todoapp

import android.app.Activity
import android.view.Gravity
import androidx.appcompat.widget.Toolbar
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.DrawerMatchers.isOpen
import androidx.test.espresso.matcher.ViewMatchers.*
import com.rowicka.newthings.R
import com.rowicka.newthings.todoapp.data.Task
import com.rowicka.newthings.todoapp.data.source.TasksRepository
import com.rowicka.newthings.todoapp.tasks.TasksActivity
import com.rowicka.newthings.todoapp.util.DataBindingIdlingResource
import com.rowicka.newthings.todoapp.util.EspressoIdlingResource
import com.rowicka.newthings.todoapp.util.monitorActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class AppNavigationTest {


    private lateinit var tasksRepository: TasksRepository

    // An Idling Resource that waits for Data Binding to have no pending bindings
    private val dataBindingIdlingResource = DataBindingIdlingResource()

    @Before
    fun init() {
        tasksRepository = ServiceLocator.provideTaskRepository(ApplicationProvider.getApplicationContext())
    }

    @After
    fun reset() {
        ServiceLocator.resetRepository()
    }

    /**
     * Idling resources tell Espresso that the app is idle or busy. This is needed when operations
     * are not scheduled in the main Looper (for example when executed on a different thread).
     */
    @Before
    fun registerIdlingResource() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
        IdlingRegistry.getInstance().register(dataBindingIdlingResource)
    }

    /**
     * Unregister your Idling Resource so it can be garbage collected and does not leak any memory.
     */
    @After
    fun unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
        IdlingRegistry.getInstance().unregister(dataBindingIdlingResource)
    }

    @Test
    fun tasksScreen_clickOnDrawerIcon_OpensNavigation(){
        val activityScenario = ActivityScenario.launch(TasksActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        onView(
            withContentDescription(
                activityScenario.getToolbarNavigationContentDescription()
            )
        ).perform(click())

        onView(withId(R.id.drawer_layout))
            .check(matches(isOpen(Gravity.START)))

        activityScenario.close()
    }

    @Test
    fun tasksDetailScreen_doubleUpButton() = runBlocking{
        val task = Task("Up button", "Description")
        tasksRepository.saveTask(task)

        val activityScenario = ActivityScenario.launch(TasksActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        onView(withText("Up button"))
            .perform(click())

        onView(withId(R.id.edit_task_fab))
            .perform(click())

        onView(withContentDescription(activityScenario.getToolbarNavigationContentDescription()))
            .perform(click())

        onView(withId(R.id.task_detail_title_text)).apply {
            check(matches(isDisplayed()))
            check(matches(withText(task.title)))
        }

        onView(withId(R.id.task_detail_description_text)).apply {
            check(matches(isDisplayed()))
            check(matches(withText(task.description)))
        }

        onView(withContentDescription(activityScenario.getToolbarNavigationContentDescription()))
            .perform(click())

        onView(withId(R.id.tasks_list))
            .check(matches(isDisplayed()))

        activityScenario.close()
    }

    @Test
    fun tasksDetailScreen_doubleBackButton() = runBlocking{
        val task = Task("Back button", "Description")
        tasksRepository.saveTask(task)

        val activityScenario = ActivityScenario.launch(TasksActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        onView(withText("Back button"))
            .perform(click())

        onView(withId(R.id.edit_task_fab))
            .perform(click())

        pressBack()

        onView(withId(R.id.task_detail_title_text)).apply {
            check(matches(isDisplayed()))
            check(matches(withText(task.title)))
        }

        onView(withId(R.id.task_detail_description_text)).apply {
            check(matches(isDisplayed()))
            check(matches(withText(task.description)))
        }

        pressBack()

        onView(withId(R.id.tasks_list))
            .check(matches(isDisplayed()))

        activityScenario.close()
    }
}

fun <T : Activity> ActivityScenario<T>.getToolbarNavigationContentDescription()
        : String {
    var description = ""
    onActivity {
        description =
            it.findViewById<Toolbar>(R.id.toolbar).navigationContentDescription as String
    }
    return description
}