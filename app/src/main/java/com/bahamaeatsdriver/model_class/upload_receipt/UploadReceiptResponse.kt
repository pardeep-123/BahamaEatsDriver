package com.bahamaeatsdriver.model_class.upload_receipt


import com.google.gson.annotations.SerializedName

data class UploadReceiptResponse(
    @SerializedName("body")
    val body: String,
    @SerializedName("code")
    val code: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Boolean
)