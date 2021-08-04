package com.bahamaeatsdriver.model_class.get_driver_license

data class GetDriverLicense(
    val body: Body,
    val code: Int,
    val message: String,
    val status: Boolean
)