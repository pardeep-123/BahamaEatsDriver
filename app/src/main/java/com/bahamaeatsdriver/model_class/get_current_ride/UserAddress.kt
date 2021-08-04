package com.bahamaeatsdriver.model_class.get_current_ride


import com.google.gson.annotations.SerializedName

data class UserAddress(
    @SerializedName("address")
    val address: String,
    @SerializedName("alternateMobileNumber")
    val alternateMobileNumber: String,
    @SerializedName("city")
    val city: String,
    @SerializedName("cityId")
    val cityId: Int,
    @SerializedName("completeAddress")
    val completeAddress: String,
    @SerializedName("country")
    val country: String,
    @SerializedName("countryCode")
    val countryCode: String,
    @SerializedName("countryCodeAlternateMobileNumber")
    val countryCodeAlternateMobileNumber: String,
    @SerializedName("countryId")
    val countryId: Int,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("deliveryInstructions")
    val deliveryInstructions: String,
    @SerializedName("firstName")
    val firstName: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("isDefault")
    val isDefault: Int,
    @SerializedName("lastName")
    val lastName: String,
    @SerializedName("latitude")
    val latitude: Double,
    @SerializedName("longitude")
    val longitude: Double,
    @SerializedName("postalCode")
    val postalCode: Int,
    @SerializedName("provience")
    val provience: String,
    @SerializedName("provienceId")
    val provienceId: Int,
    @SerializedName("streetName")
    val streetName: String,
    @SerializedName("updatedAt")
    val updatedAt: String,
    @SerializedName("userId")
    val userId: Int
)