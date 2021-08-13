package com.bahamaeatsdriver.model_class.get_take_driver_orderstatus
import com.google.gson.annotations.SerializedName


data class GetTakeDriverOrderStatus(
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
    @SerializedName("email")
    val email: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("takeOrderStatus")
    val takeOrderStatus: Int,
    @SerializedName("username")
    val username: String
)