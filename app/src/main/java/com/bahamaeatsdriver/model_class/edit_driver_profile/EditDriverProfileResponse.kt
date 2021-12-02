package com.bahamaeatsdriver.model_class.edit_driver_profile

import com.google.gson.annotations.SerializedName


data class EditDriverProfileResponse(
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
    @SerializedName("address")
    val address: String,
    @SerializedName("availability")
    val availability: Int,
    @SerializedName("carInsurance")
    val carInsurance: String,
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
    @SerializedName("default_time_zone")
    val defaultTimeZone: String,
    @SerializedName("deviceToken")
    val deviceToken: String,
    @SerializedName("deviceType")
    val deviceType: Int,
    @SerializedName("dob")
    val dob: String,
    @SerializedName("driverIdentity")
    val driverIdentity: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("firstName")
    val firstName: String,
    @SerializedName("forgotPasswordHash")
    val forgotPasswordHash: String,
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
    @SerializedName("isCarInsuranceApproved")
    val isCarInsuranceApproved: Int,
    @SerializedName("isDocumentVerified")
    val isDocumentVerified: Int,
    @SerializedName("isDriverIdApproved")
    val isDriverIdApproved: Int,
    @SerializedName("isDriverLicenseApproved")
    val isDriverLicenseApproved: Int,
    @SerializedName("isNotification")
    val isNotification: Int,
    @SerializedName("is_online")
    val isOnline: Int,
    @SerializedName("isPoliceReportApproved")
    val isPoliceReportApproved: Int,
    @SerializedName("lastName")
    val lastName: String,
    @SerializedName("latitude")
    val latitude: String,
    @SerializedName("loginType")
    val loginType: Int,
    @SerializedName("longitude")
    val longitude: String,
    @SerializedName("manually_on_off")
    val manuallyOnOff: String,
    @SerializedName("otp")
    val otp: Int,
    @SerializedName("otpVerified")
    val otpVerified: Int,
    @SerializedName("paymentStatus")
    val paymentStatus: Int,
    @SerializedName("policeRecord")
    val policeRecord: String,
    @SerializedName("postalCode")
    val postalCode: Int,
    @SerializedName("province")
    val province: String,
    @SerializedName("socialId")
    val socialId: String,
    @SerializedName("socialType")
    val socialType: Int,
    @SerializedName("status")
    val status: Int,
    @SerializedName("takeOrderStatus")
    val takeOrderStatus: Int,
    @SerializedName("unique_time")
    val uniqueTime: String,
    @SerializedName("updatedAt")
    val updatedAt: String,
    @SerializedName("username")
    val username: String,
    @SerializedName("wrongAttemptBlock")
    val wrongAttemptBlock: String,
    @SerializedName("wrongAttemptCount")
    val wrongAttemptCount: String
)