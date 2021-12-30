package com.bahamaeatsdriver.model_class.get_current_ride


import com.google.gson.annotations.SerializedName

data class Order(
    @SerializedName("receiptUpload")
    val receiptUpload: String,
    @SerializedName("address")
    val address: String,
    @SerializedName("addressId")
    val addressId: Int,
    @SerializedName("cartFee")
    val cartFee: String,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("deliveredTimestamp")
    val deliveredTimestamp: Int,
    @SerializedName("deliveryFee")
    val deliveryFee: Double,
    @SerializedName("driverConfirmedTimestamp")
    val driverConfirmedTimestamp: Int,
    @SerializedName("foodOnTheWayTimestamp")
    val foodOnTheWayTimestamp: Int,
    @SerializedName("foodPrice")
    val foodPrice: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("order_number")
    val order_number: Int,
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
    @SerializedName("orderDate")
    val orderDate: String,
    @SerializedName("orderDetails")
    val orderDetails: List<OrderDetail>,
    @SerializedName("orderPlacedTimestamp")
    val orderPlacedTimestamp: Int,
    @SerializedName("orderType")
    val orderType: Int,
    @SerializedName("paymentMethod")
    val paymentMethod: String,
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
    val tax: String,
    @SerializedName("taxPercentage")
    val taxPercentage: String,
    @SerializedName("tip")
    val tip: Double,
    @SerializedName("totalAmount")
    val totalAmount: String,
    @SerializedName("transactionId")
    val transactionId: Int,
    @SerializedName("transactionNo")
    val transactionNo: String,
    @SerializedName("updatedAt")
    val updatedAt: String,
    @SerializedName("userId")
    val userId: Int,

    @SerializedName("deliveryFeePercentage")
    val deliveryFeePercentage: String,

    @SerializedName("tipsPercentage")
    val tipsPercentage: String,

    @SerializedName("userDeliveryFee")
    val userDeliveryFee: String,

    @SerializedName("driverNetAmount")
    val driverNetAmount: String,

    @SerializedName("driverTotalAmount")
    val driverTotalAmount: String,

    @SerializedName("userTip")
    val userTip: String
)