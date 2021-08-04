package com.bahamaeatsdriver.model_class.update_phone_number

data class Body(
    val contactNo: String,
    val countryCode: String,
    val countryCodePhone: String,
    val created_at: String,
    val id: Int,
    val otp: Int,
    val updated_at: String
)