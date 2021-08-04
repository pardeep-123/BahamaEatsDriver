package com.bahamaeatsdriver.model_class.get_rider_details

data class GetRiderDetailsResponse(
    val body: Body,
    val code: Int,
    val message: String,
    val status: Boolean
)