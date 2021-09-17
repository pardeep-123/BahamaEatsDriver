package com.bahamaeats.network

import com.bahamaeats.constant.Constants
import com.bahamaeatsdriver.model_class.accept_reject_ride.AcceptRejectRideRequest
import com.bahamaeatsdriver.model_class.add_bank_details.AddUpdateBankDetails
import com.bahamaeatsdriver.model_class.add_driver_availability_slots.AddDriverAvailabilitySlots
import com.bahamaeatsdriver.model_class.add_idDetails.AddIdDetailsResponse
import com.bahamaeatsdriver.model_class.add_license_details.AddLicenseDetails
import com.bahamaeatsdriver.model_class.add_update_car_insurance.AddUpdateCarInsurance
import com.bahamaeatsdriver.model_class.add_update_police_record.AddUpdatePoliceRecord
import com.bahamaeatsdriver.model_class.change_password.ChangePasswordResponse
import com.bahamaeatsdriver.model_class.change_ride_status.ChangeRideStatusResponse
import com.bahamaeatsdriver.model_class.delete_car_insurance.DeleteCarInsuranceResponse
import com.bahamaeatsdriver.model_class.delete_id_card_image.DeleteIdDetailsResponse
import com.bahamaeatsdriver.model_class.delete_notifications.DeleteNotificationResponse
import com.bahamaeatsdriver.model_class.delete_police_record.DeletePoliceRecordResponse
import com.bahamaeatsdriver.model_class.driver_added_slots.DriverAddedSlotList
import com.bahamaeatsdriver.model_class.driver_documentation_details.DriverDocumentaionResponse
import com.bahamaeatsdriver.model_class.driver_earnings.DriverEarningResposne
import com.bahamaeatsdriver.model_class.driver_payments.DriverPaymentsResposne
import com.bahamaeatsdriver.model_class.driver_tips_earning.DriverTipsAndEarning
import com.bahamaeatsdriver.model_class.edit_driver_profile.EditDriverProfileResponse
import com.bahamaeatsdriver.model_class.forgot_password.ForgotPasswordResponse
import com.bahamaeatsdriver.model_class.get_bank_details.GetBankDetails
import com.bahamaeatsdriver.model_class.get_city.GetCityResponse
import com.bahamaeatsdriver.model_class.get_current_ride.GetCurrentRideResponse
import com.bahamaeatsdriver.model_class.get_driver_license.GetDriverLicense
import com.bahamaeatsdriver.model_class.get_id_details.GetIdDetailsRespone
import com.bahamaeatsdriver.model_class.get_otp.GetOtpResponse
import com.bahamaeatsdriver.model_class.get_rider_details.GetRiderDetailsResponse
import com.bahamaeatsdriver.model_class.get_take_driver_orderstatus.GetTakeDriverOrderStatus
import com.bahamaeatsdriver.model_class.i_am_here.IAmHereResponse
import com.bahamaeatsdriver.model_class.job_history.JobHistoryResponse
import com.bahamaeatsdriver.model_class.job_history_details.JobHistoryyDetailsResponse
import com.bahamaeatsdriver.model_class.login.LoginResponse
import com.bahamaeatsdriver.model_class.logout.LogoutResponse
import com.bahamaeatsdriver.model_class.map_poliline.Result
import com.bahamaeatsdriver.model_class.notification_listing.NotificationListingResponse
import com.bahamaeatsdriver.model_class.notification_status_change.UpdateNotificationStatus
import com.bahamaeatsdriver.model_class.profile_details.DriverProfileDetailsResposne
import com.bahamaeatsdriver.model_class.resend_otp.ResendOtpResponse
import com.bahamaeatsdriver.model_class.signup.SignUpResponse
import com.bahamaeatsdriver.model_class.slots.DriverSlots
import com.bahamaeatsdriver.model_class.terms_and_conditions.TermsAndConditionResponse
import com.bahamaeatsdriver.model_class.training_video_links.TrainingVideoLinksResponse
import com.bahamaeatsdriver.model_class.update_driver_online_status.UpdateDriverTakeOrderStatus
import com.bahamaeatsdriver.model_class.update_id_details.UpdateIdDetailsResponse
import com.bahamaeatsdriver.model_class.update_latitudeLongitude.UpdateDriverLatLongResponse
import com.bahamaeatsdriver.model_class.update_license_details.UpdateLicenseDetails
import com.bahamaeatsdriver.model_class.update_phone_number.UpdatePhoneNumberResponse
import com.bahamaeatsdriver.model_class.updated_phone_verify_otp.UpdatePhoneVerifyOtpResponse
import com.bahamaeatsdriver.model_class.upload_receipt.UploadReceiptResponse
import com.bahamaeatsdriver.model_class.verify_otp.VerifyOtpResponse
import io.reactivex.Observable
import io.reactivex.annotations.NonNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*


interface RestApiInterface {

    @FormUrlEncoded
    @PUT(Constants.LOGIN)
    fun LOGIN(
        @Header(Constants.SECURITY_KEY) key: String,
        @Field(Constants.EMAIL) email: String,
        @Field(Constants.PASSWORD) password: String,
        @Field(Constants.DEVICE_TYPE) deviceType: String,
        @Field(Constants.DEVICE_TOKEN) deviceToken: String
    ): Observable<LoginResponse>

    @FormUrlEncoded
    @PUT(Constants.FORGOT_PASSWORD)
    fun FORGOT_PASSWORD(@Field(Constants.EMAIL) email: String): Observable<ForgotPasswordResponse>

    @FormUrlEncoded
    @POST(Constants.VERIFY_OTP)
    fun VERIFY_OTP(
        @Field(Constants.COUNTRY_CODE) countryCode: String,
        @Field(Constants.PHONE) phone: String,
        @Field(Constants.OTP) otp: String
    ): Observable<VerifyOtpResponse>

    @FormUrlEncoded
    @POST(Constants.UPDATE_PHONE_VERIFY_OTP)
    fun UPDATE_PHONE_VERIFY_OTP(
        @Field(Constants.COUNTRY_CODE) countryCode: String,
        @Field(Constants.PHONE) phone: String,
        @Field(Constants.OTP) otp: String
    ): Observable<UpdatePhoneVerifyOtpResponse>

    @FormUrlEncoded
    @POST(Constants.GET_OTP)
    fun GET_OTP(
        @Field(Constants.COUNTRY_CODE) countryCode: String,
        @Field(Constants.PHONE) phone: String
    ): Observable<GetOtpResponse>

    @FormUrlEncoded
    @POST(Constants.UPDATE_PHONE_NUMBER)
    fun UPDATE_PHONE_NUMBER(
        @Field(Constants.COUNTRY_CODE) countryCode: String,
        @Field(Constants.PHONE) phone: String
    ): Observable<UpdatePhoneNumberResponse>

    @FormUrlEncoded
    @POST(Constants.RESEND_OTP)
    fun RESEND_OTP(
        @Field(Constants.COUNTRY_CODE) countryCode: String,
        @Field(Constants.PHONE) phone: String
    ): Observable<ResendOtpResponse>

    @NonNull
    @GET("getCity/{input}")
    fun GET_CITY(@Path("input") input: String): Observable<GetCityResponse>

    @NonNull
    @GET(Constants.TERMS_AND_CONDITIONS)
    fun TermsAndCondition(): Observable<TermsAndConditionResponse>

    @NonNull
    @GET(Constants.GET_DRIVER_TAKE_ORDER_STATUS)
    fun GET_DRIVER_TAKE_ORDER_STATUS(): Observable<GetTakeDriverOrderStatus>

    @Multipart
    @PUT(Constants.SIGN_UP)
    fun SIGNUP(
        @Part photo: MultipartBody.Part?,
        @Header(Constants.SECURITY_KEY) security_key: String,
        @Part(Constants.FULL_NAME) fullName: RequestBody,
        @Part(Constants.EMAIL) email: RequestBody,
        @Part(Constants.PASSWORD) password: RequestBody,
        @Part(Constants.PHONE) phone: RequestBody,
        @Part(Constants.CITY) city: RequestBody,
        @Part(Constants.COUNTRY) country: RequestBody,
        @Part(Constants.LOGIN_TYPE) loginType: RequestBody,
        @Part(Constants.DEVICE_TYPE) deviceType: RequestBody,
        @Part(Constants.DEVICE_TOKEN) deviceToken: RequestBody
    ): Observable<SignUpResponse>

    @Multipart
    @PUT(Constants.EDIT_DRIVER_PROFILE)
    fun EDIT_DRIVER_PROFILE(
        @Part photo: MultipartBody.Part?,
        @Part(Constants.COUNTRY_CODE) countryCode: RequestBody,
        @Part(Constants.PHONE) phone: RequestBody,
        @Part(Constants.LATITUDE) latitude: RequestBody,
        @Part(Constants.LONGITUDE) longitude: RequestBody,
//            @Part(Constants.ADDRESS) address: RequestBody,
        @Part(Constants.CITY) city: RequestBody,
        @Part(Constants.COUNTRY) country: RequestBody,
        @Part(Constants.FULL_NAME) fullName: RequestBody
    ): Observable<EditDriverProfileResponse>

    @Multipart
    @POST(Constants.ADD_LICENSE)
    fun ADD_LICENSE(
        @Part frontPhoto: MultipartBody.Part?,
        @Part backPhoto: MultipartBody.Part?,
        @Header("Authorization") Authorization: String,
        @Part(Constants.LICENSE_TYPE) licenseType: RequestBody,
        @Part(Constants.LICENSE_NO) licenseNo: RequestBody,
        @Part(Constants.DOB) dob: RequestBody,
        @Part(Constants.ISSUE_DATE) issueDate: RequestBody,
        @Part(Constants.EXPIRY_DATE) expiryDate: RequestBody,
        @Part(Constants.NATIONALITY) nationality: RequestBody
    ): Observable<AddLicenseDetails>

    @Multipart
    @POST(Constants.ADD_ID_CARD_DETAILS)
    fun ADD_ID_CARD_DETAILS(
        @Part frontPhoto: MultipartBody.Part?,
        @Part backPhoto: MultipartBody.Part?,
        @Part(Constants.FIRST_NAME) firstName: RequestBody,
        @Part(Constants.LAST_NAME) lastName: RequestBody,
        @Part(Constants.ID_NUMBER) idNumber: RequestBody,
        @Part(Constants.ISSUE_DATE) issueDate: RequestBody,
        @Part(Constants.DOB) dob: RequestBody,
        @Part(Constants.ADDRESS) address: RequestBody
    ): Observable<AddIdDetailsResponse>

    @Multipart
    @PUT(Constants.UPDATE_ID_CARD)
    fun UPDATE_ID_CARD(
        @Part frontPhoto: MultipartBody.Part?,
        @Part backPhoto: MultipartBody.Part?,
        @Part(Constants.CARD_ID) id: RequestBody,
        @Part(Constants.FIRST_NAME) firstName: RequestBody,
        @Part(Constants.LAST_NAME) lastName: RequestBody,
        @Part(Constants.ID_NUMBER) idNumber: RequestBody,
        @Part(Constants.ISSUE_DATE) issueDate: RequestBody,
        @Part(Constants.DOB) dob: RequestBody,
        @Part(Constants.ADDRESS) address: RequestBody
    ): Observable<UpdateIdDetailsResponse>

    @Multipart
    @POST(Constants.UPLOAD_RECEIPT)
    fun UPLOAD_RECEIPT(
        @Part receiptUpload: MultipartBody.Part?,
        @Part(Constants.ORDER_ID) orderId: RequestBody
    ): Observable<UploadReceiptResponse>

    @GET(Constants.GET_ID_CARD_DEATILS)
    fun GET_ID_CARD_DEATILS(): Observable<GetIdDetailsRespone>

    @GET(Constants.DRIVER_SLOTS)
    fun DRIVER_SLOTS(): Observable<DriverSlots>

    ///new Apis
    @GET(Constants.GET_DOCUMENTATION_DETAILS)
    fun GET_DOCUMENTATION_DETAILS(): Observable<DriverDocumentaionResponse>

    @Multipart
    @PUT(Constants.ADD_UPDATE_CAR_INSURANCE_RECORD)
    fun ADD_UPDATE_CAR_INSURANCE_RECORD(@Part carInsurance: MultipartBody.Part?): Observable<AddUpdateCarInsurance>

    @Multipart
    @PUT(Constants.ADD_UPDATE_POLICE_RECORD)
    fun ADD_UPDATE_POLICE_RECORD(
        @Part policeRecord: MultipartBody.Part?
    ): Observable<AddUpdatePoliceRecord>

    @DELETE(Constants.DELETE_POLICE_RECORD)
    fun DELETE_POLICE_RECORD(): Observable<DeletePoliceRecordResponse>

    @DELETE(Constants.DELETE_CAR_INSURANCE_RECORD)
    fun DELETE_CAR_INSURANCE_RECORD(): Observable<DeleteCarInsuranceResponse>

    @FormUrlEncoded
    @POST(Constants.DELETE_ID_CARD_IMAGE)
    fun DELETE_ID_CARD_IMAGE(@Field("id") id: String): Observable<DeleteIdDetailsResponse>

    @FormUrlEncoded
    @POST(Constants.ADD_DRIVER_SLOTS)
    fun ADD_DRIVER_SLOTS(@Field("timeSlots") timeSlots: String): Observable<AddDriverAvailabilitySlots>


    @Multipart
    @PUT(Constants.UPDATE_LICENSE)
    fun UPDATE_LICENSE(
        @Part frontPhoto: MultipartBody.Part?,
        @Part backPhoto: MultipartBody.Part?,
        @Part(Constants.LICENSE_ID) licenseId: RequestBody,
        @Part(Constants.LICENSE_TYPE) licenseType: RequestBody,
        @Part(Constants.LICENSE_NO) licenseNo: RequestBody,
        @Part(Constants.DOB) dob: RequestBody,
        @Part(Constants.ISSUE_DATE) issueDate: RequestBody,
        @Part(Constants.EXPIRY_DATE) expiryDate: RequestBody,
        @Part(Constants.NATIONALITY) nationality: RequestBody
    ): Observable<UpdateLicenseDetails>

    @PUT(Constants.LOGOUT)
    fun LOGOUT(): Observable<LogoutResponse>

    @FormUrlEncoded
    @POST(Constants.PAYMENT_STATUS_LISTING_LIVE)
    fun PAYMENT_STATUS_LISTING(@Field(Constants.type) type: String): Observable<DriverPaymentsResposne>

    @GET(Constants.EARNINGS)
    fun EARNINGS(): Observable<DriverEarningResposne>

    @GET(Constants.GET_DRIVER_ADDED_SLOTS)
    fun GET_DRIVER_ADDED_SLOTS(): Observable<DriverAddedSlotList>

    @FormUrlEncoded
    @POST(Constants.FILTER_EARNINGS)
    fun FILTER_EARNINGS(
        @Field(Constants.fromDate) fromDate: String,
        @Field(Constants.toDate) toDate: String
    ): Observable<DriverTipsAndEarning>

    @FormUrlEncoded
    @POST(Constants.ADD_UPDATE_BANK)
    fun ADD_UPDATE_BANK(
        @Field(Constants.ACCOUNT_HOLDER_NAME) account_holder_name: String,
        @Field(Constants.ACCOUNT_NUMBER) account_number: String,
        @Field(Constants.BANK_NAME) bank_name: String,
        @Field(Constants.BRANCH_NAME) branch_name: String,
        @Field(Constants.ACCOUNT_TYPE) account_type: String,
        @Field(Constants.ID) id: String
    ): Observable<AddUpdateBankDetails>

    @GET(Constants.JOB_HISTORY)
    fun JOB_HISTORY(): Observable<JobHistoryResponse>

    @GET(Constants.GET_BANK_DETAILS)
    fun GET_BANK_DETAILS(): Observable<GetBankDetails>

    @GET(Constants.GET_TRAINING_LINKS)
    fun GET_TRAINING_LINKS(): Observable<TrainingVideoLinksResponse>

    @FormUrlEncoded
    @POST(Constants.JOB_HISTORY_DETAILS)
    fun JOB_HISTORY_DETAILS(@Field(Constants.JOB_HISTORY_ID) jobHistoryId: String): Observable<JobHistoryyDetailsResponse>

    @FormUrlEncoded
    @PUT(Constants.CHANGE_PASSWORD)
    fun CHANGE_PASSWORD(
        @Field(Constants.CURRENT_PASSWORD) currentPassword: String,
        @Field(Constants.NEW_PASSWORD) newPassword: String
    ): Observable<ChangePasswordResponse>

    @FormUrlEncoded
    @PUT(Constants.UDPATE_DRIVER_LAT_LONG)
    fun UDPATE_DRIVER_LAT_LONG(
        @Field(Constants.LATITUDE) latitude: String,
        @Field(Constants.LONGITUDE) longitude: String
    ): Observable<UpdateDriverLatLongResponse>

    @FormUrlEncoded
    @PUT(Constants.UPDATE_NOTIFICATION_STATUS)
    fun UPDATE_NOTIFICATION_STATUS(@Field(Constants.IS_NOTIFICATION) isNotification: String): Observable<UpdateNotificationStatus>

    @FormUrlEncoded
    @PUT(Constants.UPDATE_DRIVER_TAKE_ORDER_STATUS)
    fun UPDATED_RIVER_TAKE_ORDERSTATUS(@Field(Constants.TAKE_ORDER_STATUS) takeOrderStatus: String): Observable<UpdateDriverTakeOrderStatus>

    /**
     * Change Reposne class here
     */
    @FormUrlEncoded
    @PUT(Constants.GET_RIDER_DETAILS)
    fun GET_RIDER_DETAILS(@Field(Constants.RIDE_REQUEST_ID) rideRequestId: String): Observable<GetRiderDetailsResponse>

    @FormUrlEncoded
    @PUT(Constants.I_AM_HERE)
    fun I_AM_HERE(@Field(Constants.RIDE_ID) rideId: String): Observable<IAmHereResponse>

    //    reposne:  // 1=>Accept 2=>Reject
    @FormUrlEncoded
    @PUT(Constants.RESPOND_RIDE_REQUEST)
    fun RESPOND_RIDE_REQUEST(
        @Field(Constants.RIDE_REQUEST_ID) rideRequestId: String,
        @Field(Constants.RESPOSE) reposne: String
    ): Observable<AcceptRejectRideRequest>

    //rideStatus:  2=>Start 3=>Complete 4=>Cancel
    @FormUrlEncoded
    @PUT(Constants.CHANGE_RIDE_STATUS)
    fun CHANGE_RIDE_STATUS(
        @Field(Constants.RIDE_REQUEST_ID) rideRequestId: String,
        @Field(Constants.RIDE_STATUS) rideStatus: String
    ): Observable<ChangeRideStatusResponse>

    @GET(Constants.DRIVER_PROFILE_DETAILS)
    fun DRIVER_PROFILE_DETAILS(
        @Header(Constants.USER_ID) user_id: String,
        @Header(Constants.SECURITY_KEY) key: String
    ): Observable<DriverProfileDetailsResposne>

    @GET(Constants.GET_LICENSE)
    fun GET_LICENSE_DETAILS(): Observable<GetDriverLicense>

    @GET(Constants.GET_CURRENT_RIDE)
    fun GET_CURRENT_RIDE(/*@Field(Constants.RIDE_STATUS) rideStatus: String*/): Observable<GetCurrentRideResponse>




    @NonNull
    @POST(Constants.DELETE_ALL_USER_NOTIFICATIONS)
    fun DELETE_ALL_USER_NOTIFICATIONS(): Observable<GetCurrentRideResponse>

    @NonNull
    @FormUrlEncoded
    @POST(Constants.DELETE_USER_NOTIFICATIONS)
    fun DELETE_USER_NOTIFICATIONS(@Field(Constants.NOTIFICATION_ID) notificationId: String): Observable<DeleteNotificationResponse>

    @NonNull
    @GET(Constants.GET_NOTIFICATIONS)
    fun GET_NOTIFICATIONS(): Observable<NotificationListingResponse>

    @NonNull
    @FormUrlEncoded
    @POST(Constants.READ_NOTIFICATIONS)
    fun READ_NOTIFICATIONS(@Field(Constants.NOTIFICATION_ID) notificationId: String): Observable<GetCurrentRideResponse>

    @GET("maps/api/directions/json")
    fun getDirections(
        @Query("mode") mmode: String?,
        @Query("transit_routing_preference") routingPreference: String?,
        @Query("origin") morigin: String?,
        @Query("destination") mdestination: String?,
        @Query("key") apiKey: String?
    ): Observable<Result>



}