package com.bahamaeatsdriver.model_class.accept_reject_ride

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
    val netAmount: String,
    val orderDate: String,
    val orderDetails: List<OrderDetail>,
    val orderType: Int,
    val paymentMethod: Int,
    val paymentStatus: Int,
    val promoCode: String,
    val promoDiscount: String,
    val restaurantId: Int,
    val scheduledDatetime: String,
    val scheduledTimestamp: Int,
    val serviceFee: String,
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