package com.bahamaeatsdriver.model_class.change_ride_status

import java.io.Serializable

data class Body(
    val addressId: String,
    val completedAt: String,
    val createdAt: String,

    val dropofftime: String,
    val pickuptime: String,

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
): Serializable