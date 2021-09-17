package com.bahamaeatsdriver.model_class.notification_listing


import com.google.gson.annotations.SerializedName

data class NotificationListingResponse(
    @SerializedName("body")
    val body: ArrayList<Body>,
    @SerializedName("code")
    val code: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Boolean
)