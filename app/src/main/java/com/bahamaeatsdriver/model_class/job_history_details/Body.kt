package com.bahamaeatsdriver.model_class.job_history_details

data class Body(
    val ToAddress: String,
    val addressId: String,
    val completedAt: String,
    val createdAt: String,
    val currentLat: String,
    val currentLong: String,
    val driverId: Int,
    val fromAddress: String,
    val fromLat: String,
    val fromLong: String,
    val id: Int,
    val order: Order,
    val orderId: Int,
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