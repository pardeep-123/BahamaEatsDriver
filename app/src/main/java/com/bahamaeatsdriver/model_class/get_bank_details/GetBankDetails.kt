package com.bahamaeatsdriver.model_class.get_bank_details

import com.google.gson.annotations.SerializedName

data class GetBankDetails(
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
    @SerializedName("account_holder_name")
    val accountHolderName: String,
    @SerializedName("account_number")
    val accountNumber: String,
    @SerializedName("account_type")
    val accountType: String,
    @SerializedName("bank_name")
    val bankName: String,
    @SerializedName("branch_name")
    val branchName: String,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("dob")
    val dob: String,
    @SerializedName("driver_id")
    val driverId: Int,
    @SerializedName("email")
    val email: String,
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("is_verified")
    val isVerified: Int,
    @SerializedName("last_name")
    val lastName: String,
    @SerializedName("nib_number")
    val nibNumber: String,
    @SerializedName("updatedAt")
    val updatedAt: String
)