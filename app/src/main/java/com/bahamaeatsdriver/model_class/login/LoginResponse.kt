package com.bahamaeatsdriver.model_class.login

data class LoginResponse(
    val body: Body,
    val code: Int,
    val message: String,
    val status: Boolean
)