package com.bahamaeatsdriver.model_class.update_phone_number

data class UpdatePhoneNumberResponse(
    val body: Body,
    val code: Int,
    val message: String,
    val status: Boolean
)