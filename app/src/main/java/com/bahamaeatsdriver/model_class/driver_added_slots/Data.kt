package com.bahamaeatsdriver.model_class.driver_added_slots


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("closeTime")
    val closeTime: String,
    @SerializedName("createdAt")
    val createdAt: Any,
    @SerializedName("day")
    val day: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("openTime")
    val openTime: String,
    @SerializedName("updatedAt")
    val updatedAt: String
)