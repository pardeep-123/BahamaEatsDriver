package com.bahamaeatsdriver.model_class.get_current_ride


import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("countryCode")
    val countryCode: String,
    @SerializedName("countryCodePhone")
    val countryCodePhone: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("firstName")
    val firstName: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("lastName")
    val lastName: String,
    @SerializedName("loginType")
    val loginType: Int,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("photo")
    val photo: String,
    @SerializedName("username")
    val username: String
)