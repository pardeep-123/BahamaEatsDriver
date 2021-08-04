package com.bahamaeatsdriver.model_class.verify_otp

data class Body(
    val contactNo: String,
    val countryCode: String,
    val countryCodePhone: String,
    val created_at: String,
    val id: Int,
    val otp: Int,
    val otpVerified: Int,
    val updated_at: String
)