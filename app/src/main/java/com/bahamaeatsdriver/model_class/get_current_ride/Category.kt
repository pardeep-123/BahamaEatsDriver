package com.bahamaeatsdriver.model_class.get_current_ride


import com.google.gson.annotations.SerializedName

data class Category(
    @SerializedName("addOnArray")
    val addOnArray: List<AddOnArray>,
    @SerializedName("categoryName")
    val categoryName: String,
    @SerializedName("id")
    val id: Int
)