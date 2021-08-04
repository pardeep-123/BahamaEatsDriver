package com.bahamaeatsdriver.model_class.get_otp

data class GetOtpResponse(
    val body: Body,
    val code: Int,
    val message: String,
    val status: Boolean
)