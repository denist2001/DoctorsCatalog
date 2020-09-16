package com.codechallenge.doctorscatalog

import android.view.KeyEvent
import android.view.View
import android.widget.SearchView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
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
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.junit.*
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@UninstallModules(NetworkModule::class)
@HiltAndroidTest
class SearchTest {

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
    @Ignore("Needs to find solution to type in SearchView")
    @Test
    fun clickOnVivyDoctors_shouldOpenDetails() {
        setDispatcher("doctors.json", 200)
        activityTestRule.launchActivity(null)

        waitUntilViewWithId(R.id.doctors_list_rv, 5, isDisplayed())
        waitUntilViewWithText("Gemeinschaftspraxis Dr. Hintsche und Dr. Klausen", 15, isDisplayed())

        onView(withId(R.id.search_doctor_bn)).perform(click())
        waitUntilViewWithId(R.id.search_doctor_sv, 5, isDisplayed())
        onView(withContentDescription("Search")).perform(click())
        typeSearchViewText("Akdenizli")
        onView(withContentDescription("Search")).perform(typeText("Akdenizli"))
        pressKey(KeyEvent.KEYCODE_SEARCH)
        waitUntilViewWithId(R.id.search_button, 5, isDisplayed())
        onView(withId(R.id.search_button)).perform(click())
        waitUntilViewWithId(R.id.search_bar, 5, isDisplayed())
        onView(withId(R.id.search_bar)).perform(click())

        onView(withId(R.id.search_doctor_sv)).perform(pressImeActionButton())

        onView(withId(R.id.doctors_list_rv)).perform(
            actionOnItemAtPosition<ItemViewHolder>(
                3,
                click()
            )
        )
        waitUntilViewWithId(R.id.name_tv, 5, isDisplayed())
        waitUntilViewWithText("Gemeinschaftspraxis Dr. Hintsche und Dr. Klausen", 5, isDisplayed())
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

    private fun typeSearchViewText(text: String): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return allOf(isDisplayed(), isAssignableFrom(SearchView::class.java))
            }

            override fun getDescription(): String {
                return "Change view text"
            }

            override fun perform(uiController: UiController, view: View) {
                (view as SearchView).setQuery(text, false)
            }
        }
    }

    @After
    fun tearDown() {
        mockServer.shutdown()
    }
}