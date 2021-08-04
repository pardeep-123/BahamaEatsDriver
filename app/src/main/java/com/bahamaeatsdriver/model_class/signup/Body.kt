package com.bahamaeatsdriver.model_class.signup

data class Body(
    val isEmailAlreadyExist: Int,
    val isPhoneExist: Int,
    val user: User
)