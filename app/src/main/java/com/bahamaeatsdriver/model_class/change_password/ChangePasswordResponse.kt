package com.bahamaeatsdriver.model_class.change_password

data class ChangePasswordResponse(
    val body: Body,
    val code: Int,
    val message: String,
    val status: Boolean
)