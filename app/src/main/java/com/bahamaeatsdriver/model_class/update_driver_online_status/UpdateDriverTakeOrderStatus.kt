package com.bahamaeatsdriver.model_class.update_driver_online_status

data class UpdateDriverTakeOrderStatus(
    val body: Body,
    val code: Int,
    val message: String,
    val status: Boolean
)