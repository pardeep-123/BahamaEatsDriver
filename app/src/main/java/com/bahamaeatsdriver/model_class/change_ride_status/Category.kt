package com.bahamaeatsdriver.model_class.change_ride_status

import java.io.Serializable

data class Category(
    val addOnArray: List<AddOnArray>,
    val categoryName: String,
    val id: Int
): Serializable