package com.bahamaeatsdriver.model_class.get_current_ride


import com.google.gson.annotations.SerializedName

data class Restaurant(
    @SerializedName("acceptCash")
    val acceptCash: String,
    @SerializedName("address")
    val address: String,
    @SerializedName("city")
    val city: String,
    @SerializedName("closeTime")
    val closeTime: String,
    @SerializedName("commissionType")
    val commissionType: Int,
    @SerializedName("country")
    val country: String,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("cuisineId")
    val cuisineId: String,
    @SerializedName("deliveryStatus")
    val deliveryStatus: Int,
    @SerializedName("deliveryType")
    val deliveryType: Int,
    @SerializedName("description")
    val description: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("featured")
    val featured: Int,
    @SerializedName("hideStatus")
    val hideStatus: Int,
    @SerializedName("id")
    val id: Int,
    @SerializedName("image")
    val image: String,
    @SerializedName("isBe")
    val isBe: String,
    @SerializedName("high_risk")
    val highRisk: String,
    @SerializedName("isPopular")
    val isPopular: Int,
    @SerializedName("latitude")
    val latitude: Double,
    @SerializedName("longitude")
    val longitude: Double,
    @SerializedName("minDelivery")
    val minDelivery: String,
    @SerializedName("minOrderAmount")
    val minOrderAmount: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("offerDescription")
    val offerDescription: String,
    @SerializedName("offerTitle")
    val offerTitle: String,
    @SerializedName("openTime")
    val openTime: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("paymentType")
    val paymentType: String,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("postalCode")
    val postalCode: String,
    @SerializedName("province")
    val province: String,
    @SerializedName("rememberToken")
    val rememberToken: String,
    @SerializedName("serviceFee")
    val serviceFee: String,
    @SerializedName("status")
    val status: Int,
    @SerializedName("tax")
    val tax: String,
    @SerializedName("totalCredit")
    val totalCredit: String,
    @SerializedName("updatedAt")
    val updatedAt: String
)