package com.bahamaeatsdriver.model_class.slots


import com.google.gson.annotations.SerializedName

data class DriverSlots(
    @SerializedName("body")
    val body: ArrayList<Body>,
    @SerializedName("code")
    val code: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Boolean
)