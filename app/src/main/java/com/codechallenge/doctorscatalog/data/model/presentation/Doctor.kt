package com.codechallenge.doctorscatalog.data.model.presentation

/**
 * Detailed doctor screen should display name, address and picture of selected doctor.
 */
data class Doctor(
    val id: String,
    val name: String?,
    val address: String?,
    val picture: String?,
    val rating: Float?
)