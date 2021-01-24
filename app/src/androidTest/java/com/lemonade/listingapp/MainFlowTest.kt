package com.lemonade.listingapp

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.lemonade.listingapp.views.MainActivity
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class MainFlowTest {

    @get:Rule
    var activityRule: ActivityScenarioRule<MainActivity> =
        ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun setup() {
        IdlingRegistry.getInstance().register(IdlingResource.countingIdlingResource)
    }

    @After
    fun teardown() {
        IdlingRegistry.getInstance().unregister(IdlingResource.countingIdlingResource)
    }


    @Test
    fun testDataIsLoadedAndDetailsOpenOnClicking() {
        onView(ViewMatchers.withId(R.id.itemsList))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    1,
                    click()
                )
            )
        onView(ViewMatchers.withId(R.id.item_name_tv))
            .check(matches(isDisplayed()))
    }

    @Test
    fun testWhenLoadFailsErrorShows() {

        InstrumentationRegistry.getInstrumentation().uiAutomation.executeShellCommand("svc wifi disable")
        InstrumentationRegistry.getInstrumentation().uiAutomation.executeShellCommand("svc data disable")
        IdlingResource.decrement()
        Espresso.onView(ViewMatchers.withId(R.id.errorTv))
            .check(ViewAssertions.matches(ViewMatchers.withText("Error loading data"))).check(
                ViewAssertions.matches(ViewMatchers.isDisplayed())
            )

        InstrumentationRegistry.getInstrumentation().uiAutomation.executeShellCommand("svc wifi enable")
        InstrumentationRegistry.getInstrumentation().uiAutomation.executeShellCommand("svc data enable")
    }
}