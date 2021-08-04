package com.bahamaeatsdriver.model_class.profile_details

import java.io.Serializable

data class DriverProfileDetailsResposne(
    val body: Body,
    val code: Int,
    val message: String,
    val status: Boolean
): Serializable