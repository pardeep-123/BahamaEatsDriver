package com.bahamaeatsdriver.model_class.get_take_driver_orderstatus

data class Body(
    val email: String,
    val id: Int,
    val takeOrderStatus: Int,
    val username: String
)