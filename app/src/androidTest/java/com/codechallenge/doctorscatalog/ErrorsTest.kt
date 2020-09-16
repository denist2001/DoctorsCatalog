package com.codechallenge.doctorscatalog

import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import androidx.test.uiautomator.UiDevice
import com.codechallenge.doctorscatalog.di.NetworkModule
import com.codechallenge.doctorscatalog.utils.getStringFrom
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.hamcrest.Matchers
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@UninstallModules(NetworkModule::class)
@HiltAndroidTest
class ErrorsTest {

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

    @Test
    fun emptyJson_shouldShowErrorMessage() {
        setDispatcher("empty.json", 200)
        activityTestRule.launchActivity(null)
        checkToast("Result can not be parsed")
    }

    @Test
    fun malformedJson_shouldShowErrorMessage() {
        setDispatcher("malformed_doctors.json", 200)
        activityTestRule.launchActivity(null)
        checkToast("Result can not be parsed")
    }

    @Test
    fun networkError_shouldShowErrorMessage() {
        setDispatcher("doctors.json", 404)
        activityTestRule.launchActivity(null)
        checkToast("Network error")
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

    private fun checkToast(message: String) {
        Espresso.onView(ViewMatchers.withText(message))
            .inRoot(
                RootMatchers.withDecorView(
                    Matchers.not(
                        Matchers.`is`(
                            activityTestRule.activity.window.decorView
                        )
                    )
                )
            )
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @After
    fun tearDown() {
        UiDevice.getInstance(InstrumentationRegistry.getInstrumentation()).setOrientationNatural()
        mockServer.shutdown()
    }
}