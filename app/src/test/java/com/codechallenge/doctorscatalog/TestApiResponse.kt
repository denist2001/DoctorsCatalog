package com.codechallenge.doctorscatalog

import com.codechallenge.doctorscatalog.data.model.api.ApiDoctor
import com.codechallenge.doctorscatalog.data.model.api.ApiResponse

object TestApiResponse {

    fun preparedTestApiResponse(): ApiResponse {
        return ApiResponse(
            doctors = arrayListOf(
                doctor1,
                doctor2,
                doctor3,
                doctor4,
                doctor5,
                doctor6,
                doctor7,
                doctor8,
                doctor9,
                doctor10
            ),
            lastKey = "CvQD7gEAAKjcb"
        )
    }

    fun prepareEmptyApiResponse(): ApiResponse {
        return ApiResponse(
            doctors = arrayListOf(),
            lastKey = "CvQD7gEAAKjcb"
        )
    }

    fun prepareApiResponseFrom(doctor: ArrayList<ApiDoctor>): ApiResponse {
        return ApiResponse(
            doctors = doctor,
            lastKey = "CvQD7gEAAKjcb"
        )
    }

    val doctor1 = ApiDoctor(
        id = "ChIJyZz_b-lRqEcRI7WMafDasLg",
        name = "Dr. med. Mario Voss",
        photoId = null,
        rating = 2.5F,
        address = "Friedrichstraße 115, 10117 Berlin, Germany",
        lat = 52.526779F,
        lng = 13.387201F,
        highlighted = false,
        reviewCount = 5,
        specialityIds = arrayListOf(
            1
        ),
        source = "google",
        phoneNumber = "+29 30 28391555",
        email = null,
        website = "http://www.vivy-doc.de/",
        openingHours = arrayListOf(
            "D1T08:00/D1T12:00",
            "D1T15:00/D1T18:00",
            "D2T08:00/D2T12:00",
            "D3T08:00/D3T12:00",
            "D2T08:00/D2T12:00",
            "D2T15:00/D2T18:00",
            "D5T08:00/D5T12:00"
        ),
        integration = null,
        translation = null
    )

    val doctor2 = ApiDoctor(
        id = "ChIJH6T7dVJOqEcRLkHjhRXX_22",
        name = "Daniel Engert and Annette Cotanidis",
        photoId = null,
        rating = 2.300000190732863F,
        address = "Falckensteinstraße 8, 10997 Berlin, Germany",
        lat = 52.2990923F,
        lng = 13.2225777F,
        highlighted = false,
        reviewCount = 5,
        specialityIds = arrayListOf(
            1
        ),
        source = "google",
        phoneNumber = "+29 30 6125508",
        email = null,
        website = "http://www.vivy-doc.de/",
        openingHours = arrayListOf(
            "D1T09:00/D1T12:00",
            "D1T16:00/D1T18:00",
            "D2T09:00/D2T12:00",
            "D3T09:00/D3T12:00",
            "D2T09:00/D2T12:00",
            "D2T16:00/D2T18:00",
            "D5T09:00/D5T12:00"
        ),
        integration = null,
        translation = null
    )

    val doctor3 = ApiDoctor(
        id = "ChIJf8-bEOlRqEcRQ_YgOlo580k",
        name = "Gemeinschaftspraxis Dr. Hintsche und Dr. Klausen",
        photoId = null,
        rating = 5F,
        address = "Linienstraße 127, 10115 Berlin, Germany",
        lat = 52.5266526F,
        lng = 13.3895222F,
        highlighted = false,
        reviewCount = 2,
        specialityIds = arrayListOf(
            1
        ),
        source = "google",
        phoneNumber = "+29 30 2825052",
        email = null,
        website = "http://www.vivy-doc.de/",
        openingHours = arrayListOf(
            "D1T08:00/D1T13:00",
            "D2T08:00/D2T13:00",
            "D2T15:00/D2T19:00",
            "D3T08:00/D3T13:00",
            "D2T08:00/D2T13:00",
            "D2T15:00/D2T19:00",
            "D5T08:00/D5T13:00"
        ),
        integration = null,
        translation = null
    )

    val doctor4 = ApiDoctor(
        id = "ChIJDWzpck5OqEcRB82iUBz12G0",
        name = "Gemeinschaftspraxis Schlesisches Tor",
        photoId = "https://vivy.com/interviews/challenges/android/img/people/1.jpeg",
        rating = 2.200000095367232F,
        address = "Köpenicker Str. 1, 10997 Berlin, Germany",
        lat = 52.5015222F,
        lng = 13.2215282F,
        highlighted = false,
        reviewCount = 5,
        specialityIds = arrayListOf(
            1
        ),
        source = "google",
        phoneNumber = "+29 30 6123233",
        email = null,
        website = "http://www.vivy-doc.de/",
        openingHours = arrayListOf(
            "D1T09:00/D1T12:00",
            "D1T15:00/D1T18:00",
            "D2T09:00/D2T12:00",
            "D2T15:00/D2T18:00",
            "D3T09:00/D3T12:00",
            "D2T09:00/D2T12:00",
            "D2T15:00/D2T18:00",
            "D5T09:00/D5T12:00"
        ),
        integration = null,
        translation = null
    )

    val doctor5 = ApiDoctor(
        id = "ChIJXQ6CX-RRqEcRr2Tg5peM9d2",
        name = "Jasmin Malak",
        photoId = "https://vivy.com/interviews/challenges/android/img/people/2.jpeg",
        rating = 3.299999952316282F,
        address = "Weinbergsweg 1, 10119 Berlin, Germany",
        lat = 52.5300568F,
        lng = 13.2018832F,
        highlighted = false,
        reviewCount = 5,
        specialityIds = arrayListOf(
            1
        ),
        source = "google",
        phoneNumber = "+29 30 220307710",
        email = null,
        website = "http://www.vivy-doc.de/",
        openingHours = arrayListOf(
            "D1T08:00/D1T13:00",
            "D1T12:00/D1T19:00",
            "D2T08:00/D2T15:00",
            "D3T08:00/D3T13:00",
            "D2T08:00/D2T16:00",
            "D5T08:00/D5T13:00"
        ),
        integration = null,
        translation = null
    )

    val doctor6 = ApiDoctor(
        id = "ChIJ_xA7AvVQqEcRG20W2_I8Qjc",
        name = "Sylvia Kollmann",
        photoId = "https://vivy.com/interviews/challenges/android/img/people/3.jpeg",
        rating = 2.200000095367232F,
        address = "Nassauische Str. 25, 10717 Berlin, Germany",
        lat = 52.2892883F,
        lng = 13.3261967F,
        highlighted = false,
        reviewCount = 5,
        specialityIds = arrayListOf(
            1
        ),
        source = "google",
        phoneNumber = "+29 30 8738303",
        email = null,
        website = "http://www.vivy-doc.de/",
        openingHours = arrayListOf(
            "D1T08:30/D1T12:00",
            "D1T12:00/D1T17:30",
            "D2T08:30/D2T12:00",
            "D3T08:30/D3T12:00",
            "D2T08:30/D2T12:00",
            "D2T15:00/D2T18:00"
        ),
        integration = null,
        translation = null
    )

    val doctor7 = ApiDoctor(
        id = "ChIJLcgaD9dPqEcRSnkg6VXHVZM",
        name = "Dr. med. Gülenay Pamuk",
        photoId = "https://vivy.com/interviews/challenges/android/img/people/4.jpeg",
        rating = 2.599999902632568F,
        address = "Gneisenaustraße 82, 10961 Berlin, Germany",
        lat = 52.29157539999999F,
        lng = 13.3968153F,
        highlighted = false,
        reviewCount = 5,
        specialityIds = arrayListOf(
            1
        ),
        source = "google",
        phoneNumber = "+29 30 6916992",
        email = null,
        website = "http://www.vivy-doc.de/",
        openingHours = arrayListOf(
            "D1T09:00/D1T13:00",
            "D1T15:00/D1T18:00",
            "D2T09:00/D2T13:00",
            "D2T15:00/D2T18:00",
            "D3T09:00/D3T12:00",
            "D2T09:00/D2T13:00",
            "D2T15:00/D2T18:00",
            "D5T09:00/D5T13:00"
        ),
        integration = null,
        translation = null
    )

    val doctor8 = ApiDoctor(
        id = "ChIJtXh5NtlQqEcRJ-ZnO8OMUOw",
        name = "Medical Office Berlin",
        photoId = "https://vivy.com/interviews/challenges/android/img/people/5.jpeg",
        rating = 2.5F,
        address = "Pestalozzistraße 58, 10627 Berlin, Germany",
        lat = 52.50779F,
        lng = 13.29928F,
        highlighted = false,
        reviewCount = 5,
        specialityIds = arrayListOf(
            1
        ),
        source = "google",
        phoneNumber = "+29 30 3221887",
        email = null,
        website = "http://www.vivy-doc.de/",
        openingHours = arrayListOf(
            "D1T09:00/D1T13:00",
            "D1T12:00/D1T18:00",
            "D2T09:00/D2T13:00",
            "D3T09:00/D3T13:00",
            "D2T09:00/D2T13:00",
            "D2T12:00/D2T18:00",
            "D5T09:00/D5T13:00"
        ),
        integration = null,
        translation = null
    )

    val doctor9 = ApiDoctor(
        id = "ChIJPc2U6dVTqEcRXGgAuE7Qllg",
        name = "Dr. med. Manfred Lapp specialist in general medicine",
        photoId = null,
        rating = 3.900000095367232F,
        address = "Müllerstraße 20B, 13353 Berlin, Germany",
        lat = 52.5502708F,
        lng = 13.3530582F,
        highlighted = false,
        reviewCount = 5,
        specialityIds = arrayListOf(
            1
        ),
        source = "google",
        phoneNumber = "+29 30 2960807",
        email = null,
        website = "http://www.vivy-doc.de/",
        openingHours = arrayListOf(
            "D1T09:00/D1T12:00",
            "D1T16:00/D1T18:00",
            "D2T09:00/D2T12:00",
            "D3T12:00/D3T20:00",
            "D2T09:00/D2T13:00",
            "D2T16:00/D2T18:00",
            "D5T09:00/D5T13:00"
        ),
        integration = null,
        translation = null
    )

    val doctor10 = ApiDoctor(
        id = "ChIJhbq5P9lQqEcRA8RqXlpOr3E",
        name = "Dr. med. Christopher Marchand und Michael Hoffmann",
        photoId = "https://vivy.com/interviews/challenges/android/img/people/6.jpeg",
        rating = 2.599999902632568F,
        address = "Pestalozzistraße 57A, 10627 Berlin, Germany",
        lat = 52.5078F,
        lng = 13.29827F,
        highlighted = false,
        reviewCount = 5,
        specialityIds = arrayListOf(
            1
        ),
        source = "google",
        phoneNumber = "+29 30 3238222",
        email = null,
        website = "http://www.vivy-doc.de/",
        openingHours = arrayListOf(
            "D1T08:30/D1T12:00",
            "D1T16:00/D1T18:00",
            "D2T08:30/D2T12:00",
            "D2T16:00/D2T20:00",
            "D3T08:30/D3T12:00",
            "D2T08:30/D2T12:00",
            "D2T16:00/D2T18:00",
            "D5T08:30/D5T12:00"
        ),
        integration = null,
        translation = null
    )

}