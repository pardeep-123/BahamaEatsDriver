package com.bahamaeatsdriver.model_class.job_history

data class Order(
    val address: String,
    val addressId: Int,
    val cartFee: String,
    val createdAt: String,
    val deliveryFee: String,
    val foodPrice: String,
    val id: Int,
    val isDelivery: Int,
    val latitude: String,
    val longitude: String,
    val netAmount: Double,
    val orderDate: String,
    val driverNetAmount: String,
    val userDeliveryFee: String,
    val orderDetails: List<OrderDetail>,
    val orderRating: String,
    val orderType: Int,
    val paymentMethod: Int,
    val paymentStatus: Int,
    val preparationTime: String,
    val promoCode: String,
    val promoDiscount: String,
    val restaurantId: Int,
    val scheduledDatetime: String,
    val scheduledTimestamp: Int,
    val serviceFee: Double,
    val serviceFeePercentage: String,
    val specialRequest: String,
    val status: Int,
    val tax: String,
    val taxPercentage: String,
    val tip: String,
    val totalAmount: String,
    val transactionId: Int,
    val transactionNo: String,
    val updatedAt: String,
    val userId: Int
)