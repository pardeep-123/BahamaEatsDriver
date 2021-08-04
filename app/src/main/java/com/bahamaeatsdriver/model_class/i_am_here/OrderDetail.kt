package com.bahamaeatsdriver.model_class.i_am_here

data class OrderDetail(
    val addons: String,
    val createdAt: String,
    val id: Int,
    val itemDescription: String,
    val itemId: Int,
    val itemImage: String,
    val itemImagePath: String,
    val itemMenuId: Int,
    val itemName: String,
    val orderId: Int,
    val price: String,
    val quantity: Int,
    val restaurantId: Int,
    val updatedAt: String
)