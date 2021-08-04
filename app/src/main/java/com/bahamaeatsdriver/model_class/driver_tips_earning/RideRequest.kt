package com.bahamaeatsdriver.model_class.driver_tips_earning

data class RideRequest(
    val addressId: String,
    val completedAt: String,
    val createdAt: String,
    val currentLat: String,
    val currentLong: String,
    val driverId: String,
    val fromLat: String,
    val fromLong: String,
    val id: Int,
    val order: Order,
    val orderId: Int,
    val response: Int,
    val restaurantId: Int,
    val rideStatus: Int,
    val toLat: String,
    val toLong: String,
    val updatedAt: String,
    val user: User,
    val userId: Int
)