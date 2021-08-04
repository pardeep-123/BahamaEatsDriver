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