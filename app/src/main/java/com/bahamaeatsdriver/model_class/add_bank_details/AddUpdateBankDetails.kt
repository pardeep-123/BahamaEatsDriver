package com.bahamaeatsdriver.model_class.add_bank_details


import com.google.gson.annotations.SerializedName

data class AddUpdateBankDetails(
    @SerializedName("body")
    val body: Body,
    @SerializedName("code")
    val code: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Boolean
)