package com.bahamaeatsdriver.model_class.notification_status_change

data class UpdateNotificationStatus(
    val body: Body,
    val code: Int,
    val message: String,
    val status: Boolean
)