package com.bahamaeatsdriver.model_class.change_ride_status

import java.io.Serializable

data class ChangeRideStatusResponse(
    val body: Body,
    val code: Int,
    val message: String,
    val status: Boolean
):Serializable