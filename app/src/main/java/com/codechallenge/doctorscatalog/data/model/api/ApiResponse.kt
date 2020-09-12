package com.codechallenge.doctorscatalog.data.model.api

data class ApiResponse(
    val doctors: ArrayList<ApiDoctor>,
    val lastKey: String
)