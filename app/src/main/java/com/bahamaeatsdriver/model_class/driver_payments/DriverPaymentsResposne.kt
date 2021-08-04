package com.bahamaeatsdriver.model_class.driver_payments

data class DriverPaymentsResposne(
    val body: Body,
    val code: Int,
    val message: String,
    val status: Boolean
)