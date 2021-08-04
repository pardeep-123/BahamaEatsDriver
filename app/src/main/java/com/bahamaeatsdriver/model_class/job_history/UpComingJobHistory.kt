package com.bahamaeatsdriver.model_class.job_history

data class UpComingJobHistory(
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
    val rating: Int,
    val response: Int,
    val restaurant: RestaurantX,
    val restaurantId: Int,
    val rideStatus: Int,
    val toLat: String,
    val toLong: String,
    val updatedAt: Any,
    val userId: Int,
    val vehicle: String
)