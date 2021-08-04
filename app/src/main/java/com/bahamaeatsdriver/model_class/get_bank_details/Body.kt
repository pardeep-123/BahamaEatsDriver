package com.bahamaeatsdriver.model_class.get_bank_details


import com.google.gson.annotations.SerializedName

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
    @SerializedName("driver_id")
    val driverId: Int,
    @SerializedName("id")
    val id: Int,
    @SerializedName("is_verified")
    val isVerified: Int,
    @SerializedName("updatedAt")
    val updatedAt: String
)