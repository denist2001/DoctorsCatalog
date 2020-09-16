package com.codechallenge.doctorscatalog.utils

import com.codechallenge.doctorscatalog.data.model.api.ApiResponse
import com.codechallenge.doctorscatalog.data.model.presentation.Doctor
import javax.inject.Inject

class ResponsesMapper @Inject constructor() {
    fun transform(apiResponse: ApiResponse): List<Doctor> {
        if (apiResponse.doctors.isNullOrEmpty()) return emptyList()
        val doctorsList = mutableListOf<Doctor>()
        for (doctor in apiResponse.doctors) {
            doctorsList.add(
                Doctor(
                    id = doctor.id,
                    name = doctor.name,
                    address = doctor.address,
                    picture = doctor.photoId,
                    rating = doctor.rating
                )
            )
        }
        doctorsList.sortByDescending { doctor ->
            doctor.rating
        }
        return doctorsList
    }
}
