package com.bahamaeatsdriver.model_class.driver_details

data class DriverDetails(val driverId: Int, val email: String, val fullName: String, val token: String, val driverIdentity: String, val image: String, val isApproved: Int, val city: String, val availability: Int , var takeOrderStatus: Int)