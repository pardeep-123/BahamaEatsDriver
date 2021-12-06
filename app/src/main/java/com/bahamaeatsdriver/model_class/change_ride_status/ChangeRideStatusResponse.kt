package com.bahamaeatsdriver.model_class.change_ride_status
import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class ChangeRideStatusResponse(
    @SerializedName("body")
    val body: Body,
    @SerializedName("code")
    val code: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Boolean
): Serializable

data class Body(
    @SerializedName("addressId")
    val addressId: String,
    @SerializedName("completedAt")
    val completedAt: Any,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("currentLat")
    val currentLat: String,
    @SerializedName("currentLong")
    val currentLong: String,
    @SerializedName("driver")
    val driver: Driver,
    @SerializedName("driverId")
    val driverId: Int,
    @SerializedName("dropofftime")
    val dropofftime: Int,
    @SerializedName("fromLat")
    val fromLat: String,
    @SerializedName("fromLong")
    val fromLong: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("order")
    val order: Order,
    @SerializedName("orderId")
    val orderId: Int,
    @SerializedName("pickuptime")
    val pickuptime: Int,
    @SerializedName("response")
    val response: Int,
    @SerializedName("restaurant")
    val restaurant: Restaurant,
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
    @SerializedName("userAddress")
    val userAddress: UserAddress,
    @SerializedName("userId")
    val userId: Int
):Serializable

data class Driver(
    @SerializedName("address")
    val address: String,
    @SerializedName("availability")
    val availability: Int,
    @SerializedName("carInsurance")
    val carInsurance: String,
    @SerializedName("city")
    val city: String,
    @SerializedName("contactNo")
    val contactNo: String,
    @SerializedName("country")
    val country: String,
    @SerializedName("countryCode")
    val countryCode: String,
    @SerializedName("countryCodePhone")
    val countryCodePhone: String,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("default_time_zone")
    val defaultTimeZone: String,
    @SerializedName("deviceToken")
    val deviceToken: String,
    @SerializedName("deviceType")
    val deviceType: Int,
    @SerializedName("dob")
    val dob: String,
    @SerializedName("driverIdentity")
    val driverIdentity: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("firstName")
    val firstName: String,
    @SerializedName("forgotPasswordHash")
    val forgotPasswordHash: String,
    @SerializedName("fullName")
    val fullName: String,
    @SerializedName("gender")
    val gender: Int,
    @SerializedName("id")
    val id: Int,
    @SerializedName("image")
    val image: String,
    @SerializedName("isApproved")
    val isApproved: Int,
    @SerializedName("isCarInsuranceApproved")
    val isCarInsuranceApproved: Int,
    @SerializedName("isDocumentVerified")
    val isDocumentVerified: Int,
    @SerializedName("isDriverIdApproved")
    val isDriverIdApproved: Int,
    @SerializedName("isDriverLicenseApproved")
    val isDriverLicenseApproved: Int,
    @SerializedName("isNotification")
    val isNotification: Int,
    @SerializedName("is_online")
    val isOnline: Int,
    @SerializedName("isPoliceReportApproved")
    val isPoliceReportApproved: Int,
    @SerializedName("lastName")
    val lastName: String,
    @SerializedName("latitude")
    val latitude: String,
    @SerializedName("loginType")
    val loginType: Int,
    @SerializedName("longitude")
    val longitude: String,
    @SerializedName("manually_on_off")
    val manuallyOnOff: String,
    @SerializedName("otp")
    val otp: Int,
    @SerializedName("otpVerified")
    val otpVerified: Int,
    @SerializedName("password")
    val password: String,
    @SerializedName("paymentStatus")
    val paymentStatus: Int,
    @SerializedName("policeRecord")
    val policeRecord: String,
    @SerializedName("postalCode")
    val postalCode: Int,
    @SerializedName("province")
    val province: String,
    @SerializedName("socialId")
    val socialId: String,
    @SerializedName("socialType")
    val socialType: Int,
    @SerializedName("status")
    val status: Int,
    @SerializedName("takeOrderStatus")
    val takeOrderStatus: Int,
    @SerializedName("unique_time")
    val uniqueTime: String,
    @SerializedName("updatedAt")
    val updatedAt: String,
    @SerializedName("username")
    val username: String,
    @SerializedName("wrongAttemptBlock")
    val wrongAttemptBlock: String,
    @SerializedName("wrongAttemptCount")
    val wrongAttemptCount: String
):Serializable

data class Order(
    @SerializedName("address")
    val address: String,
    @SerializedName("addressId")
    val addressId: Int,
    @SerializedName("atlantis_payment_status")
    val atlantisPaymentStatus: Int,
    @SerializedName("cartFee")
    val cartFee: String,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("deliveredTimestamp")
    val deliveredTimestamp: Int,
    @SerializedName("deliveryFee")
    val deliveryFee: String,
    @SerializedName("deliveryFeePercentage")
    val deliveryFeePercentage: String,
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
    @SerializedName("orderDetails")
    val orderDetails: List<OrderDetail>,
    @SerializedName("order_device_type")
    val orderDeviceType: Int,
    @SerializedName("orderLoyalityPoints")
    val orderLoyalityPoints: String,
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
    val tax: String,
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
):Serializable

data class Restaurant(
    @SerializedName("acceptCash")
    val acceptCash: String,
    @SerializedName("address")
    val address: String,
    @SerializedName("city")
    val city: String,
    @SerializedName("closeTime")
    val closeTime: String,
    @SerializedName("commissionType")
    val commissionType: Int,
    @SerializedName("country")
    val country: String,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("cuisineId")
    val cuisineId: String,
    @SerializedName("deliveryStatus")
    val deliveryStatus: Int,
    @SerializedName("deliveryType")
    val deliveryType: Int,
    @SerializedName("description")
    val description: Any,
    @SerializedName("distanceLimit")
    val distanceLimit: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("featured")
    val featured: Int,
    @SerializedName("hideStatus")
    val hideStatus: Int,
    @SerializedName("id")
    val id: Int,
    @SerializedName("image")
    val image: String,
    @SerializedName("isAvailable")
    val isAvailable: Int,
    @SerializedName("isBe")
    val isBe: String,
    @SerializedName("isPopular")
    val isPopular: Int,
    @SerializedName("latitude")
    val latitude: String,
    @SerializedName("longitude")
    val longitude: String,
    @SerializedName("loyality_amount")
    val loyalityAmount: String,
    @SerializedName("loyality_details_description")
    val loyalityDetailsDescription: String,
    @SerializedName("loyality_details_title")
    val loyalityDetailsTitle: String,
    @SerializedName("loyality_expiry_date")
    val loyalityExpiryDate: String,
    @SerializedName("loyality_id")
    val loyalityId: Int,
    @SerializedName("loyality_mini_order_amount")
    val loyalityMiniOrderAmount: String,
    @SerializedName("loyality_order_count")
    val loyalityOrderCount: String,
    @SerializedName("loyality_reward_description")
    val loyalityRewardDescription: String,
    @SerializedName("loyality_reward_title")
    val loyalityRewardTitle: String,
    @SerializedName("loyality_status")
    val loyalityStatus: Int,
    @SerializedName("minDelivery")
    val minDelivery: String,
    @SerializedName("minOrderAmount")
    val minOrderAmount: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("offerDescription")
    val offerDescription: String,
    @SerializedName("offerTitle")
    val offerTitle: String,
    @SerializedName("openTime")
    val openTime: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("paymentType")
    val paymentType: String,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("pickupServiceFee")
    val pickupServiceFee: String,
    @SerializedName("pickupServiceFeeType")
    val pickupServiceFeeType: String,
    @SerializedName("postalCode")
    val postalCode: String,
    @SerializedName("province")
    val province: String,
    @SerializedName("rememberToken")
    val rememberToken: Any,
    @SerializedName("serviceFee")
    val serviceFee: String,
    @SerializedName("status")
    val status: Int,
    @SerializedName("tax")
    val tax: String,
    @SerializedName("totalCredit")
    val totalCredit: String,
    @SerializedName("updatedAt")
    val updatedAt: String
):Serializable

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
):Serializable

data class UserAddress(
    @SerializedName("address")
    val address: String,
    @SerializedName("alternateMobileNumber")
    val alternateMobileNumber: String,
    @SerializedName("city")
    val city: String,
    @SerializedName("cityId")
    val cityId: Int,
    @SerializedName("completeAddress")
    val completeAddress: String,
    @SerializedName("country")
    val country: String,
    @SerializedName("countryCode")
    val countryCode: String,
    @SerializedName("countryCodeAlternateMobileNumber")
    val countryCodeAlternateMobileNumber: String,
    @SerializedName("countryId")
    val countryId: Int,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("deliveryInstructions")
    val deliveryInstructions: String,
    @SerializedName("firstName")
    val firstName: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("isDefault")
    val isDefault: Int,
    @SerializedName("lastName")
    val lastName: String,
    @SerializedName("latitude")
    val latitude: String,
    @SerializedName("longitude")
    val longitude: String,
    @SerializedName("postalCode")
    val postalCode: Int,
    @SerializedName("provience")
    val provience: String,
    @SerializedName("provienceId")
    val provienceId: Int,
    @SerializedName("streetName")
    val streetName: String,
    @SerializedName("updatedAt")
    val updatedAt: String,
    @SerializedName("userId")
    val userId: Int
):Serializable

data class OrderDetail(
    @SerializedName("addons")
    val addons: String,
    @SerializedName("categories")
    val categories: List<Category>,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("is_completed_order")
    val isCompletedOrder: Int,
    @SerializedName("itemDescription")
    val itemDescription: String,
    @SerializedName("itemId")
    val itemId: Int,
    @SerializedName("itemImage")
    val itemImage: String,
    @SerializedName("itemImagePath")
    val itemImagePath: String,
    @SerializedName("itemMenuId")
    val itemMenuId: Int,
    @SerializedName("itemName")
    val itemName: String,
    @SerializedName("itemSpecialRequest")
    val itemSpecialRequest: String,
    @SerializedName("orderId")
    val orderId: Int,
    @SerializedName("price")
    val price: String,
    @SerializedName("quantity")
    val quantity: Int,
    @SerializedName("restaurantId")
    val restaurantId: Int,
    @SerializedName("updatedAt")
    val updatedAt: String
):Serializable


data class Category(
    @SerializedName("addOnArray")
    val addOnArray: List<AddOnArray>,
    @SerializedName("categoryName")
    val categoryName: String,
    @SerializedName("id")
    val id: Int
):Serializable

data class AddOnArray(
    @SerializedName("addon")
    val addon: String,
    @SerializedName("price")
    val price: String
):Serializable