package com.bahamaeatsdriver.model_class.get_current_ride


import com.google.gson.annotations.SerializedName

data class Body(
    @SerializedName("addressId")
    val addressId: String,
    @SerializedName("completedAt")
    val completedAt: Any,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("currentLat")
    val currentLat: String,
    @SerializedName("currentLong")
    val currentLong: String,
    @SerializedName("driverId")
    val driverId: Int,
    @SerializedName("fromLat")
    val fromLat: String,
    @SerializedName("fromLong")
    val fromLong: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("order")
    val order: Order,
    @SerializedName("orderId")
    val orderId: Int,
    @SerializedName("response")
    var response: Int,
    @SerializedName("restaurant")
    val restaurant: Restaurant,
    @SerializedName("restaurantId")
    val restaurantId: Int,
    @SerializedName("rideStatus")
    var rideStatus: Int,
    @SerializedName("toLat")
    val toLat: String,
    @SerializedName("toLong")
    val toLong: String,
    @SerializedName("updatedAt")
    val updatedAt: Any,
    @SerializedName("user")
    val user: User,
    @SerializedName("userAddress")
    val userAddress: UserAddress,
    @SerializedName("userId")
    val userId: Int
)