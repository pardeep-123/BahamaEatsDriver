package com.bahamaeatsdriver.model_class.update_driver_online_status

data class Body(
    val email: String,
    val id: Int,
    val takeOrderStatus: Int,
    val username: String
)