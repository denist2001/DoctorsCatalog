package com.codechallenge.doctorscatalog

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.codechallenge.doctorscatalog.di.NetworkModule
import com.codechallenge.doctorscatalog.ui.viewholder.ItemViewHolder
import com.codechallenge.doctorscatalog.utils.getStringFrom
import com.codechallenge.doctorscatalog.utils.waitUntilViewWithId
import com.codechallenge.doctorscatalog.utils.waitUntilViewWithText
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@UninstallModules(NetworkModule::class)
@HiltAndroidTest
class ClickOnItems {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val activityTestRule = ActivityTestRule(MainActivity::class.java, true, false)

    private lateinit var mockServer: MockWebServer

    @Before
    fun setUp() {
        hiltRule.inject()
        mockServer = MockWebServer()
        mockServer.start(8080)
    }

    @ExperimentalCoroutinesApi
    @InternalCoroutinesApi
    @Test
    fun clickOnVivyDoctors_shouldOpenDetails() {
        setDispatcher("doctors.json", 200)
        activityTestRule.launchActivity(null)

        waitUntilViewWithId(R.id.doctors_list_rv, 5, isDisplayed())
        waitUntilViewWithText("Gemeinschaftspraxis Dr. Hintsche und Dr. Klausen", 5, isDisplayed())
        onView(withId(R.id.doctors_list_rv)).perform(RecyclerViewActions.actionOnItemAtPosition<ItemViewHolder>(3, click()))
        waitUntilViewWithId(R.id.name_tv, 5, isDisplayed())
        waitUntilViewWithText("Gemeinschaftspraxis Dr. Hintsche und Dr. Klausen", 5, isDisplayed())
    }

    @ExperimentalCoroutinesApi
    @InternalCoroutinesApi
    @LargeTest
    @Test
    fun clickOnRecentDoctors_shouldOpenDetails() {
        setDispatcher("doctors.json", 200)
        activityTestRule.launchActivity(null)

        waitUntilViewWithId(R.id.doctors_list_rv, 5, isDisplayed())
        waitUntilViewWithText("Gemeinschaftspraxis Dr. Hintsche und Dr. Klausen", 15, isDisplayed())
        onView(withId(R.id.doctors_list_rv)).perform(RecyclerViewActions.actionOnItemAtPosition<ItemViewHolder>(3, click()))
        waitUntilViewWithId(R.id.name_tv, 15, isDisplayed())
        waitUntilViewWithText("Gemeinschaftspraxis Dr. Hintsche und Dr. Klausen", 15, isDisplayed())
        pressBack()
        onView(withId(R.id.doctor_1_iv)).perform(click())
        waitUntilViewWithText("Gemeinschaftspraxis Dr. Hintsche und Dr. Klausen", 15, isDisplayed())
    }

    private fun setDispatcher(fileName: String, responseCode: Int) {
        mockServer.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                val path = request.path
                if (!path.isNullOrEmpty() && path.contains("doctors-CvQD7gEAAKjcb.json")) {
                    return MockResponse()
                        .setResponseCode(responseCode)
                        .setBody(getStringFrom("doctors-CvQD7gEAAKjcb.json"))
                }
                return MockResponse()
                    .setResponseCode(responseCode)
                    .setBody(getStringFrom(fileName))
            }
        }
    }

    @After
    fun tearDown() {
        mockServer.shutdown()
    }
}