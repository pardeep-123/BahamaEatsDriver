package com.bahamaeatsdriver.model_class.get_current_ride


import com.google.gson.annotations.SerializedName

data class AddOnArray(
    @SerializedName("addon")
    val addon: String,
    @SerializedName("price")
    val price: String,
    @SerializedName("quantity")
    val quantity: Int
)