package com.codechallenge.doctorscatalog.utils

import com.codechallenge.doctorscatalog.TestApiResponse
import com.codechallenge.doctorscatalog.data.model.api.ApiDoctor
import com.codechallenge.doctorscatalog.data.model.presentation.Doctor
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import org.junit.Test

class ResponsesMapperTest {
    private val subject = ResponsesMapper()

    /**
    rating
    06 - "Dr. med. Mario Voss" - 2.5
    08 - "Daniel Engert and Annette Cotanidis" - 2.300000190732863
    01 - "Gemeinschaftspraxis Dr. Hintsche und Dr. Klausen" - 5
    09 - "Gemeinschaftspraxis Schlesisches Tor" - 2.200000095367232
    03 - "Jasmin Malak" - 3.299999952316282
    10 - "Sylvia Kollmann" - 2.200000095367232
    04 - "Dr. med. Gülenay Pamuk" - 2.599999902632568
    07 - "Medical Office Berlin" - 2.5
    02 - "Dr. med. Manfred Lapp specialist in general medicine" - 3.900000095367232
    05 - "Dr. med. Christopher Marchand und Michael Hoffmann" - 2.599999902632568
     */
    @Test
    fun `can transform and sort values`() {
        val input = TestApiResponse.preparedTestApiResponse()
        val result = subject.transform(input)
        assertEquals(TestApiResponse.doctor3.name, result[0].name)
        assertEquals(TestApiResponse.doctor9.name, result[1].name)
        assertEquals(TestApiResponse.doctor5.name, result[2].name)
        assertEquals(TestApiResponse.doctor7.name, result[3].name)
        assertEquals(TestApiResponse.doctor10.name, result[4].name)
        assertEquals(TestApiResponse.doctor1.name, result[5].name)
        assertEquals(TestApiResponse.doctor8.name, result[6].name)
        assertEquals(TestApiResponse.doctor2.name, result[7].name)
        assertEquals(TestApiResponse.doctor4.name, result[8].name)
        assertEquals(TestApiResponse.doctor6.name, result[9].name)
    }

    @Test
    fun `when empty list comes should return empty list`() {
        val input = TestApiResponse.prepareEmptyApiResponse()
        assertEquals(emptyList<Doctor>(), subject.transform(input))
    }

    @Test
    fun `when doctor has required fields as null should return valid value`() {
        val doctor = ApiDoctor(
            id = "some id",
            name = null,
            photoId = null,
            rating = null,
            address = null,
            lat = null,
            lng = null,
            highlighted = null,
            reviewCount = null,
            specialityIds = null,
            source = null,
            phoneNumber = null,
            email = null,
            website = null,
            openingHours = null,
            integration = null,
            translation = null
        )
        val input = TestApiResponse.prepareApiResponseFrom(arrayListOf(doctor))
        assertNotNull(subject.transform(input)[0])
    }
}