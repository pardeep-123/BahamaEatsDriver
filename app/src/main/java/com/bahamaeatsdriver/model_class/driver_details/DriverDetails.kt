package com.bahamaeatsdriver.model_class.driver_details

data class DriverDetails(
    val email: String,
    val firstName: String,
    val id: Int,
    var image: String,
    var dob: String,
    var gender: Int,
    var driver_referrals_amount: String,
    var referrals_code: String,
    val isApproved: Int,
    val isDocumentVerified: Int,
    val isLicenceExists: Int,
    val lastName: String,
    var fullName: String,
    val token: String,
    val username: String,
    var isNotification: Int,
    val countryCodePhone: String,
    val city: String,
    val countryCode: String,
    val contactNo: String
)