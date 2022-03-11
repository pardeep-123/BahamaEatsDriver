package com.bahamaeatsdriver.model_class.like_merchant_deal

import com.bahamaeatsdriver.model_class.driver_deals.All
import com.google.gson.annotations.SerializedName

data class LikeUnlikeDealResponse(
    @SerializedName("body")
    val body: Body,
    @SerializedName("code")
    val code: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Boolean
) {
    data class Body(
        @SerializedName("all")
        val all: ArrayList<All>,
        @SerializedName("favourite")
        val favourite: ArrayList<All>
    )
}