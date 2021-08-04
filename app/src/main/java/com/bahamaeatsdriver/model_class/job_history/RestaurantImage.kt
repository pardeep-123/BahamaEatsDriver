package com.bahamaeatsdriver.model_class.job_history

data class RestaurantImage(
    val createdAt: String,
    val id: Int,
    val image: String,
    val restaurantId: Int,
    val updatedAt: String
)