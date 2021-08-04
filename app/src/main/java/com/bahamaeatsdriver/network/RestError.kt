package com.pipt.restApi

import com.google.gson.annotations.SerializedName

class RestError {
    @SerializedName("status")
    var status: String = "true"

    @SerializedName("message")
    var message: String? = null
}