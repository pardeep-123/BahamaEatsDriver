package com.bahamaeatsdriver.model_class.driver_deals

data class DriverDealsResponse(
    val body: Body,
    val code: Int,
    val message: String,
    val status: Boolean
)

data class Body(
    val all: ArrayList<All>,
    val favourite: ArrayList<All>
)

data class All(
    val complete_rides: Int,
    val created_at: String,
    val deal_type: Int,
    val description: String,
    val discount_description: String,
    val discount_title: String,
    val expire_date: Int,
    var favouritecount: Int,
    val hide_status: Int,
    val id: Int,
    val image: String,
    val latitude: String,
    val location: String,
    val longitude: String,
    val name: String,
    val number_of_order: Int,
    val status: Int,
    val updated_at: String
)
