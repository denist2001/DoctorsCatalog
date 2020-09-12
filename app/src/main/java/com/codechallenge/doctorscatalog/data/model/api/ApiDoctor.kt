package com.codechallenge.doctorscatalog.data.model.api

data class ApiDoctor(
    val id: String,
    val name: String?,
    val photoId: String?,
    val rating: Float?,
    val address: String?,
    val lat: Float?,
    val lng: Float?,
    val highlighted: Boolean?,
    val reviewCount: Int?,
    val specialityIds: ArrayList<Int>?,
    val source: String?,
    val phoneNumber: String?,
    val email: String?,
    val website: String?,
    val openingHours: ArrayList<String>?,
    val integration: String?,
    val translation: String?
)