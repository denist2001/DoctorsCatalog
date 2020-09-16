package com.codechallenge.doctorscatalog.data

import com.codechallenge.doctorscatalog.data.model.api.ApiResponse
import retrofit2.http.*

interface RepositoryService {

    @GET("interviews/challenges/android/doctors.json")
    suspend fun loadFirstPage(): ApiResponse

    @GET("interviews/challenges/android/doctors-{lastKey}.json")
    suspend fun loadNextPage(
        @Path("lastKey") lastKey: String
    ): ApiResponse
}