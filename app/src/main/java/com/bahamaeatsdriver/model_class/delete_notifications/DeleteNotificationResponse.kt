package com.bahamaeatsdriver.model_class.delete_notifications

import com.google.gson.annotations.SerializedName


data class DeleteNotificationResponse(
    @SerializedName("body")
    val body: Body,
    @SerializedName("code")
    val code: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Boolean
)