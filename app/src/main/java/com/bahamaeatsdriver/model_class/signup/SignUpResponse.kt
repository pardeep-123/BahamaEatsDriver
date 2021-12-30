package com.bahamaeatsdriver.model_class.signup

import com.google.gson.annotations.SerializedName

data class SignUpResponse(
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
    @SerializedName("isEmailAlreadyExist")
    val isEmailAlreadyExist: Int,
    @SerializedName("isPhoneExist")
    val isPhoneExist: Int,
    @SerializedName("user")
    val user: User
)

data class User(
    @SerializedName("city")
    val city: String,
    @SerializedName("contactNo")
    val contactNo: String,
    @SerializedName("country")
    val country: String,
    @SerializedName("countryCode")
    val countryCode: String,
    @SerializedName("countryCodePhone")
    val countryCodePhone: String,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("deviceToken")
    val deviceToken: String,
    @SerializedName("deviceType")
    val deviceType: Int,
    @SerializedName("dob")
    val dob: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("firstName")
    val firstName: String,
    @SerializedName("fullName")
    val fullName: String,
    @SerializedName("gender")
    val gender: Int,
    @SerializedName("id")
    val id: Int,
    @SerializedName("image")
    val image: String,
    @SerializedName("isApproved")
    val isApproved: Int,
    @SerializedName("isDocumentVerified")
    val isDocumentVerified: Int,
    @SerializedName("isLicenceExists")
    val isLicenceExists: Int,
    @SerializedName("isNotification")
    val isNotification: Int,
    @SerializedName("lastName")
    val lastName: String,
    @SerializedName("loginType")
    val loginType: Int,
    @SerializedName("referrals_code")
    val referralsCode: String,
    @SerializedName("socialId")
    val socialId: String,
    @SerializedName("socialType")
    val socialType: Int,
    @SerializedName("token")
    val token: String,
    @SerializedName("unique_time")
    val uniqueTime: String,
    @SerializedName("updated_at")
    val updatedAt: String,
    @SerializedName("username")
    val username: String
)