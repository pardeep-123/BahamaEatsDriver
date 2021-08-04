package com.bahamaeatsdriver.model_class.forgot_password

data class ForgotPasswordResponse(
    val body: Body,
    val code: Int,
    val message: String,
    val status: Boolean
)