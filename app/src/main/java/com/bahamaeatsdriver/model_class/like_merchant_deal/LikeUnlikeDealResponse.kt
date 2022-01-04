package com.bahamaeatsdriver.model_class.like_merchant_deal

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
)

data class Body(
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("driver_id")
    val driverId: Int,
    @SerializedName("id")
    val id: Int,
    @SerializedName("merchant_deal_id")
    val merchantDealId: Int,
    @SerializedName("status")
    val status: Int,
    @SerializedName("updatedAt")
    val updatedAt: String
)