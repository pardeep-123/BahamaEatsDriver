package com.bahamaeatsdriver.model_class.update_latitudeLongitude

data class UpdateDriverLatLongResponse(
    val body: Body,
    val code: Int,
    val message: String,
    val status: Boolean
)