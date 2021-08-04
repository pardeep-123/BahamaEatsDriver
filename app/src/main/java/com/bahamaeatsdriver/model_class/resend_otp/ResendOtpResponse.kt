package com.bahamaeatsdriver.model_class.resend_otp

data class ResendOtpResponse(
    val body: Body,
    val code: Int,
    val message: String,
    val status: Boolean
)