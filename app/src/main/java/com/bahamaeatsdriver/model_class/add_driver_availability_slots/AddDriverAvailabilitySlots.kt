package com.bahamaeatsdriver.model_class.add_driver_availability_slots


import com.google.gson.annotations.SerializedName

data class AddDriverAvailabilitySlots(
    @SerializedName("body")
    val body: Body,
    @SerializedName("code")
    val code: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Boolean
)