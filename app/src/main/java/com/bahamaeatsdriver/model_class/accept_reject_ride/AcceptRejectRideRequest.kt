package com.bahamaeatsdriver.model_class.accept_reject_ride

data class AcceptRejectRideRequest(
    val body: Body,
    val code: Int,
    val message: String,
    val status: Boolean
)