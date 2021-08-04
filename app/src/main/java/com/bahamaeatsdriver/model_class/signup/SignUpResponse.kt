package com.bahamaeatsdriver.model_class.signup

data class SignUpResponse(
    val body: Body,
    val code: Int,
    val message: String,
    val status: Boolean
)