package com.codechallenge.doctorscatalog

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.codechallenge.doctorscatalog.di.NetworkModule
import com.codechallenge.doctorscatalog.ui.main.HeaderViewHolder
import com.codechallenge.doctorscatalog.ui.main.ItemViewHolder
import com.codechallenge.doctorscatalog.utils.getStringFrom
import com.codechallenge.doctorscatalog.utils.recyclerItemAtPosition
import com.codechallenge.doctorscatalog.utils.waitUntilViewWithId
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
class SortingTest {

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

    /**
    10 - "Dr. med. Mario Voss" - 2.5
    13 - "Daniel Engert and Annette Cotanidis" - 2.300000190732863
    01 - "Gemeinschaftspraxis Dr. Hintsche und Dr. Klausen" - 5
    14 - "Gemeinschaftspraxis Schlesisches Tor" - 2.200000095367232
    05 - "Jasmin Malak" - 3.299999952316282
    15 - "Sylvia Kollmann" - 2.200000095367232
    08 - "Dr. med. Gülenay Pamuk" - 2.599999902632568
    11 - "Medical Office Berlin" - 2.5
    02 - "Dr. med. Manfred Lapp specialist in general medicine" - 3.900000095367232
    09 - "Dr. med. Christopher Marchand und Michael Hoffmann" - 2.599999902632568
    12 - "Dorothee Michel - Specialist in General Medicine" - 2.5
    06 - "Dr. med. Michaela Machachej Specialist f. General Medicine" - 2.900000095367232
    20 - "Kathrin Schmidt Specialist f. General Medicine" - 2
    16 - "Praxis am Kreuzberg, Fachärztin für Allgemeinmedizin/Hausarzt, Gabriele Fahrbach" - 2.199999809265137
    19 - "Dr. med. Artur Jelitto" - 2.099999902632568
    17 - "Dr. Med. Thorsten Richter" - 2.199999809265137
    07 - "Herr Dr. med. Gerd Klausen" - 2.800000190732863
    18 - "Praxis Lemm" - 2.199999809265137
    04 - "Dr. med. Garney Micus" - 3.799999952316282
    03 - "Dr. med. Casares Akdenizli" - 3.900000095367232
     */
    @ExperimentalCoroutinesApi
    @InternalCoroutinesApi
    @Test
    fun validJson_shouldShowItemsWithDescendingSortByRating() {
        setDispatcher("doctors.json", 200)
        activityTestRule.launchActivity(null)

        waitUntilViewWithId(R.id.doctors_list_rv, 5, isDisplayed())

        onView(withId(R.id.doctors_list_rv))
            .perform(scrollToPosition<HeaderViewHolder>(0)).check(matches(
            recyclerItemAtPosition(0, hasDescendant(withText("Recent Doctors"))))
        )
        onView(withId(R.id.doctors_list_rv))
            .perform(scrollToPosition<HeaderViewHolder>(2)).check(matches(
            recyclerItemAtPosition(2, hasDescendant(withText("Vivy Doctors"))))
            )
        onView(withId(R.id.doctors_list_rv))
            .perform(scrollToPosition<ItemViewHolder>(3)).check(matches(
            recyclerItemAtPosition(3,
                hasDescendant(withText("Gemeinschaftspraxis Dr. Hintsche und Dr. Klausen"))))
            )
        onView(withId(R.id.doctors_list_rv))
            .perform(scrollToPosition<ItemViewHolder>(7)).check(matches(
            recyclerItemAtPosition(7,
                    hasDescendant(withText("Jasmin Malak"))))
            )
        onView(withId(R.id.doctors_list_rv))
            .perform(scrollToPosition<ItemViewHolder>(12)).check(matches(
                recyclerItemAtPosition(12,
                    hasDescendant(withText("Dr. med. Mario Voss"))))
            )
        onView(withId(R.id.doctors_list_rv))
            .perform(scrollToPosition<ItemViewHolder>(17)).check(matches(
                recyclerItemAtPosition(17,
                    hasDescendant(withText("Sylvia Kollmann"))))
            )
        onView(withId(R.id.doctors_list_rv))
            .perform(scrollToPosition<ItemViewHolder>(22)).check(matches(
                recyclerItemAtPosition(22,
                    hasDescendant(withText("Kathrin Schmidt Specialist f. General Medicine"))))
            )
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