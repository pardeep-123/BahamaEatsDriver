package com.bahamaeatsdriver.model_class.driver_deals

import com.google.gson.annotations.SerializedName

data class DriverDealsResponse(
    @SerializedName("body")
    val body: ArrayList<Body>,
    @SerializedName("code")
    val code: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Boolean
)

data class Body(
    @SerializedName("complete_rides")
    val completeRides: Int,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("deal_type")
    val dealType: Int,
    @SerializedName("description")
    val description: String,
    @SerializedName("discount_description")
    val discountDescription: String,
    @SerializedName("discount_title")
    val discountTitle: String,
    @SerializedName("expire_date")
    val expireDate: Int,
    @SerializedName("favouritecount")
    var favouritecount: Int,
    @SerializedName("hide_status")
    val hideStatus: Int,
    @SerializedName("id")
    val id: Int,
    @SerializedName("image")
    val image: String,
    @SerializedName("latitude")
    val latitude: String,
    @SerializedName("location")
    val location: String,
    @SerializedName("longitude")
    val longitude: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("number_of_order")
    val numberOfOrder: Int,
    @SerializedName("status")
    val status: Int,
    @SerializedName("updated_at")
    val updatedAt: String
)