package com.bahamaeatsdriver.model_class.delete_id_card_image

data class Body(
    val address: String,
    val backPhoto: String,
    val createdAt: String,
    val dob: String,
    val driverId: Int,
    val firstName: String,
    val frontPhoto: String,
    val id: Int,
    val idNumber: String,
    val issueDate: String,
    val lastName: String,
    val updatedAt: String
)