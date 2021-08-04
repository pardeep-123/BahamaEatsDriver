package com.bahamaeatsdriver.model_class.change_ride_status

import java.io.Serializable

data class Order(
    val address: String,
    val addressId: Int,
    val cartFee: Double,
    val createdAt: String,
    val deliveryFee: Double,
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
    val promoDiscount: Double,
    val restaurantId: Int,
    val scheduledDatetime: String,
    val scheduledTimestamp: Int,
    val serviceFee: Double,
    val serviceFeePercentage: String,
    val specialRequest: String,
    val status: Int,
    val tax: Double,
    val taxPercentage: String,
    val tip: Double,
    val totalAmount: String,
    val transactionId: Int,
    val transactionNo: String,
    val updatedAt: String,
    val userId: Int
): Serializable