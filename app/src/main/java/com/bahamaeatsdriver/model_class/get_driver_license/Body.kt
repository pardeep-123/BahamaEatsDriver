package com.bahamaeatsdriver.model_class.get_driver_license

import java.io.Serializable

data class Body(
        val backPhoto: String,
        val createdAt: String,
        val dob: String,
        val driverId: Int,
        val expiryDate: String,
        val frontPhoto: String,
        val id: Int,
        val issueDate: String,
        val licenseNo: String,
        val licenseType: String,
        val nationality: String,
        val updatedAt: String
) : Serializable