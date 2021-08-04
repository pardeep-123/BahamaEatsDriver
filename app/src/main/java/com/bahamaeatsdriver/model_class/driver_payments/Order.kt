package com.bahamaeatsdriver.model_class.driver_payments

data class Order(
    val addressId: String,
    val completedAt: Any,
    val createdAt: String,
    val currentLat: String,
    val currentLong: String,
    val driverId: Int,
    val fromLat: String,
    val fromLong: String,
    val id: Int,
    val order: OrderX,
    val orderId: Int,
    val response: Int,
    val restaurantId: Int,
    val rideStatus: Int,
    val toLat: String,
    val toLong: String,
    val updatedAt: Any,
    val user: User,
    val userId: Int
)