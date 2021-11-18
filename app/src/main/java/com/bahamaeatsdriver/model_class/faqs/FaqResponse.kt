package com.bahamaeatsdriver.model_class.faqs


import com.google.gson.annotations.SerializedName

data class FaqResponse(
    @SerializedName("body")
    val body: List<Body>,
    @SerializedName("code")
    val code: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Boolean
)