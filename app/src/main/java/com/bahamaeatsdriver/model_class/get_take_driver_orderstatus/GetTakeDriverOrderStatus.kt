package com.bahamaeatsdriver.model_class.get_take_driver_orderstatus

import com.google.gson.annotations.SerializedName

data class GetTakeDriverOrderStatus(
    @SerializedName("body")
    val body: Body,
    @SerializedName("code")
    val code: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Boolean
)

data class Body(
    @SerializedName("email")
    val email: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("selectedSlots")
    val selectedSlots: MutableList<SelectedSlot>,
    @SerializedName("takeOrderStatus")
    val takeOrderStatus: Int,
    @SerializedName("username")
    val username: String
)

data class SelectedSlot(
    @SerializedName("closeTime")
    val closeTime: String,
    @SerializedName("createdAt")
    val createdAt: Any,
    @SerializedName("day")
    val day: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("isSelected")
    val isSelected: Int,
    @SerializedName("openTime")
    val openTime: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("updatedAt")
    val updatedAt: String
)