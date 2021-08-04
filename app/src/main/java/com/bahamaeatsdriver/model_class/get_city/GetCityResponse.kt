package com.bahamaeatsdriver.model_class.get_city

data class GetCityResponse(
    val body: List<Body>,
    val code: Int,
    val message: String,
    val status: Boolean
)