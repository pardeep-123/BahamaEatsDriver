package com.bahamaeatsdriver.model_class.driver_support


import com.google.gson.annotations.SerializedName
data class DriverSupportResponse(
    @SerializedName("body")
    val body: Body,
    @SerializedName("code")
    val code: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Boolean
) {
    data class Body(
        @SerializedName("call_support_link")
        val callSupportLink: String,
        @SerializedName("chat_team_link")
        val chatTeamLink: String,
        @SerializedName("id")
        val id: Int
    )
}