package com.bahamaeatsdriver.model_class.change_ride_status

import java.io.Serializable

data class UserAddress(
    val address: String,
    val alternateMobileNumber: String,
    val city: String,
    val cityId: Int,
    val completeAddress: String,
    val streetName: String,
    val country: String,
    val countryCode: String,
    val countryCodeAlternateMobileNumber: String,
    val countryId: Int,
    val createdAt: String,
    val deliveryInstructions: String,
    val firstName: String,
    val id: Int,
    val isDefault: Int,
    val lastName: String,
    val latitude: Double,
    val longitude: Double,
    val postalCode: Int,
    val provience: String,
    val provienceId: Int,
    val updatedAt: String,
    val userId: Int
): Serializable