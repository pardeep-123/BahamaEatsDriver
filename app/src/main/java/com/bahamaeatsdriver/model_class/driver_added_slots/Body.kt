package com.bahamaeatsdriver.model_class.driver_added_slots


import com.google.gson.annotations.SerializedName

data class Body(
    @SerializedName("data")
    val `data`: List<Data>,
    @SerializedName("day")
    val day: Int
)