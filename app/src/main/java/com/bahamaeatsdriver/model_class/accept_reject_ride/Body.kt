package com.bahamaeatsdriver.model_class.accept_reject_ride

data class Body(
    val addressId: String,
    val completedAt: String,
    val createdAt: String,
    val pickuptime: String,
    val dropofftime: String,
    val currentLat: String,
    val currentLong: String,
    val driver: Driver,
    val driverId: Int,
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
    val user: User,
    val userAddress: UserAddress,
    val userId: Int
)