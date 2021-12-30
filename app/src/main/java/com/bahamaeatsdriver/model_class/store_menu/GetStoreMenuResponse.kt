package com.bahamaeatsdriver.model_class.store_menu


import com.google.gson.annotations.SerializedName

data class GetStoreMenuResponse(
    @SerializedName("body")
    val body: Body,
    @SerializedName("code")
    val code: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Boolean
)