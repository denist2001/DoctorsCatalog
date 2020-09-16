package com.codechallenge.doctorscatalog

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import androidx.test.uiautomator.UiDevice
import com.codechallenge.doctorscatalog.di.NetworkModule
import com.codechallenge.doctorscatalog.ui.main.ItemViewHolder
import com.codechallenge.doctorscatalog.utils.getStringFrom
import com.codechallenge.doctorscatalog.utils.recyclerItemAtPosition
import com.codechallenge.doctorscatalog.utils.waitUntilViewWithId
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import junit.framework.Assert.assertEquals
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
class RotationTest {

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
        UiDevice.getInstance(InstrumentationRegistry.getInstrumentation()).unfreezeRotation()
        UiDevice.getInstance(InstrumentationRegistry.getInstrumentation()).setOrientationNatural()
    }

    //TODO: Investigate - probably wrong behavior!
    @ExperimentalCoroutinesApi
    @InternalCoroutinesApi
    @Test
    fun rotation_shouldMakeSecondRequest() {
        setDispatcher("doctors.json", 200)
        activityTestRule.launchActivity(null)

        waitUntilViewWithId(R.id.doctors_list_rv, 5, isDisplayed())
        assertEquals(1, mockServer.requestCount)
        onView(withId(R.id.doctors_list_rv))
            .perform(scrollToPosition<ItemViewHolder>(7)).check(
                matches(
                    recyclerItemAtPosition(
                        7,
                        hasDescendant(withText("Jasmin Malak"))
                    )
                )
            )

        UiDevice.getInstance(InstrumentationRegistry.getInstrumentation()).setOrientationLeft()
        waitUntilViewWithId(R.id.doctors_list_rv, 5, isDisplayed())
        onView(withId(R.id.doctors_list_rv))
            .perform(scrollToPosition<ItemViewHolder>(7)).check(
                matches(
                    recyclerItemAtPosition(
                        7,
                        hasDescendant(withText("Jasmin Malak"))
                    )
                )
            )
        assertEquals(2, mockServer.requestCount)
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
        UiDevice.getInstance(InstrumentationRegistry.getInstrumentation()).setOrientationNatural()
        mockServer.shutdown()
    }
}