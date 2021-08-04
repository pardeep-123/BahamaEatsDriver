package com.bahamaeatsdriver.model_class.notification_status_change

data class Body(
    val createdAt: String,
    val email: String,
    val fullName: String,
    val id: Int,
    val isNotification: Int,
    val updatedAt: String
)