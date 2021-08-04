package com.bahamaeatsdriver.model_class.job_history

data class RestaurantX(
    val acceptCash: String,
    val address: String,
    val city: String,
    val closeTime: String,
    val commissionType: Int,
    val country: String,
    val createdAt: String,
    val cuisineId: String,
    val deliveryStatus: Int,
    val deliveryType: Int,
    val description: String,
    val email: String,
    val featured: Int,
    val hideStatus: Int,
    val id: Int,
    val image: String,
    val isPopular: Int,
    val latitude: String,
    val longitude: String,
    val minDelivery: String,
    val minOrderAmount: String,
    val name: String,
    val openTime: String,
    val password: String,
    val paymentType: String,
    val phone: String,
    val postalCode: String,
    val province: String,
    val rememberToken: Any,
    val restaurantImages: List<RestaurantImageX>,
    val serviceFee: String,
    val status: Int,
    val tax: String,
    val totalCredit: String,
    val updatedAt: String
)