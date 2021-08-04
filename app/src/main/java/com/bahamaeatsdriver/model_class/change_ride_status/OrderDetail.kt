package com.bahamaeatsdriver.model_class.change_ride_status

import java.io.Serializable

data class OrderDetail(
    val addons: String,
    val categories: List<Category>,
    val createdAt: String,
    val id: Int,
    val itemDescription: String,
    val itemId: Int,
    val itemImage: String,
    val itemImagePath: String,
    val itemMenuId: Int,
    val itemName: String,
    val orderId: Int,
    val price: Double,
    val quantity: Int,
    val restaurantId: Int,
    val updatedAt: String
): Serializable