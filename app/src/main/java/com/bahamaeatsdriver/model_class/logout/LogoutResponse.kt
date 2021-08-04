package com.bahamaeatsdriver.model_class.logout

data class LogoutResponse(
    val body: Body,
    val code: Int,
    val message: String,
    val status: Boolean
)