package com.bahamaeatsdriver.model_class.get_rider_details

data class UserAddress(
    val address: String,
    val alternateMobileNumber: String,
    val city: String,
    val cityId: Int,
    val completeAddress: String,
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
    val latitude: String,
    val longitude: String,
    val postalCode: Int,
    val provience: String,
    val provienceId: Int,
    val updatedAt: String,
    val userId: Int
)