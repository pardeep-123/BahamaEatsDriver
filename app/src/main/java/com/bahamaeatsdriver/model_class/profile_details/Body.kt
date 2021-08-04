package com.bahamaeatsdriver.model_class.profile_details

import java.io.Serializable

data class Body(
    val address: String,
    val availability: Int,
    val city: String,
    val contactNo: String,
    val country: String,
    val countryCode: String,
    val countryCodePhone: String,
    val createdAt: String,
    val deviceToken: String,
    val deviceType: Int,
    val driverAddress: DriverAddress,
    val driverIdentity: String,
    val email: String,
    val firstName: String,
    val forgotPasswordHash: String,
    val fullName: String,
    val id: Int,
    val image: String,
    val isApproved: Int,
    val isDocumentVerified: Int,
    val isNotification: Int,
    val lastName: String,
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
    val takeOrderStatus: Int,
    val updatedAt: String,
    val username: String
):Serializable