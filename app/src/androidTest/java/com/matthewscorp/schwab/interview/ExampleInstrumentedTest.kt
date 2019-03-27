package com.matthewscorp.schwab.interview

import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.*
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.intent.rule.IntentsTestRule
import android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.runner.AndroidJUnit4
import com.afollestad.materialdialogs.MaterialDialog
import com.matthewscorp.schwab.interview.activities.ScrollingActivity
import kotlinx.android.synthetic.main.activity_scrolling.*
import org.junit.Assert

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Rule
import android.support.test.espresso.Espresso
import android.support.test.espresso.IdlingPolicies
import java.util.concurrent.TimeUnit
import android.support.v7.widget.RecyclerView
import android.support.test.espresso.NoMatchingViewException
import android.support.test.espresso.ViewAssertion
import android.view.View
import org.hamcrest.CoreMatchers.`is`


/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    @Rule
    @JvmField
    var activityRule = IntentsTestRule(ScrollingActivity::class.java)

    val activity by lazy {
        activityRule.activity
    }

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getTargetContext()
        assertEquals("com.matthewscorp.schwab.interview", appContext.packageName)
    }

    @Test
    fun test_Activity() {
        val some = activity.fab
        Assert.assertTrue(some.hasOnClickListeners())
        activity.runOnUiThread(Runnable { activity.fab.callOnClick() })
        hasComponent(MaterialDialog::class.java.name)
        onView(withText(activity.getString(R.string.enter_search))).check(matches(isDisplayed()))
        onView(withId(android.R.id.input)).perform(typeText("pizza"), closeSoftKeyboard())
        onView(withId(com.afollestad.materialdialogs.R.id.md_buttonDefaultPositive)).perform(click())

        // Make sure Espresso does not time out
        IdlingPolicies.setMasterPolicyTimeout(10000 * 2, TimeUnit.MILLISECONDS)
        IdlingPolicies.setIdlingResourceTimeout(10000 * 2, TimeUnit.MILLISECONDS)

        // Now we wait
        val idlingResource = ElapsedTimeIdlingResource(10000)
        Espresso.registerIdlingResources(idlingResource)
        onView(withId(R.id.rv_scrolling_activity)).check(RecyclerViewItemCountAssertion(10))
        Espresso.unregisterIdlingResources(idlingResource)
    }

    inner class RecyclerViewItemCountAssertion(private val expectedCount: Int) : ViewAssertion {

        override fun check(view: View, noViewFoundException: NoMatchingViewException?) {
            if (noViewFoundException != null) {
                throw noViewFoundException
            }

            val recyclerView = view as RecyclerView
            val adapter = recyclerView.adapter
            Assert.assertThat(adapter!!.itemCount, `is`(expectedCount))
        }
    }
}
