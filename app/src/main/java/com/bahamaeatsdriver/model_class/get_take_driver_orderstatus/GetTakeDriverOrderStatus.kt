package com.bahamaeatsdriver.model_class.get_take_driver_orderstatus

data class GetTakeDriverOrderStatus(
    val body: Body,
    val code: Int,
    val message: String,
    val status: Boolean
)