package com.bahamaeatsdriver.model_class.verify_otp

data class VerifyOtpResponse(
    val body: Body,
    val code: Int,
    val message: String,
    val status: Boolean
)