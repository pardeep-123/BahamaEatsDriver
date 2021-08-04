package com.bahamaeatsdriver.model_class.driver_payments

data class Body(
    val earning: Earning,
    val order: ArrayList<Order>
)