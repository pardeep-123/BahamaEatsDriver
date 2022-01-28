package com.bahamaeatsdriver.model_class.slots


import com.google.gson.annotations.SerializedName

data class AvailableSlot(
    @SerializedName("closeTime")
    val closeTime: String,
    @SerializedName("createdAt")
    val createdAt: Any,
    @SerializedName("day")
    val day: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("countOfOtherDriversWithSameSlot")
    val countOfOtherDriversWithSameSlot: Int,
    @SerializedName("openTime")
    val openTime: String,
    @SerializedName("updatedAt")
    val updatedAt: String,
    var isSelected:Boolean

)