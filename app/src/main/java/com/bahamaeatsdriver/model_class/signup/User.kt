package com.bahamaeatsdriver.model_class.signup

data class User(
    val city: String,
    val contactNo: String,
    val country: String,
    val countryCode: String,
    val countryCodePhone: String,
    val createdAt: String,
    val deviceToken: String,
    val deviceType: Int,
    val email: String,
    val firstName: String,
    val fullName: String,
    val id: Int,
    val image: String,
    val isLicenceExists: Int,
    val lastName: String,
    val loginType: Int,
    val socialId: String,
    val socialType: Int,
    val token: String,
    val updated_at: String,
    val username: String
)