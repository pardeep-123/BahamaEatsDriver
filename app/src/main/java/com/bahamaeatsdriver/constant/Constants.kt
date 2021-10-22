package com.bahamaeats.constant

/***
 * Created by Poonam on 23-June-2020
 */
class Constants {
    companion object {
        //Live server urls
//         const val BASE_URL = "https://bahamaeats.com:8008/apiNew/"
//        const val SOCKET_BASE_URL = "https://bahamaeats.com:8008/"
//        const val IMAGE_URL = "https://bahamaeats.com:8008/images/drivers/"
//        const val RESTAURANT_BASE_URL = "https://admin.bahamaeats.com/"
        //Local server urls
        const val BASE_URL = "https://dev.bahamaeats.com:8008/apiNew/"
        const val SOCKET_BASE_URL = "https://dev.bahamaeats.com:8008/"
        const val IMAGE_URL = "https://dev.bahamaeats.com:8008/images/drivers/"
        const val RESTAURANT_BASE_URL = "https://admin.bahamaeats.com/"

        const val SECURITY_KEY = "security_key"
        const val SECURITY_KEY_CODE = "BahamaEats"
        const val LICENSE_DATE_FORMAT = "dd-MM-yyyy"
        const val ORDER_DATE_FORMAT = "dd-MMM-yyyy"

        //End URL
        const val SIGN_UP = "driversignUp"
        const val LOGIN = "driverLogin"
        const val GET_OTP = "getDriverOTP"
        const val UPDATE_PHONE_NUMBER = "sendDriverPhoneOTP"/*for update phone number*/
        const val VERIFY_OTP = "verifyDriverOTP"
        const val UPDATE_PHONE_VERIFY_OTP = "verifyDriverPhoneOTP "
        const val RESEND_OTP = "resendDriverOTP"
        const val GET_CITY = "getCity"
        const val TERMS_AND_CONDITIONS = "termsAndConditions"
        const val UPDATE_DRIVER_TAKE_ORDER_STATUS = "updateDriverTakeOrderStatus"
        const val DRIVER_PROFILE_DETAILS = "getDriverProfile"

        const val UPDATE_LICENSE = "updateLicense"
        const val ADD_LICENSE = "addLicense"
        const val GET_LICENSE = "getLicense"
        const val EDIT_DRIVER_PROFILE = "editDriverProfile"
        const val UPDATE_NOTIFICATION_STATUS = "driverChangeNotificationStatus"
        const val ADD_ID_CARD_DETAILS = "addIdCard"
        const val GET_ID_CARD_DEATILS = "getIdCard"
        const val UPDATE_ID_CARD = "updateIdCard"
        const val DELETE_ID_CARD_IMAGE = "deleteIdCardImage"
        const val FORGOT_PASSWORD = "driverForgotPassword"
        const val OTP = "otp"
        const val LOGOUT = "driverLogout"
        const val CHANGE_PASSWORD = "driverChangePassword"
        const val TAKE_ORDER_STATUS = "takeOrderStatus"
        const val UDPATE_DRIVER_LAT_LONG = "updateDriverLatLng"
        const val JOB_HISTORY = "jobHistory"
        const val JOB_HISTORY_DETAILS = "jobHistoryDetails"
        const val PAYMENT_STATUS_LISTING = "paymentStatusListingDriver"
        const val PAYMENT_STATUS_LISTING_LIVE = "paymentStatusListingDriverlive"

        const val GET_DRIVER_TAKE_ORDER_STATUS = "getDriverTakeOrderStatus"
        const val RESPOND_RIDE_REQUEST = "respondRideRequest"
        const val CHANGE_RIDE_STATUS = "changeRideStatus"
        const val GET_CURRENT_RIDE = "getCurrentRide"
        const val GET_RIDER_DETAILS = "getRideDetails"
        const val I_AM_HERE = "iAmHere"
        const val EARNINGS = "earnings"
        const val FILTER_EARNINGS = "earningsDataBasedOnDates"
        const val ADD_UPDATE_BANK = "addupdatebank"
        const val GET_BANK_DETAILS = "getbankdetail"
        const val GET_TRAINING_LINKS = "getTrainingLinks"


        const val DELETE_USER_NOTIFICATIONS = "deleteDriverNotifications"
        const val DELETE_ALL_USER_NOTIFICATIONS = "deleteAllUserNotifications"
        const val READ_NOTIFICATIONS = "readNotification"
        const val GET_NOTIFICATIONS = "getDriverNotifications"
        const val NOTIFICATION_ID = "notificationId"

        /**
         * New api's
         */
        const val  GET_DOCUMENTATION_DETAILS="getDriverDocumentation"

        const val  ADD_UPDATE_POLICE_RECORD="addUpdatePoliceRecord"
        const val  DELETE_POLICE_RECORD="deletePoliceRecord"

        const val  ADD_UPDATE_CAR_INSURANCE_RECORD="addUpdateCarInsurance"
        const val  DELETE_CAR_INSURANCE_RECORD="deleteCarInsurance"
        const val  UPLOAD_RECEIPT="uploadBeReceipt"
        const val  DRIVER_SLOTS="driverSlots"
        const val  ADD_DRIVER_SLOTS="addTimeSlots"
        const val  GET_DRIVER_ADDED_SLOTS="getDriverSlots"


        const val LATITUDE = "latitude"
        const val LONGITUDE = "longitude"
        const val ADDRESS = "address"
        const val ORDER_ID = "orderId"
        const val RECEIPT_AMOUNT = "receipt_amount"
        const val RECEIPT_NUMBER = "receipt_number"
        const val TOKEN = "token"
        const val FIRST_TIME_OPEN = "app_open_count"

        /**
         * 1=IOS device
         * 2= For Android device
         */
        const val DEVICE_ID = "2"
        const val CARD_ID = "id"
        const val FULL_NAME = "fullName"
        const val FIRST_NAME = "firstName"
        const val LAST_NAME = "lastName"
        const val USER_NAME = "username"
        const val EMAIL = "email"
        const val CITY = "city"
        const val COUNTRY = "country"
        const val PASSWORD = "password"
        const val LOGIN_TYPE = "loginType"
        const val DEVICE_TYPE = "deviceType"
        const val PHONE = "phone"
        const val COUNTRY_CODE = "countryCode"
        const val image = "image"
        const val DEVICE_TOKEN = "deviceToken"
        const val USER_ID = "user_id"
        const val FRONT_PHOTO = "frontPhoto"
        const val BACK_PHOTO = "backPhoto"
        const val LICENSE_TYPE = "licenseType"
        const val LICENSE_NO = "licenseNo"
        const val DOB = "dob"
        const val ISSUE_DATE = "issueDate"
        const val EXPIRY_DATE = "expiryDate"
        const val NATIONALITY = "nationality"
        const val LICENSE_ID = "licenseId"
        const val CURRENT_PASSWORD = "currentPassword"
        const val JOB_HISTORY_ID = "jobHistoryId"
        const val toDate = "toDate"
        const val fromDate = "fromDate"
        const val type = "type"
        const val NEW_PASSWORD = "newPassword"
        const val IS_NOTIFICATION = "isNotification"
        const val ID_NUMBER = "idNumber"
        const val RIDE_STATUS = "rideStatus"
        const val RIDE_REQUEST_ID = "rideRequestId"
        const val RESPOSE = "response"
        const val RIDE_ID = "rideId"
        const val ACCOUNT_HOLDER_NAME = "account_holder_name"
        const val ACCOUNT_NUMBER = "account_number"
        const val BANK_NAME = "bank_name"
        const val BRANCH_NAME = "branch_name"
        const val ACCOUNT_TYPE = "account_type"
        const val ID = "id"
        const val DAY_ID = "dayId"

        /**
         * ANDROID_DEVICE:1=For IOS device
         *ANDROID_DEVICE :2= Android device
         */
        const val ANDROID_DEVICE = "2"
        const val SIMPLE_LOGIN = "0"

        /***
         * Ride response params // 1=>Accept 2=>Reject
         */
        const val ACCEPT_RIDE_STATUS = "1"
        const val REJECT_RIDE_STATUS = "2"

        const val START_RIDE = "2"
        const val COMPLETE_RIDE = "3"
        const val CANCEL_RIDE = "4"
        const val HELPLINE_NO = "+1(242)817-7188"

        /***
         * SharedPrefrence Constants
         */
        const val DRIVER_DETAILS = "driver_details"
        /**
         * SOCKET CALL Type
         *"type": 1=>user, 2=>driver
         */
        const val DRIVER_TYPE=2
        const val USER_TYPE=1
    }
}