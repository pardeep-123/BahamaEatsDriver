package com.bahamaeatsdriver.model_class.slots


import com.google.gson.annotations.SerializedName

data class Body(
    @SerializedName("availableSlotList")
    val availableSlotList: ArrayList<AvailableSlot>,
    @SerializedName("dayName")
    val dayName: String,
    @SerializedName("id")
    val id: String
)