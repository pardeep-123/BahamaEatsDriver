package com.bahamaeatsdriver.model_class.driver_added_slots


import com.google.gson.annotations.SerializedName

data class DriverAddedSlotList(
    @SerializedName("body")
    val body: List<Body>,
    @SerializedName("code")
    val code: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Boolean
)