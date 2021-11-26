package com.bahamaeatsdriver.model_class.login

data class Body(
    val address: String,
    val availability: Int,
    val city: String,
    var gender: Int,
    var dob: String,
    val contactNo: String,
    val country: String,
    val countryCode: String,
    val countryCodePhone: String,
    val createdAt: String,
    val deviceToken: String,
    val deviceType: Int,
    val driverIdentity: String,
    val email: String,
    val firstName: String,
    val forgotPasswordHash: String,
    val id: Int,
    var image: String,
    val isApproved: Int,
    val isDocumentVerified: Int,
    val isLicenceExists: Int,
    val lastName: String,
    var fullName: String,
    val latitude: String,
    val loginType: Int,
    val longitude: String,
    val otp: Int,
    val otpVerified: Int,
    val paymentStatus: Int,
    val postalCode: Int,
    val province: String,
    val socialId: String,
    val socialType: Int,
    val status: Int,
    var isNotification: Int,
    var takeOrderStatus: Int,
    val token: String,
    val updatedAt: String,
    val username: String
)