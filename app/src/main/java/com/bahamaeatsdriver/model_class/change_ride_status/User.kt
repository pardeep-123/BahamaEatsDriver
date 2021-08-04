package com.bahamaeatsdriver.model_class.change_ride_status

import java.io.Serializable

data class User(
    val countryCode: String,
    val countryCodePhone: String,
    val deviceToken: String,
    val email: String,
    val firstName: String,
    val id: Int,
    val lastName: String,
    val phone: String,
    val photo: String,
    val username: String
): Serializable