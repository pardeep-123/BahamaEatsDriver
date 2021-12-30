package com.bahamaeatsdriver.model_class.driver_payments

import com.bahamaeatsdriver.model_class.driver_tips_earning.RefferalData
import com.google.gson.annotations.SerializedName


data class DriverPaymentsResposne(
    @SerializedName("body")
    val body: Body,
    @SerializedName("code")
    val code: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Boolean
)

data class Body(
    @SerializedName("earning")
    val earning: Earning,
    @SerializedName("order")
    val order: ArrayList<Order>,
    @SerializedName("refferalData")
    val refferalData: ArrayList<RefferalData>
)

data class Earning(
    @SerializedName("referal_bonus")
    val referalBonus: String,
    @SerializedName("total_delivery_fee")
    val totalDeliveryFee: Double,
    @SerializedName("total_tip")
    val totalTip: Double
)

data class Order(
    @SerializedName("addressId")
    val addressId: String,
    @SerializedName("completedAt")
    val completedAt: String,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("currentLat")
    val currentLat: String,
    @SerializedName("currentLong")
    val currentLong: String,
    @SerializedName("driverId")
    val driverId: Int,
    @SerializedName("fromLat")
    val fromLat: String,
    @SerializedName("fromLong")
    val fromLong: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("order")
    val order: OrderX,
    @SerializedName("orderId")
    val orderId: Int,
    @SerializedName("response")
    val response: Int,
    @SerializedName("restaurantId")
    val restaurantId: Int,
    @SerializedName("ride_created_at")
    val rideCreatedAt: String,
    @SerializedName("rideStatus")
    val rideStatus: Int,
    @SerializedName("toLat")
    val toLat: String,
    @SerializedName("toLong")
    val toLong: String,
    @SerializedName("updatedAt")
    val updatedAt: String,
    @SerializedName("user")
    val user: User,
    @SerializedName("userId")
    val userId: Int
)


data class OrderX(
    @SerializedName("address")
    val address: String,
    @SerializedName("addressId")
    val addressId: Int,
    @SerializedName("atlantis_payment_status")
    val atlantisPaymentStatus: Int,
    @SerializedName("cartFee")
    val cartFee: Int,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("deliveredTimestamp")
    val deliveredTimestamp: Int,
    @SerializedName("deliveryFee")
    val deliveryFee: String,
    @SerializedName("deliveryFeePercentage")
    val deliveryFeePercentage: String,
    @SerializedName("deliverydeduction")
    val deliverydeduction: Deliverydeduction,
    @SerializedName("driverConfirmedTimestamp")
    val driverConfirmedTimestamp: Int,
    @SerializedName("driverNetAmount")
    val driverNetAmount: String,
    @SerializedName("driverTotalAmount")
    val driverTotalAmount: String,
    @SerializedName("foodOnTheWayTimestamp")
    val foodOnTheWayTimestamp: Int,
    @SerializedName("foodPrice")
    val foodPrice: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("isDelivery")
    val isDelivery: Int,
    @SerializedName("latitude")
    val latitude: String,
    @SerializedName("longitude")
    val longitude: String,
    @SerializedName("netAmount")
    val netAmount: String,
    @SerializedName("orderConfirmedTimestamp")
    val orderConfirmedTimestamp: Int,
    @SerializedName("order_created_at")
    val orderCreatedAt: String,
    @SerializedName("orderDate")
    val orderDate: String,
    @SerializedName("order_device_type")
    val orderDeviceType: Int,
    @SerializedName("orderLoyalityPoints")
    val orderLoyalityPoints: String,
    @SerializedName("order_number")
    val orderNumber: Int,
    @SerializedName("orderPlacedTimestamp")
    val orderPlacedTimestamp: Int,
    @SerializedName("orderType")
    val orderType: Int,
    @SerializedName("paymentMethod")
    val paymentMethod: Int,
    @SerializedName("paymentStatus")
    val paymentStatus: Int,
    @SerializedName("preparationTime")
    val preparationTime: String,
    @SerializedName("preparingOrderTimestamp")
    val preparingOrderTimestamp: Int,
    @SerializedName("promoCode")
    val promoCode: String,
    @SerializedName("promoDiscount")
    val promoDiscount: String,
    @SerializedName("receipt_amount")
    val receiptAmount: String,
    @SerializedName("receipt_number")
    val receiptNumber: String,
    @SerializedName("receiptUpload")
    val receiptUpload: String,
    @SerializedName("restaurantId")
    val restaurantId: Int,
    @SerializedName("scheduledDatetime")
    val scheduledDatetime: String,
    @SerializedName("scheduledTimestamp")
    val scheduledTimestamp: Int,
    @SerializedName("serviceFee")
    val serviceFee: Double,
    @SerializedName("serviceFeePercentage")
    val serviceFeePercentage: String,
    @SerializedName("specialRequest")
    val specialRequest: String,
    @SerializedName("status")
    val status: Int,
    @SerializedName("tax")
    val tax: Double,
    @SerializedName("taxPercentage")
    val taxPercentage: String,
    @SerializedName("temp_status")
    val tempStatus: Int,
    @SerializedName("tip")
    val tip: String,
    @SerializedName("tipsPercentage")
    val tipsPercentage: String,
    @SerializedName("tollServiceFee")
    val tollServiceFee: String,
    @SerializedName("tollServiceFeePercentage")
    val tollServiceFeePercentage: String,
    @SerializedName("totalAmount")
    val totalAmount: String,
    @SerializedName("transactionId")
    val transactionId: Int,
    @SerializedName("transactionNo")
    val transactionNo: String,
    @SerializedName("updatedAt")
    val updatedAt: String,
    @SerializedName("userDeliveryFee")
    val userDeliveryFee: String,
    @SerializedName("userId")
    val userId: Int,
    @SerializedName("userTip")
    val userTip: String,
    @SerializedName("userTollServiceFee")
    val userTollServiceFee: String
)

data class User(
    @SerializedName("countryCode")
    val countryCode: String,
    @SerializedName("countryCodePhone")
    val countryCodePhone: String,
    @SerializedName("deviceToken")
    val deviceToken: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("firstName")
    val firstName: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("lastName")
    val lastName: String,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("photo")
    val photo: String,
    @SerializedName("username")
    val username: String
)

data class Deliverydeduction(
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("deduction_type")
    val deductionType: Int,
    @SerializedName("delivery_deduction")
    val deliveryDeduction: String,
    @SerializedName("delivery_fee")
    val deliveryFee: Double,
    @SerializedName("delivery_received")
    val deliveryReceived: String,
    @SerializedName("driver_id")
    val driverId: Int,
    @SerializedName("id")
    val id: Int,
    @SerializedName("order_id")
    val orderId: Int,
    @SerializedName("reason")
    val reason: String,
    @SerializedName("updatedAt")
    val updatedAt: String
)

data class Driverdetails(
    @SerializedName("countryCodePhone")
    val countryCodePhone: String,
    @SerializedName("firstName")
    val firstName: String,
    @SerializedName("fullName")
    val fullName: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("lastName")
    val lastName: String
)