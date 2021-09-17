package com.bahamaeatsdriver.model_class.notification_listing

import com.google.gson.annotations.SerializedName


data class Body(
    @SerializedName("code")
    val code: Int,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("data")
    val `data`: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("isDeleted")
    val isDeleted: Int,
    @SerializedName("isRated")
    val isRated: String,
    @SerializedName("isRead")
    val isRead: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("orderDetails")
    val orderDetails: List<Any>,
    @SerializedName("orderId")
    val orderId: Int,
    @SerializedName("receiverId")
    val receiverId: Int,
    @SerializedName("receiverType")
    val receiverType: Int,
    @SerializedName("restaurantId")
    val restaurantId: String,
    @SerializedName("restaurantImage")
    val restaurantImage: String,
    @SerializedName("rideRequestId")
    val rideRequestId: Int,
    @SerializedName("senderId")
    val senderId: Int,
    @SerializedName("senderType")
    val senderType: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("updatedAt")
    val updatedAt: String
)