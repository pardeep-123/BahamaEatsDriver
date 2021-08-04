package com.bahamaeatsdriver.model_class.job_history_details

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
    val itemSpecialRequest: String,
    val orderId: Int,
    val price: String,
    val quantity: Int,
    val restaurantId: Int,
    val updatedAt: String
)