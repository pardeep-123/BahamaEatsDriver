package com.bahamaeatsdriver.model_class.job_history

data class PastJobHistory(
    val addressId: String,
    val completedAt: String,
    val createdAt: String,
    val currentLat: String,
    val currentLong: String,
    val driverId: Int,
    val fromLat: String,
    val fromLong: String,
    val id: Int,
    val order: Order,
    val orderId: Int,
    val rating: Int,
    val response: Int,
    val restaurant: Restaurant,
    val restaurantId: Int,
    val rideStatus: Int,
    val toLat: String,
    val toLong: String,
    val updatedAt: Any,
    val userId: Int,
    val vehicle: String
)