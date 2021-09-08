package com.bahamaeatsdriver.repository

import android.annotation.SuppressLint
import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bahamaeats.constant.Constants
import com.bahamaeats.listeners.OnNoInternetConnectionListener
import com.bahamaeats.network.RestApiInterface
import com.bahamaeats.network.RestObservable
import com.bahamaeatsdriver.R
import com.bahamaeatsdriver.activity.Otp.Otp_Fill_Activity
import com.bahamaeatsdriver.di.App
import com.bahamaeatsdriver.helper.extensions.getTokenPrefrence
import com.bahamaeatsdriver.helper.others.Helper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject


class BaseViewModel : ViewModel() {


    @Inject
    lateinit var apiService: RestApiInterface

    init {
        App.getinstance().getmydicomponent().inject(this)
    }

    private var loginResponse: MutableLiveData<RestObservable> = MutableLiveData()
    fun getLoginResponse(): LiveData<RestObservable> {
        return loginResponse
    }

    @SuppressLint("CheckResult")
    fun loginApi(activity: Activity, email: String, password: String, isDialogShow: Boolean) {
        if (Helper.isNetworkConnected(activity)) {
            apiService.LOGIN(
                Constants.SECURITY_KEY_CODE,
                email,
                password,
                Constants.DEVICE_ID, getTokenPrefrence(Constants.DEVICE_TOKEN, "")
            ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    loginResponse.value = RestObservable.loading(activity, isDialogShow)
                }
                .subscribe({ loginResponse.value = RestObservable.success(it) },
                    { loginResponse.value = RestObservable.error(activity, it) })
        } else {
            Helper.showNoInternetAlert(activity,
                activity.getString(R.string.no_internet_connection),
                object : OnNoInternetConnectionListener {
                    override fun onRetryApi() {
                        loginApi(
                            activity,
                            email,
                            password,
                            isDialogShow
                        )
                    }
                })
        }
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /***
     * Forgot password
     */

    private var forgotPasswordResponse: MutableLiveData<RestObservable> = MutableLiveData()
    fun getforgotPasswordResponse(): LiveData<RestObservable> {
        return forgotPasswordResponse
    }

    @SuppressLint("CheckResult")
    fun forgotPasswordApi(
        forgotPasswordActivity: Activity,
        email: String,
        isDialogShow: Boolean
    ) {
        if (Helper.isNetworkConnected(forgotPasswordActivity)) {
            apiService.FORGOT_PASSWORD(email)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {

                    forgotPasswordResponse.value =
                        RestObservable.loading(forgotPasswordActivity, isDialogShow)
                }
                .subscribe(
                    { forgotPasswordResponse.value = RestObservable.success(it) },
                    {
                        forgotPasswordResponse.value =
                            RestObservable.error(forgotPasswordActivity, it)
                    }
                )
        } else {

            Helper.showNoInternetAlert(forgotPasswordActivity,
                forgotPasswordActivity.getString(R.string.no_internet_connection),
                object : OnNoInternetConnectionListener {
                    override fun onRetryApi() {
                        forgotPasswordApi(
                            forgotPasswordActivity,
                            email,
                            isDialogShow
                        )
                    }
                })
        }
    }
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    private var verifyOtpResponsne: MutableLiveData<RestObservable> = MutableLiveData()

    fun getverifyOtp(): LiveData<RestObservable> {
        return verifyOtpResponsne
    }

    @SuppressLint("CheckResult")
    fun verifyOtprApi(
        activity: Otp_Fill_Activity,
        countryCode: String,
        phone: String,
        otp: String,
        isDialogShow: Boolean
    ) {
        if (Helper.isNetworkConnected(activity)) {
            apiService.VERIFY_OTP(
                countryCode, phone, otp
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {

                    verifyOtpResponsne.value =
                        RestObservable.loading(activity, isDialogShow)
                }
                .subscribe(
                    { verifyOtpResponsne.value = RestObservable.success(it) },
                    { verifyOtpResponsne.value = RestObservable.error(activity, it) }
                )
        } else {

            Helper.showNoInternetAlert(activity,
                activity.getString(R.string.no_internet_connection),
                object : OnNoInternetConnectionListener {
                    override fun onRetryApi() {
                        verifyOtprApi(
                            activity, countryCode, phone, otp,
                            isDialogShow
                        )
                    }
                })
        }
    }
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /***
     * Update phone verify OTp
     */
    private var updatedPhoneVerifyOtpResponsne: MutableLiveData<RestObservable> = MutableLiveData()

    fun getUpdatedPhoneVerifyOtp(): LiveData<RestObservable> {
        return updatedPhoneVerifyOtpResponsne
    }

    @SuppressLint("CheckResult")
    fun updatedPhoneVerifyOtpResponsneApi(
        activity: Otp_Fill_Activity,
        countryCode: String,
        phone: String,
        otp: String,
        isDialogShow: Boolean
    ) {
        if (Helper.isNetworkConnected(activity)) {
            apiService.UPDATE_PHONE_VERIFY_OTP(
                countryCode, phone, otp
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {

                    updatedPhoneVerifyOtpResponsne.value =
                        RestObservable.loading(activity, isDialogShow)
                }
                .subscribe(
                    { updatedPhoneVerifyOtpResponsne.value = RestObservable.success(it) },
                    { updatedPhoneVerifyOtpResponsne.value = RestObservable.error(activity, it) }
                )
        } else {

            Helper.showNoInternetAlert(activity,
                activity.getString(R.string.no_internet_connection),
                object : OnNoInternetConnectionListener {
                    override fun onRetryApi() {
                        verifyOtprApi(
                            activity, countryCode, phone, otp,
                            isDialogShow
                        )
                    }
                })
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /****
     * Get OTP API call
     */

    private var getOtpResponsne: MutableLiveData<RestObservable> = MutableLiveData()

    fun getOtpApiResponse(): LiveData<RestObservable> {
        return getOtpResponsne
    }

    @SuppressLint("CheckResult")
    fun getOtpApiCall(
        activity: Activity,
        countryCode: String,
        phone: String,
        isDialogShow: Boolean
    ) {
        if (Helper.isNetworkConnected(activity)) {
            apiService.GET_OTP(countryCode, phone)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    getOtpResponsne.value = RestObservable.loading(activity, isDialogShow)
                }
                .subscribe({ getOtpResponsne.value = RestObservable.success(it) },
                    { getOtpResponsne.value = RestObservable.error(activity, it) }
                )
        } else {

            Helper.showNoInternetAlert(activity,
                activity.getString(R.string.no_internet_connection),
                object : OnNoInternetConnectionListener {
                    override fun onRetryApi() {
                        getOtpApiCall(
                            activity, countryCode, phone,
                            isDialogShow
                        )
                    }
                })
        }
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /****
     *Update phone to get OTP
     */

    private var updatePhoneOtpResponse: MutableLiveData<RestObservable> = MutableLiveData()
    fun getupdatePhoneOtpResponse(): LiveData<RestObservable> {
        return updatePhoneOtpResponse
    }

    @SuppressLint("CheckResult")
    fun getupdatePhoneOtpApiCall(
        activity: Activity,
        countryCode: String,
        phone: String,
        isDialogShow: Boolean
    ) {
        if (Helper.isNetworkConnected(activity)) {
            apiService.UPDATE_PHONE_NUMBER(countryCode, phone)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    updatePhoneOtpResponse.value = RestObservable.loading(activity, isDialogShow)
                }
                .subscribe({ updatePhoneOtpResponse.value = RestObservable.success(it) },
                    { updatePhoneOtpResponse.value = RestObservable.error(activity, it) }
                )
        } else {

            Helper.showNoInternetAlert(activity,
                activity.getString(R.string.no_internet_connection),
                object : OnNoInternetConnectionListener {
                    override fun onRetryApi() {
                        getOtpApiCall(
                            activity, countryCode, phone,
                            isDialogShow
                        )
                    }
                })
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /****
     * Get OTP API call
     */

    private var getresendResponsne: MutableLiveData<RestObservable> = MutableLiveData()

    fun getresendApiResponse(): LiveData<RestObservable> {
        return getresendResponsne
    }

    @SuppressLint("CheckResult")
    fun resendOtpApiCall(
        activity: Activity,
        countryCode: String,
        phone: String,
        isDialogShow: Boolean
    ) {
        if (Helper.isNetworkConnected(activity)) {
            apiService.RESEND_OTP(countryCode, phone)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    getresendResponsne.value = RestObservable.loading(activity, isDialogShow)
                }
                .subscribe({ getresendResponsne.value = RestObservable.success(it) },
                    { getresendResponsne.value = RestObservable.error(activity, it) }
                )
        } else {

            Helper.showNoInternetAlert(activity,
                activity.getString(R.string.no_internet_connection),
                object : OnNoInternetConnectionListener {
                    override fun onRetryApi() {
                        resendOtpApiCall(
                            activity, countryCode, phone,
                            isDialogShow
                        )
                    }
                })
        }
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /****
     * Get City API call
     */

    private var getCityResponse: MutableLiveData<RestObservable> = MutableLiveData()

    fun getCityApiResponse(): LiveData<RestObservable> {
        return getCityResponse
    }

    @SuppressLint("CheckResult")
    fun getCityApiCall(activity: Activity, cityCode: String, isDialogShow: Boolean) {

        if (Helper.isNetworkConnected(activity)) {
            apiService.GET_CITY(cityCode)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {

                    getCityResponse.value =
                        RestObservable.loading(activity, isDialogShow)
                }
                .subscribe(
                    { getCityResponse.value = RestObservable.success(it) },
                    { getCityResponse.value = RestObservable.error(activity, it) }
                )
        } else {

            Helper.showNoInternetAlert(activity,
                activity.getString(R.string.no_internet_connection),
                object : OnNoInternetConnectionListener {
                    override fun onRetryApi() {
                        getCityApiCall(
                            activity, cityCode,
                            isDialogShow
                        )
                    }
                })
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /*Register account call*/
    private var signUpResponse: MutableLiveData<RestObservable> = MutableLiveData()

    fun getSignUpResponse(): LiveData<RestObservable> {
        return signUpResponse
    }

    @SuppressLint("CheckResult")
    fun signUpApi(
        activity: Activity,
        fullname: String,
        email: String,
        password: String,
        phone: String,
        city: String,
        country: String,
        loginType: String,
        deviceType: String,
        deviceToken: String,
        imageUrl: String,
        isDialogShow: Boolean
    ) {
        if (Helper.isNetworkConnected(activity)) {
            var newFile: File? = null
            var imageFileBody: MultipartBody.Part? = null
            if (imageUrl != "") {
                newFile = File(imageUrl)
            }
            if (newFile != null && newFile.exists() && !newFile.equals("")) {
                val mediaType: MediaType?
                if (imageUrl.endsWith("png")) {
                    mediaType = "image/png".toMediaTypeOrNull()
                } else {
                    mediaType = "image/jpeg".toMediaTypeOrNull()
                }
                val requestBody: RequestBody = newFile.asRequestBody(mediaType)
                imageFileBody =
                    MultipartBody.Part.createFormData("photo", newFile.name, requestBody)
            }
            val keyFullName = fullname.toRequestBody("text/plain".toMediaTypeOrNull())
            val keyEmail = email.toRequestBody("text/plain".toMediaTypeOrNull())
            val keyPassword = password.toRequestBody("text/plain".toMediaTypeOrNull())
            val keyPhone = phone.toRequestBody("text/plain".toMediaTypeOrNull())
            val keyCity = city.toRequestBody("text/plain".toMediaTypeOrNull())
            val keyCountry = country.toRequestBody("text/plain".toMediaTypeOrNull())
            val keyLoginType = loginType.toRequestBody("text/plain".toMediaTypeOrNull())
            val keyDeviceType = deviceType.toRequestBody("text/plain".toMediaTypeOrNull())
            val keyDeviceToken = deviceToken.toRequestBody("text/plain".toMediaTypeOrNull())

            apiService.SIGNUP(
                imageFileBody,
                Constants.SECURITY_KEY_CODE,
                keyFullName,
                keyEmail,
                keyPassword,
                keyPhone,
                keyCity,
                keyCountry,
                keyLoginType,
                keyDeviceType,
                keyDeviceToken
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {

                    signUpResponse.value = RestObservable.loading(activity, isDialogShow)
                }
                .subscribe(
                    { signUpResponse.value = RestObservable.success(it) },
                    { signUpResponse.value = RestObservable.error(activity, it) }
                )
        } else {
            Helper.showNoInternetAlert(activity,
                activity.getString(R.string.no_internet_connection),
                object : OnNoInternetConnectionListener {
                    override fun onRetryApi() {
                        signUpApi(
                            activity,
                            fullname,
                            email,
                            password,
                            phone,
                            city,
                            country,
                            loginType,
                            deviceType,
                            deviceType,
                            imageUrl,
                            isDialogShow
                        )
                    }
                })
        }

    }

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //Terms and conditions API call
    private var termsAndConditionsResposne: MutableLiveData<RestObservable> = MutableLiveData()
    fun getTermsAndConditionsResposne(): LiveData<RestObservable> {
        return termsAndConditionsResposne
    }

    @SuppressLint("CheckResult")
    fun termsAndConditionsApi(
        activity: Activity,
        isDialogShow: Boolean
    ) {
        if (Helper.isNetworkConnected(activity)) {
            apiService.TermsAndCondition()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {

                    termsAndConditionsResposne.value =
                        RestObservable.loading(activity, isDialogShow)
                }
                .subscribe(
                    { termsAndConditionsResposne.value = RestObservable.success(it) },
                    {
                        termsAndConditionsResposne.value =
                            RestObservable.error(activity, it)
                    }
                )
        } else {

            Helper.showNoInternetAlert(activity,
                activity.getString(R.string.no_internet_connection),
                object : OnNoInternetConnectionListener {
                    override fun onRetryApi() {
                        termsAndConditionsApi(
                            activity,
                            isDialogShow
                        )
                    }
                })
        }
    }

    /****************************************************************************************************************************************************/
    //logout api call
    private var logoutResposne: MutableLiveData<RestObservable> = MutableLiveData()
    fun getlogoutResposne(): LiveData<RestObservable> {
        return logoutResposne
    }

    @SuppressLint("CheckResult")
    fun logoutApi(
        activity: Activity,
        isDialogShow: Boolean
    ) {
        if (Helper.isNetworkConnected(activity)) {
            apiService.LOGOUT()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {

                    logoutResposne.value =
                        RestObservable.loading(activity, isDialogShow)
                }
                .subscribe(
                    { logoutResposne.value = RestObservable.success(it) },
                    {
                        logoutResposne.value =
                            RestObservable.error(activity, it)
                    }
                )
        } else {

            Helper.showNoInternetAlert(activity,
                activity.getString(R.string.no_internet_connection),
                object : OnNoInternetConnectionListener {
                    override fun onRetryApi() {
                        logoutApi(
                            activity,
                            isDialogShow
                        )
                    }
                })
        }
    }

    /****************************************************************************************************************************************************/
    //Update driver online/offline status

    private var updateDriverOnlineStatusResposne: MutableLiveData<RestObservable> =
        MutableLiveData()

    fun updateDriverOnlineStatusResposne(): LiveData<RestObservable> {
        return updateDriverOnlineStatusResposne
    }

    @SuppressLint("CheckResult")
    fun updateDriverOnlineStatusResposneApi(
        activity: Activity,
        takeOrderStatus: String,
        isDialogShow: Boolean
    ) {

        if (Helper.isNetworkConnected(activity)) {
            apiService.UPDATED_RIVER_TAKE_ORDERSTATUS(takeOrderStatus)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {

                    updateDriverOnlineStatusResposne.value =
                        RestObservable.loading(activity, isDialogShow)
                }
                .subscribe(
                    { updateDriverOnlineStatusResposne.value = RestObservable.success(it) },
                    {
                        updateDriverOnlineStatusResposne.value =
                            RestObservable.error(activity, it)
                    }
                )
        } else {
            Helper.showNoInternetAlert(activity,
                activity.getString(R.string.no_internet_connection),
                object : OnNoInternetConnectionListener {
                    override fun onRetryApi() {
                        updateDriverOnlineStatusResposneApi(
                            activity, takeOrderStatus,
                            isDialogShow
                        )
                    }
                })
        }
    }

    /****************************************************************************************************************************************************/
    //GET DRIVER PROFILE DETAILS

    private var driverDetailsResposne: MutableLiveData<RestObservable> = MutableLiveData()
    fun getDriverDetailsResposne(): LiveData<RestObservable> {
        return driverDetailsResposne
    }

    @SuppressLint("CheckResult")
    fun getDriverDetailsResposneApi(
        activity: Activity,
        userId: Int,
        isDialogShow: Boolean
    ) {
        if (Helper.isNetworkConnected(activity)) {
            apiService.DRIVER_PROFILE_DETAILS(userId.toString(), Constants.SECURITY_KEY_CODE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    driverDetailsResposne.value =
                        RestObservable.loading(activity, isDialogShow)
                }
                .subscribe(
                    { driverDetailsResposne.value = RestObservable.success(it) },
                    {
                        driverDetailsResposne.value = RestObservable.error(activity, it)
                    }
                )
        } else {
            Helper.showNoInternetAlert(activity,
                activity.getString(R.string.no_internet_connection),
                object : OnNoInternetConnectionListener {
                    override fun onRetryApi() {
                        getDriverDetailsResposneApi(
                            activity, userId,
                            isDialogShow
                        )
                    }
                })
        }
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /*Add License details*/
    private var addLicenseResponse: MutableLiveData<RestObservable> = MutableLiveData()

    fun getAddLicenseResponse(): LiveData<RestObservable> {
        return addLicenseResponse
    }

    @SuppressLint("CheckResult")
    fun addLicenseApi(
        activity: Activity,
        licenseType: String,
        licenseNumber: String,
        dob: String,
        issueOn: String,
        expiryDate: String,
        naitionality: String,
        frontImageUrl: String,
        backImageUrl: String,
        token: String,
        isDialogShow: Boolean
    ) {
        if (Helper.isNetworkConnected(activity)) {
            var newFrontFile: File? = null
            var newBackFile: File? = null

            var imageFontFileBody: MultipartBody.Part? = null
            var imageBackFileBody: MultipartBody.Part? = null

            if (frontImageUrl != "") {
                newFrontFile = File(frontImageUrl)
            }

            if (backImageUrl != "") {
                newBackFile = File(backImageUrl)
            }

            if (newFrontFile != null && newFrontFile.exists() && !newFrontFile.equals("")) {
                val mediaType: MediaType?
                if (frontImageUrl.endsWith("png")) {
                    mediaType = "image/png".toMediaTypeOrNull()
                } else {
                    mediaType = "image/jpeg".toMediaTypeOrNull()
                }
                val requestBody: RequestBody = newFrontFile.asRequestBody(mediaType)
                imageFontFileBody =
                    MultipartBody.Part.createFormData("frontPhoto", newFrontFile.name, requestBody)
            }

            if (newBackFile != null && newBackFile.exists() && !newBackFile.equals("")) {
                val mediaType: MediaType?
                if (backImageUrl.endsWith("png")) {
                    mediaType = "image/png".toMediaTypeOrNull()
                } else {
                    mediaType = "image/jpeg".toMediaTypeOrNull()
                }
                val requestBody: RequestBody = newBackFile.asRequestBody(mediaType)
                imageBackFileBody =
                    MultipartBody.Part.createFormData("backPhoto", newBackFile.name, requestBody)
            }

            val keyLicenseType = licenseType.toRequestBody("text/plain".toMediaTypeOrNull())
            val keyLicenseNumber = licenseNumber.toRequestBody("text/plain".toMediaTypeOrNull())
            val keyDob = dob.toRequestBody("text/plain".toMediaTypeOrNull())
            val keyIssueOn = issueOn.toRequestBody("text/plain".toMediaTypeOrNull())
            val keyExpiryDate = expiryDate.toRequestBody("text/plain".toMediaTypeOrNull())
            val keyNaitionality = naitionality.toRequestBody("text/plain".toMediaTypeOrNull())

            apiService.ADD_LICENSE(
                imageFontFileBody,
                imageBackFileBody,
                "Bearer " + token,
                keyLicenseType,
                keyLicenseNumber,
                keyDob,
                keyIssueOn,
                keyExpiryDate,
                keyNaitionality
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    addLicenseResponse.value = RestObservable.loading(activity, isDialogShow)
                }
                .subscribe({ addLicenseResponse.value = RestObservable.success(it) },
                    { addLicenseResponse.value = RestObservable.error(activity, it) })
        } else {
            Helper.showNoInternetAlert(activity,
                activity.getString(R.string.no_internet_connection),
                object : OnNoInternetConnectionListener {
                    override fun onRetryApi() {
                        addLicenseApi(
                            activity,
                            licenseType,
                            licenseNumber,
                            dob,
                            issueOn,
                            expiryDate,
                            naitionality,
                            frontImageUrl,
                            backImageUrl,
                            token,
                            isDialogShow
                        )
                    }
                })
        }

    }   //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /*Add ID details*/
    private var addIdCardResponse: MutableLiveData<RestObservable> = MutableLiveData()
    fun getIdCardResponse(): LiveData<RestObservable> {
        return addIdCardResponse
    }

    @SuppressLint("CheckResult")
    fun addIdCardApi(
        activity: Activity,
        idNumber: String,
        firstName: String,
        lastName: String,
        dob: String,
        issueDate: String,
        address: String,
        frontImageUrl: String,
        backImageUrl: String,
        isDialogShow: Boolean
    ) {
        if (Helper.isNetworkConnected(activity)) {
            var newFrontFile: File? = null
            var newBackFile: File? = null
            var imageFontFileBody: MultipartBody.Part? = null
            var imageBackFileBody: MultipartBody.Part? = null

            if (frontImageUrl != "") {
                newFrontFile = File(frontImageUrl)
            }
            if (backImageUrl != "") {
                newBackFile = File(backImageUrl)
            }
            if (newFrontFile != null && newFrontFile.exists() && !newFrontFile.equals("")) {
                val mediaType: MediaType?
                if (frontImageUrl.endsWith("png")) {
                    mediaType = "image/png".toMediaTypeOrNull()
                } else {
                    mediaType = "image/jpeg".toMediaTypeOrNull()
                }
                val requestBody: RequestBody = newFrontFile.asRequestBody(mediaType)
                imageFontFileBody =
                    MultipartBody.Part.createFormData("frontPhoto", newFrontFile.name, requestBody)
            }
            if (newBackFile != null && newBackFile.exists() && !newBackFile.equals("")) {
                val mediaType: MediaType?
                if (backImageUrl.endsWith("png")) {
                    mediaType = "image/png".toMediaTypeOrNull()
                } else {
                    mediaType = "image/jpeg".toMediaTypeOrNull()
                }
                val requestBody: RequestBody = newBackFile.asRequestBody(mediaType)
                imageBackFileBody =
                    MultipartBody.Part.createFormData("backPhoto", newBackFile.name, requestBody)
            }

            val keyIdNumber = idNumber.toRequestBody("text/plain".toMediaTypeOrNull())
            val keyFirstName = firstName.toRequestBody("text/plain".toMediaTypeOrNull())
            val keyLastName = lastName.toRequestBody("text/plain".toMediaTypeOrNull())
            val keyIssueDate = issueDate.toRequestBody("text/plain".toMediaTypeOrNull())
            val keyDob = dob.toRequestBody("text/plain".toMediaTypeOrNull())
            val keyAddress = address.toRequestBody("text/plain".toMediaTypeOrNull())
            apiService.ADD_ID_CARD_DETAILS(
                imageFontFileBody,
                imageBackFileBody,
                keyFirstName,
                keyLastName,
                keyIdNumber,
                keyIssueDate,
                keyDob,
                keyAddress
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    addIdCardResponse.value = RestObservable.loading(activity, isDialogShow)
                }
                .subscribe({ addIdCardResponse.value = RestObservable.success(it) },
                    { addIdCardResponse.value = RestObservable.error(activity, it) })
        } else {
            Helper.showNoInternetAlert(
                activity,
                activity.getString(R.string.no_internet_connection),
                object : OnNoInternetConnectionListener {
                    override fun onRetryApi() {
                        addIdCardApi(
                            activity,
                            idNumber,
                            firstName,
                            lastName,
                            dob,
                            issueDate,
                            address,
                            frontImageUrl,
                            backImageUrl,
                            isDialogShow
                        )
                    }
                })
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /*Update ID details*/
    private var updateIdCardResponse: MutableLiveData<RestObservable> = MutableLiveData()
    fun getUpdateIdCardResponse(): LiveData<RestObservable> {
        return updateIdCardResponse
    }

    @SuppressLint("CheckResult")
    fun updateIdCardApi(
        activity: Activity,
        cardId: String,
        idNumber: String,
        firstName: String,
        lastName: String,
        dob: String,
        issueDate: String,
        address: String,
        frontImageUrl: String,
        backImageUrl: String,
        isDialogShow: Boolean
    ) {
        if (Helper.isNetworkConnected(activity)) {
            var newFrontFile: File? = null
            var newBackFile: File? = null
            var imageFontFileBody: MultipartBody.Part? = null
            var imageBackFileBody: MultipartBody.Part? = null
            if (frontImageUrl != "") {
                newFrontFile = File(frontImageUrl)
            }
            if (backImageUrl != "") {
                newBackFile = File(backImageUrl)
            }
            if (newFrontFile != null && newFrontFile.exists() && !newFrontFile.equals("")) {
                val mediaType: MediaType?
                if (frontImageUrl.endsWith("png")) {
                    mediaType = "image/png".toMediaTypeOrNull()
                } else {
                    mediaType = "image/jpeg".toMediaTypeOrNull()
                }
                val requestBody: RequestBody = newFrontFile.asRequestBody(mediaType)
                imageFontFileBody =
                    MultipartBody.Part.createFormData("frontPhoto", newFrontFile.name, requestBody)
            }
            if (newBackFile != null && newBackFile.exists() && !newBackFile.equals("")) {
                val mediaType: MediaType?
                if (backImageUrl.endsWith("png")) {
                    mediaType = "image/png".toMediaTypeOrNull()
                } else {
                    mediaType = "image/jpeg".toMediaTypeOrNull()
                }
                val requestBody: RequestBody = newBackFile.asRequestBody(mediaType)
                imageBackFileBody =
                    MultipartBody.Part.createFormData("backPhoto", newBackFile.name, requestBody)
            }

            val keyCardId = cardId.toRequestBody("text/plain".toMediaTypeOrNull())
            val keyIdNumber = idNumber.toRequestBody("text/plain".toMediaTypeOrNull())
            val keyFirstName = firstName.toRequestBody("text/plain".toMediaTypeOrNull())
            val keyLastName = lastName.toRequestBody("text/plain".toMediaTypeOrNull())
            val keyIssueDate = issueDate.toRequestBody("text/plain".toMediaTypeOrNull())
            val keyDob = dob.toRequestBody("text/plain".toMediaTypeOrNull())
            val keyAddress = address.toRequestBody("text/plain".toMediaTypeOrNull())
            apiService.UPDATE_ID_CARD(
                imageFontFileBody,
                imageBackFileBody,
                keyCardId,
                keyFirstName,
                keyLastName,
                keyIdNumber,
                keyIssueDate,
                keyDob,
                keyAddress
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    updateIdCardResponse.value = RestObservable.loading(activity, isDialogShow)
                }
                .subscribe({ updateIdCardResponse.value = RestObservable.success(it) },
                    { updateIdCardResponse.value = RestObservable.error(activity, it) })
        } else {
            Helper.showNoInternetAlert(
                activity,
                activity.getString(R.string.no_internet_connection),
                object : OnNoInternetConnectionListener {
                    override fun onRetryApi() {
                        updateIdCardApi(
                            activity,
                            cardId,
                            idNumber,
                            firstName,
                            lastName,
                            dob,
                            issueDate,
                            address,
                            frontImageUrl,
                            backImageUrl,
                            isDialogShow
                        )
                    }
                })
        }
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /****************************************************************************************************************************************************/
    //Get ID Details
    private var getIdDetailsResposne: MutableLiveData<RestObservable> = MutableLiveData()
    fun getIdDetails(): LiveData<RestObservable> {
        return getIdDetailsResposne
    }

    @SuppressLint("CheckResult")
    fun getIdDetailsApi(activity: Activity, isDialogShow: Boolean) {
        if (Helper.isNetworkConnected(activity)) {
            apiService.GET_ID_CARD_DEATILS()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    getIdDetailsResposne.value = RestObservable.loading(activity, isDialogShow)
                }
                .subscribe(
                    { getIdDetailsResposne.value = RestObservable.success(it) },
                    { getIdDetailsResposne.value = RestObservable.error(activity, it) })
        } else {
            Helper.showNoInternetAlert(activity,
                activity.getString(R.string.no_internet_connection),
                object : OnNoInternetConnectionListener {
                    override fun onRetryApi() {
                        getIdDetailsApi(activity, isDialogShow)
                    }
                })
        }
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /*Edit License details*/
    private var editLicenseResponse: MutableLiveData<RestObservable> = MutableLiveData()

    fun getEditLicenseResponse(): LiveData<RestObservable> {
        return editLicenseResponse
    }

    @SuppressLint("CheckResult")
    fun editLicenseApi(
        activity: Activity,
        licenseId: String,
        licenseType: String,
        licenseNumber: String,
        dob: String,
        issueOn: String,
        expiryDate: String,
        naitionality: String,
        frontImageUrl: String,
        backImageUrl: String,
        isDialogShow: Boolean
    ) {
        if (Helper.isNetworkConnected(activity)) {
            var newFrontFile: File? = null
            var newBackFile: File? = null

            var imageFontFileBody: MultipartBody.Part? = null
            var imageBackFileBody: MultipartBody.Part? = null

            if (frontImageUrl != "") {
                newFrontFile = File(frontImageUrl)
            }

            if (backImageUrl != "") {
                newBackFile = File(backImageUrl)
            }

            if (newFrontFile != null && newFrontFile.exists() && !newFrontFile.equals("")) {
                val mediaType: MediaType?
                if (frontImageUrl.endsWith("png")) {
                    mediaType = "image/png".toMediaTypeOrNull()
                } else {
                    mediaType = "image/jpeg".toMediaTypeOrNull()
                }
                val requestBody: RequestBody = newFrontFile.asRequestBody(mediaType)
                imageFontFileBody =
                    MultipartBody.Part.createFormData("frontPhoto", newFrontFile.name, requestBody)
            }

            if (newBackFile != null && newBackFile.exists() && !newBackFile.equals("")) {
                val mediaType: MediaType?
                if (backImageUrl.endsWith("png")) {
                    mediaType = "image/png".toMediaTypeOrNull()
                } else {
                    mediaType = "image/jpeg".toMediaTypeOrNull()
                }
                val requestBody: RequestBody = newBackFile.asRequestBody(mediaType)
                imageBackFileBody =
                    MultipartBody.Part.createFormData("backPhoto", newBackFile.name, requestBody)
            }

            val keyLicenseId = licenseId.toRequestBody("text/plain".toMediaTypeOrNull())
            val keyLicenseType = licenseType.toRequestBody("text/plain".toMediaTypeOrNull())
            val keyLicenseNumber = licenseNumber.toRequestBody("text/plain".toMediaTypeOrNull())
            val keyDob = dob.toRequestBody("text/plain".toMediaTypeOrNull())
            val keyIssueOn = issueOn.toRequestBody("text/plain".toMediaTypeOrNull())
            val keyExpiryDate = expiryDate.toRequestBody("text/plain".toMediaTypeOrNull())
            val keyNaitionality = naitionality.toRequestBody("text/plain".toMediaTypeOrNull())

            apiService.UPDATE_LICENSE(
                imageFontFileBody,
                imageBackFileBody,
                keyLicenseId,
                keyLicenseType,
                keyLicenseNumber,
                keyDob,
                keyIssueOn,
                keyExpiryDate,
                keyNaitionality
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    editLicenseResponse.value = RestObservable.loading(activity, isDialogShow)
                }
                .subscribe({ editLicenseResponse.value = RestObservable.success(it) },
                    { editLicenseResponse.value = RestObservable.error(activity, it) })
        } else {
            Helper.showNoInternetAlert(activity,
                activity.getString(R.string.no_internet_connection),
                object : OnNoInternetConnectionListener {
                    override fun onRetryApi() {
                        editLicenseApi(
                            activity,
                            licenseId,
                            licenseType,
                            licenseNumber,
                            dob,
                            issueOn,
                            expiryDate,
                            naitionality,
                            frontImageUrl,
                            backImageUrl,
                            isDialogShow
                        )
                    }
                })
        }

    }

    /****************************************************************************************************************************************************/
    //GET DRIVER PROFILE DETAILS

    private var driverLicenseDetailsResposne: MutableLiveData<RestObservable> = MutableLiveData()
    fun getDriverLicenseDetailsResposne(): LiveData<RestObservable> {
        return driverLicenseDetailsResposne
    }

    @SuppressLint("CheckResult")
    fun getDriverLicenseDetailsApi(
        activity: Activity,
        isDialogShow: Boolean
    ) {
        if (Helper.isNetworkConnected(activity)) {
            apiService.GET_LICENSE_DETAILS()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    driverLicenseDetailsResposne.value =
                        RestObservable.loading(activity, isDialogShow)
                }
                .subscribe(
                    { driverLicenseDetailsResposne.value = RestObservable.success(it) },
                    {
                        driverLicenseDetailsResposne.value = RestObservable.error(activity, it)
                    }
                )
        } else {
            Helper.showNoInternetAlert(activity,
                activity.getString(R.string.no_internet_connection),
                object : OnNoInternetConnectionListener {
                    override fun onRetryApi() {
                        getDriverLicenseDetailsApi(activity, isDialogShow)
                    }
                })
        }
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /*Edit profile details api call*/
    private var editProfileResponse: MutableLiveData<RestObservable> = MutableLiveData()

    fun getEditProfileResponse(): LiveData<RestObservable> {
        return editProfileResponse
    }

    @SuppressLint("CheckResult")
    fun editProfileApi(
        activity: Activity,
        countryCode: String,
        phone: String,
        latitude: String,
        longitude: String,
//            address: String,
        city: String,
        country: String,
        fullName: String,
        imageUrl: String,
        isDialogShow: Boolean
    ) {
        if (Helper.isNetworkConnected(activity)) {
            var newFile: File? = null
            var imageFileBody: MultipartBody.Part? = null
            if (imageUrl != "") {
                newFile = File(imageUrl)
            }
            if (newFile != null && newFile.exists() && !newFile.equals("")) {
                val mediaType: MediaType?
                if (imageUrl.endsWith("png")) {
                    mediaType = "image/png".toMediaTypeOrNull()
                } else {
                    mediaType = "image/jpeg".toMediaTypeOrNull()
                }
                val requestBody: RequestBody = newFile.asRequestBody(mediaType)
                imageFileBody =
                    MultipartBody.Part.createFormData("photo", newFile.name, requestBody)
            }
            val keyCountryCode = countryCode.toRequestBody("text/plain".toMediaTypeOrNull())
            val keyPhone = phone.toRequestBody("text/plain".toMediaTypeOrNull())
            val keyLatitude = latitude.toRequestBody("text/plain".toMediaTypeOrNull())
            val keyLongitude = longitude.toRequestBody("text/plain".toMediaTypeOrNull())
//            val keyAddress = address.toRequestBody("text/plain".toMediaTypeOrNull())
            val keyCity = city.toRequestBody("text/plain".toMediaTypeOrNull())
            val keyCountry = country.toRequestBody("text/plain".toMediaTypeOrNull())
            val keyFullName = fullName.toRequestBody("text/plain".toMediaTypeOrNull())

            apiService.EDIT_DRIVER_PROFILE(
                imageFileBody,
                keyCountryCode,
                keyPhone,
                keyLatitude,
                keyLongitude,
                keyCity,
                keyCity,
                keyFullName
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {

                    editProfileResponse.value = RestObservable.loading(activity, isDialogShow)
                }
                .subscribe(
                    { editProfileResponse.value = RestObservable.success(it) },
                    { editProfileResponse.value = RestObservable.error(activity, it) }
                )
        } else {
            Helper.showNoInternetAlert(activity,
                activity.getString(R.string.no_internet_connection),
                object : OnNoInternetConnectionListener {
                    override fun onRetryApi() {
                        editProfileApi(
                            activity,
                            countryCode,
                            phone,
                            latitude,
                            longitude,
                            city,
                            country,
                            fullName,
                            imageUrl,
                            isDialogShow
                        )
                    }
                })
        }

    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Change user password
     */
    var changePasswordResposne: MutableLiveData<RestObservable> = MutableLiveData()
    fun getChangePasswordResponse(): LiveData<RestObservable> {
        return changePasswordResposne
    }

    @SuppressLint("CheckResult")
    fun changePasswordApi(
        activity: Activity,
        currentPassword: String,
        newPassword: String,
        isDialogShow: Boolean
    ) {
        if (Helper.isNetworkConnected(activity)) {
            apiService.CHANGE_PASSWORD(currentPassword, newPassword)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {

                    changePasswordResposne.value =
                        RestObservable.loading(activity, isDialogShow)
                }
                .subscribe(
                    { changePasswordResposne.value = RestObservable.success(it) },
                    {
                        changePasswordResposne.value =
                            RestObservable.error(activity, it)
                    }
                )
        } else {

            Helper.showNoInternetAlert(activity,
                activity.getString(R.string.no_internet_connection),
                object : OnNoInternetConnectionListener {
                    override fun onRetryApi() {
                        changePasswordApi(
                            activity,
                            currentPassword, newPassword,
                            isDialogShow
                        )
                    }
                })
        }
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Change notificationn status
     */
    var changeNotificationStatus: MutableLiveData<RestObservable> = MutableLiveData()
    fun getchangeNotificationStatusResponse(): LiveData<RestObservable> {
        return changeNotificationStatus
    }

    @SuppressLint("CheckResult")
    fun changeNotificationStatusApi(
        activity: Activity,
        type: String,
        isDialogShow: Boolean
    ) {
        if (Helper.isNetworkConnected(activity)) {
            apiService.UPDATE_NOTIFICATION_STATUS(
                type
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {

                    changeNotificationStatus.value =
                        RestObservable.loading(activity, isDialogShow)
                }
                .subscribe(
                    { changeNotificationStatus.value = RestObservable.success(it) },
                    {
                        changeNotificationStatus.value =
                            RestObservable.error(activity, it)
                    }
                )
        } else {

            Helper.showNoInternetAlert(activity,
                activity.getString(R.string.no_internet_connection),
                object : OnNoInternetConnectionListener {
                    override fun onRetryApi() {
                        changeNotificationStatusApi(
                            activity,
                            type,
                            isDialogShow
                        )
                    }
                })
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Update driver Latitude and Longitude
     */
    var updateDriverLatLongResposne: MutableLiveData<RestObservable> = MutableLiveData()
    fun getUpdateDriverLatLongResponse(): LiveData<RestObservable> {
        return updateDriverLatLongResposne
    }

    @SuppressLint("CheckResult")
    fun updateDriverLatLongApi(
        activity: Activity,
        latitude: String,
        longitude: String,
        isDialogShow: Boolean
    ) {
        if (Helper.isNetworkConnected(activity)) {
            apiService.UDPATE_DRIVER_LAT_LONG(latitude, longitude)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    updateDriverLatLongResposne.value =
                        RestObservable.loading(activity, isDialogShow)
                }
                .subscribe(
                    { updateDriverLatLongResposne.value = RestObservable.success(it) },
                    { updateDriverLatLongResposne.value = RestObservable.error(activity, it) })
        } else {
            Helper.showNoInternetAlert(activity,
                activity.getString(R.string.no_internet_connection),
                object : OnNoInternetConnectionListener {
                    override fun onRetryApi() {
                        updateDriverLatLongApi(activity, latitude, longitude, isDialogShow)
                    }
                })
        }
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //*JobHistory APi call

    private var jobHistoryResposne: MutableLiveData<RestObservable> = MutableLiveData()
    fun getJobHistoryResposne(): LiveData<RestObservable> {
        return jobHistoryResposne
    }

    @SuppressLint("CheckResult")
    fun jobHistoryApi(
        activity: Activity,
        isDialogShow: Boolean
    ) {
        if (Helper.isNetworkConnected(activity)) {
            apiService.JOB_HISTORY()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    jobHistoryResposne.value = RestObservable.loading(activity, isDialogShow)
                }
                .subscribe(
                    { jobHistoryResposne.value = RestObservable.success(it) },
                    {
                        jobHistoryResposne.value = RestObservable.error(activity, it)
                    }
                )
        } else {

            Helper.showNoInternetAlert(activity,
                activity.getString(R.string.no_internet_connection),
                object : OnNoInternetConnectionListener {
                    override fun onRetryApi() {
                        jobHistoryApi(
                            activity,
                            isDialogShow
                        )
                    }
                })
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Job history Details
     */
    var jobHistoryDetailsResposne: MutableLiveData<RestObservable> = MutableLiveData()
    fun getJobHistoryDetailsResponse(): LiveData<RestObservable> {
        return jobHistoryDetailsResposne
    }

    @SuppressLint("CheckResult")
    fun jobHistoryDetailsApi(
        activity: Activity,
        jobHistoryId: String,
        isDialogShow: Boolean
    ) {
        if (Helper.isNetworkConnected(activity)) {
            apiService.JOB_HISTORY_DETAILS(jobHistoryId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {

                    jobHistoryDetailsResposne.value =
                        RestObservable.loading(activity, isDialogShow)
                }
                .subscribe(
                    { jobHistoryDetailsResposne.value = RestObservable.success(it) },
                    {
                        jobHistoryDetailsResposne.value =
                            RestObservable.error(activity, it)
                    }
                )
        } else {

            Helper.showNoInternetAlert(activity,
                activity.getString(R.string.no_internet_connection),
                object : OnNoInternetConnectionListener {
                    override fun onRetryApi() {
                        jobHistoryDetailsApi(
                            activity,
                            jobHistoryId,
                            isDialogShow
                        )
                    }
                })
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //*Payment status list APi call

    private var paymentStatusListResposne: MutableLiveData<RestObservable> = MutableLiveData()
    fun getPaymentStatusListResposne(): LiveData<RestObservable> {
        return paymentStatusListResposne
    }

    @SuppressLint("CheckResult")
    fun paymentStatusListApi(
        activity: Activity,
        type: String,
        isDialogShow: Boolean
    ) {
        if (Helper.isNetworkConnected(activity)) {
            apiService.PAYMENT_STATUS_LISTING(type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    paymentStatusListResposne.value = RestObservable.loading(activity, isDialogShow)
                }
                .subscribe(
                    { paymentStatusListResposne.value = RestObservable.success(it) },
                    {
                        paymentStatusListResposne.value = RestObservable.error(activity, it)
                    }
                )
        } else {

            Helper.showNoInternetAlert(activity,
                activity.getString(R.string.no_internet_connection),
                object : OnNoInternetConnectionListener {
                    override fun onRetryApi() {
                        paymentStatusListApi(
                            activity, type,
                            isDialogShow
                        )
                    }
                })
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //*Payment status list APi call

    private var earningResposne: MutableLiveData<RestObservable> = MutableLiveData()
    fun getEarningResposneResposne(): LiveData<RestObservable> {
        return earningResposne
    }

    @SuppressLint("CheckResult")
    fun driverEarningsApi(
        activity: Activity,
        isDialogShow: Boolean
    ) {
        if (Helper.isNetworkConnected(activity)) {
            apiService.EARNINGS()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    earningResposne.value = RestObservable.loading(activity, isDialogShow)
                }
                .subscribe(
                    { earningResposne.value = RestObservable.success(it) },
                    {
                        earningResposne.value = RestObservable.error(activity, it)
                    }
                )
        } else {

            Helper.showNoInternetAlert(activity,
                activity.getString(R.string.no_internet_connection),
                object : OnNoInternetConnectionListener {
                    override fun onRetryApi() {
                        driverEarningsApi(
                            activity,
                            isDialogShow
                        )
                    }
                })
        }
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //*get current rider APi call

    private var currentRideResposne: MutableLiveData<RestObservable> = MutableLiveData()
    fun getCurrentRideResposne(): LiveData<RestObservable> {
        return currentRideResposne
    }

    @SuppressLint("CheckResult")
    fun currentRideApi(
        activity: Activity,
        isDialogShow: Boolean
    ) {
        if (Helper.isNetworkConnected(activity)) {
            apiService.GET_CURRENT_RIDE()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    currentRideResposne.value = RestObservable.loading(activity, isDialogShow)
                }
                .subscribe(
                    { currentRideResposne.value = RestObservable.success(it) },
                    {
                        currentRideResposne.value = RestObservable.error(activity, it)
                    }
                )
        } else {

            Helper.showNoInternetAlert(activity,
                activity.getString(R.string.no_internet_connection),
                object : OnNoInternetConnectionListener {
                    override fun onRetryApi() {
                        currentRideApi(
                            activity,
                            isDialogShow
                        )
                    }
                })
        }
    }

    /****************************************************************************************************************************************************/
    //Reposne to Ride request

    private var riderDetailsResposne: MutableLiveData<RestObservable> = MutableLiveData()

    fun getRiderDetailsResposneResposne(): LiveData<RestObservable> {
        return riderDetailsResposne
    }

    @SuppressLint("CheckResult")
    fun riderDetailsResposneApi(
        activity: Activity,
        rideRequestId: String,
        isDialogShow: Boolean
    ) {

        if (Helper.isNetworkConnected(activity)) {
            apiService.GET_RIDER_DETAILS(rideRequestId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    riderDetailsResposne.value = RestObservable.loading(activity, isDialogShow)
                }
                .subscribe(
                    { riderDetailsResposne.value = RestObservable.success(it) },
                    {
                        riderDetailsResposne.value = RestObservable.error(activity, it)
                    }
                )
        } else {
            Helper.showNoInternetAlert(activity,
                activity.getString(R.string.no_internet_connection),
                object : OnNoInternetConnectionListener {
                    override fun onRetryApi() {
                        riderDetailsResposneApi(
                            activity, rideRequestId,
                            isDialogShow
                        )
                    }
                })
        }
    }


    /****************************************************************************************************************************************************/
    //Reposne to Ride request

    private var responseRideRequestResposne: MutableLiveData<RestObservable> = MutableLiveData()

    fun getResponseRideRequestResposne(): LiveData<RestObservable> {
        return responseRideRequestResposne
    }

    @SuppressLint("CheckResult")
    fun responseRideRequestApi(
        activity: Activity,
        rideRequestId: String,
        response: String,
        isDialogShow: Boolean
    ) {

        if (Helper.isNetworkConnected(activity)) {
            //1=>Accept 2=>Reject
            apiService.RESPOND_RIDE_REQUEST(rideRequestId, response)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {

                    responseRideRequestResposne.value =
                        RestObservable.loading(activity, isDialogShow)
                }
                .subscribe(
                    { responseRideRequestResposne.value = RestObservable.success(it) },
                    {
                        responseRideRequestResposne.value =
                            RestObservable.error(activity, it)
                    }
                )
        } else {
            Helper.showNoInternetAlert(activity,
                activity.getString(R.string.no_internet_connection),
                object : OnNoInternetConnectionListener {
                    override fun onRetryApi() {
                        responseRideRequestApi(
                            activity, rideRequestId, response,
                            isDialogShow
                        )
                    }
                })
        }
    }

    /****************************************************************************************************************************************************/
    //Change Ride status of current ride

    private var changeRideStatusResposne: MutableLiveData<RestObservable> = MutableLiveData()
    fun getChangeRideStatusResposne(): LiveData<RestObservable> {
        return changeRideStatusResposne
    }

    @SuppressLint("CheckResult")
    fun changeRideStatusApi(
        activity: Activity,
        rideRequestId: String,
        rideStatus: String,
        isDialogShow: Boolean
    ) {

        if (Helper.isNetworkConnected(activity)) {
//            2=>Start 3=>Complete 4=>Cancel
            apiService.CHANGE_RIDE_STATUS(rideRequestId, rideStatus)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {

                    changeRideStatusResposne.value =
                        RestObservable.loading(activity, isDialogShow)
                }
                .subscribe(
                    { changeRideStatusResposne.value = RestObservable.success(it) },
                    {
                        changeRideStatusResposne.value =
                            RestObservable.error(activity, it)
                    }
                )
        } else {
            Helper.showNoInternetAlert(activity,
                activity.getString(R.string.no_internet_connection),
                object : OnNoInternetConnectionListener {
                    override fun onRetryApi() {
                        changeRideStatusApi(
                            activity, rideRequestId, rideStatus,
                            isDialogShow
                        )
                    }
                })
        }
    }

    /****************************************************************************************************************************************************/
    //I am here API call
    private var iAmHereResposne: MutableLiveData<RestObservable> = MutableLiveData()

    fun getIamHereResposne(): LiveData<RestObservable> {
        return iAmHereResposne
    }

    @SuppressLint("CheckResult")
    fun iAmHereApi(
        activity: Activity,
        rideId: String,
        isDialogShow: Boolean
    ) {

        if (Helper.isNetworkConnected(activity)) {
            apiService.I_AM_HERE(rideId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    iAmHereResposne.value = RestObservable.loading(activity, isDialogShow)
                }
                .subscribe(
                    { iAmHereResposne.value = RestObservable.success(it) },
                    {
                        iAmHereResposne.value = RestObservable.error(activity, it)
                    }
                )
        } else {
            Helper.showNoInternetAlert(activity,
                activity.getString(R.string.no_internet_connection),
                object : OnNoInternetConnectionListener {
                    override fun onRetryApi() {
                        iAmHereApi(
                            activity, rideId,
                            isDialogShow
                        )
                    }
                })
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //*Payment status list APi call

    private var filterEarningResposne: MutableLiveData<RestObservable> = MutableLiveData()
    fun getfilterEarningResposneResposne(): LiveData<RestObservable> {
        return filterEarningResposne
    }

    @SuppressLint("CheckResult")
    fun driverFilterEarningsApi(
        activity: Activity,
        startDate: String,
        endDate: String,
        isDialogShow: Boolean
    ) {
        if (Helper.isNetworkConnected(activity)) {
            apiService.FILTER_EARNINGS(startDate, endDate)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    filterEarningResposne.value = RestObservable.loading(activity, isDialogShow)
                }
                .subscribe(
                    { filterEarningResposne.value = RestObservable.success(it) },
                    {
                        filterEarningResposne.value = RestObservable.error(activity, it)
                    }
                )
        } else {

            Helper.showNoInternetAlert(activity,
                activity.getString(R.string.no_internet_connection),
                object : OnNoInternetConnectionListener {
                    override fun onRetryApi() {
                        driverFilterEarningsApi(
                            activity, startDate, endDate,
                            isDialogShow
                        )
                    }
                })
        }
    }
//////////////////////////////////New API's
    /****************************************************************************************************************************************************/
    //Get documentation Details
    private var getDocumentationDetailsResposne: MutableLiveData<RestObservable> = MutableLiveData()
    fun getDocumentationDetails(): LiveData<RestObservable> {
        return getDocumentationDetailsResposne
    }

    @SuppressLint("CheckResult")
    fun getDocumentationDetailsApi(activity: Activity, isDialogShow: Boolean) {
        if (Helper.isNetworkConnected(activity)) {
            apiService.GET_DOCUMENTATION_DETAILS()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    getDocumentationDetailsResposne.value =
                        RestObservable.loading(activity, isDialogShow)
                }
                .subscribe(
                    { getDocumentationDetailsResposne.value = RestObservable.success(it) },
                    { getDocumentationDetailsResposne.value = RestObservable.error(activity, it) })
        } else {
            Helper.showNoInternetAlert(activity,
                activity.getString(R.string.no_internet_connection),
                object : OnNoInternetConnectionListener {
                    override fun onRetryApi() {
                        getDocumentationDetailsApi(activity, isDialogShow)
                    }
                })
        }
    }

    /*Edit police record*/
    private var editPoliceRecordResponse: MutableLiveData<RestObservable> = MutableLiveData()
    fun getPoliceRecordResponse(): LiveData<RestObservable> {
        return editPoliceRecordResponse
    }

    @SuppressLint("CheckResult")
    fun addUpdatePoliceRecordApi(
        activity: Activity,
        image: String,
        isDialogShow: Boolean
    ) {
        if (Helper.isNetworkConnected(activity)) {
            var imageFile: File? = null
            var imageFileBody: MultipartBody.Part? = null
            if (image != "") {
                imageFile = File(image)
            }
            if (imageFile != null && imageFile.exists() && !imageFile.equals("")) {
                val mediaType: MediaType?
                if (image.endsWith("png")) {
                    mediaType = "image/png".toMediaTypeOrNull()
                } else {
                    mediaType = "image/jpeg".toMediaTypeOrNull()
                }
                val requestBody: RequestBody = imageFile.asRequestBody(mediaType)
                imageFileBody =
                    MultipartBody.Part.createFormData("policeRecord", imageFile.name, requestBody)
            }
            apiService.ADD_UPDATE_POLICE_RECORD(imageFileBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    editPoliceRecordResponse.value = RestObservable.loading(activity, isDialogShow)
                }
                .subscribe({ editPoliceRecordResponse.value = RestObservable.success(it) },
                    { editPoliceRecordResponse.value = RestObservable.error(activity, it) })
        } else {
            Helper.showNoInternetAlert(activity,
                activity.getString(R.string.no_internet_connection),
                object : OnNoInternetConnectionListener {
                    override fun onRetryApi() {
                        addUpdatePoliceRecordApi(activity, image, isDialogShow)
                    }
                })
        }
    }

    /*Edit car insurance record*/
    private var editCarInsuranceResponse: MutableLiveData<RestObservable> = MutableLiveData()
    fun getCarInsuranceResponse(): LiveData<RestObservable> {
        return editCarInsuranceResponse
    }

    @SuppressLint("CheckResult")
    fun addUpdateCarInsuranceApi(
        activity: Activity,
        image: String,
        isDialogShow: Boolean
    ) {
        if (Helper.isNetworkConnected(activity)) {
            var imageFile: File? = null
            var imageFileBody: MultipartBody.Part? = null
            if (image != "") {
                imageFile = File(image)
            }
            if (imageFile != null && imageFile.exists() && !imageFile.equals("")) {
                val mediaType: MediaType?
                if (image.endsWith("png")) {
                    mediaType = "image/png".toMediaTypeOrNull()
                } else {
                    mediaType = "image/jpeg".toMediaTypeOrNull()
                }
                val requestBody: RequestBody = imageFile.asRequestBody(mediaType)
                imageFileBody =
                    MultipartBody.Part.createFormData("carInsurance", imageFile.name, requestBody)
            }
            apiService.ADD_UPDATE_CAR_INSURANCE_RECORD(imageFileBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    editCarInsuranceResponse.value = RestObservable.loading(activity, isDialogShow)
                }
                .subscribe({ editCarInsuranceResponse.value = RestObservable.success(it) },
                    { editCarInsuranceResponse.value = RestObservable.error(activity, it) })
        } else {
            Helper.showNoInternetAlert(activity,
                activity.getString(R.string.no_internet_connection),
                object : OnNoInternetConnectionListener {
                    override fun onRetryApi() {
                        addUpdateCarInsuranceApi(activity, image, isDialogShow)
                    }
                })
        }
    }

    /*delete car insurance record*/
    private var deleteCarInsuranceResponse: MutableLiveData<RestObservable> = MutableLiveData()
    fun deleteCarInsuranceResponse(): LiveData<RestObservable> {
        return deleteCarInsuranceResponse
    }

    @SuppressLint("CheckResult")
    fun deleteCarInsuranceApi(
        activity: Activity,
        isDialogShow: Boolean
    ) {
        if (Helper.isNetworkConnected(activity)) {

            apiService.DELETE_CAR_INSURANCE_RECORD()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    deleteCarInsuranceResponse.value =
                        RestObservable.loading(activity, isDialogShow)
                }
                .subscribe({ deleteCarInsuranceResponse.value = RestObservable.success(it) },
                    { deleteCarInsuranceResponse.value = RestObservable.error(activity, it) })
        } else {
            Helper.showNoInternetAlert(activity,
                activity.getString(R.string.no_internet_connection),
                object : OnNoInternetConnectionListener {
                    override fun onRetryApi() {
                        deleteCarInsuranceApi(activity, isDialogShow)
                    }
                })
        }
    }

    /*delete police record record*/
    private var deletePoliceRecordResponse: MutableLiveData<RestObservable> = MutableLiveData()
    fun deletePoliceRecordResponse(): LiveData<RestObservable> {
        return deletePoliceRecordResponse
    }

    @SuppressLint("CheckResult")
    fun deletePoliceRecordApi(
        activity: Activity,
        isDialogShow: Boolean
    ) {
        if (Helper.isNetworkConnected(activity)) {

            apiService.DELETE_POLICE_RECORD()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    deletePoliceRecordResponse.value =
                        RestObservable.loading(activity, isDialogShow)
                }
                .subscribe({ deletePoliceRecordResponse.value = RestObservable.success(it) },
                    { deletePoliceRecordResponse.value = RestObservable.error(activity, it) })
        } else {
            Helper.showNoInternetAlert(activity,
                activity.getString(R.string.no_internet_connection),
                object : OnNoInternetConnectionListener {
                    override fun onRetryApi() {
                        deletePoliceRecordApi(activity, isDialogShow)
                    }
                })
        }
    }

    /*delete driver id card*/
    private var deleteDriverIdCardResponse: MutableLiveData<RestObservable> = MutableLiveData()
    fun deleteDriverIdCardResponse(): LiveData<RestObservable> {
        return deleteDriverIdCardResponse
    }

    @SuppressLint("CheckResult")
    fun deleteDriverIdCardApi(
        activity: Activity,
        idOfDriverId: String,
        isDialogShow: Boolean
    ) {
        if (Helper.isNetworkConnected(activity)) {

            apiService.DELETE_ID_CARD_IMAGE(idOfDriverId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    deleteDriverIdCardResponse.value =
                        RestObservable.loading(activity, isDialogShow)
                }
                .subscribe({ deleteDriverIdCardResponse.value = RestObservable.success(it) },
                    { deleteDriverIdCardResponse.value = RestObservable.error(activity, it) })
        } else {
            Helper.showNoInternetAlert(activity,
                activity.getString(R.string.no_internet_connection),
                object : OnNoInternetConnectionListener {
                    override fun onRetryApi() {
                        deleteDriverIdCardApi(activity, idOfDriverId, isDialogShow)
                    }
                })
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /*Upload receipt by driver*/
    private var uploadReceiptResponse: MutableLiveData<RestObservable> = MutableLiveData()
    fun getUploadReceiptResponse(): LiveData<RestObservable> {
        return uploadReceiptResponse
    }

    @SuppressLint("CheckResult")
    fun uploadReceiptApi(
        activity: Activity,
        receiptUpload: String,
        orderId: String,
        isDialogShow: Boolean
    ) {
        if (Helper.isNetworkConnected(activity)) {
            var receiptUploadFile: File? = null
            var receiptFileBody: MultipartBody.Part? = null
            if (receiptUpload != "") {
                receiptUploadFile = File(receiptUpload)
            }
            if (receiptUploadFile != null && receiptUploadFile.exists() && !receiptUploadFile.equals(
                    ""
                )
            ) {
                val mediaType: MediaType?
                if (receiptUpload.endsWith("png")) {
                    mediaType = "image/png".toMediaTypeOrNull()
                } else {
                    mediaType = "image/jpeg".toMediaTypeOrNull()
                }
                val requestBody: RequestBody = receiptUploadFile.asRequestBody(mediaType)
                receiptFileBody = MultipartBody.Part.createFormData(
                    "receiptUpload",
                    receiptUploadFile.name,
                    requestBody
                )
            }

            val keyOrderId = orderId.toRequestBody("text/plain".toMediaTypeOrNull())
            apiService.UPLOAD_RECEIPT(receiptFileBody, keyOrderId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    uploadReceiptResponse.value = RestObservable.loading(activity, isDialogShow)
                }
                .subscribe({ uploadReceiptResponse.value = RestObservable.success(it) },
                    { uploadReceiptResponse.value = RestObservable.error(activity, it) })
        } else {
            Helper.showNoInternetAlert(
                activity,
                activity.getString(R.string.no_internet_connection),
                object : OnNoInternetConnectionListener {
                    override fun onRetryApi() {
                        uploadReceiptApi(activity, receiptUpload, orderId, isDialogShow)
                    }
                })
        }
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /****************************************************************************************************************************************************/
    //Get Driver available slots
    private var getdriverAvailableSlotsResposne: MutableLiveData<RestObservable> = MutableLiveData()
    fun getDriverAvailableSlotsResposne(): LiveData<RestObservable> {
        return getdriverAvailableSlotsResposne
    }

    @SuppressLint("CheckResult")
    fun getdriverAvailableSlotsApi(activity: Activity, isDialogShow: Boolean) {
        if (Helper.isNetworkConnected(activity)) {
            apiService.DRIVER_SLOTS()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    getdriverAvailableSlotsResposne.value =
                        RestObservable.loading(activity, isDialogShow)
                }
                .subscribe(
                    { getdriverAvailableSlotsResposne.value = RestObservable.success(it) },
                    { getdriverAvailableSlotsResposne.value = RestObservable.error(activity, it) })
        } else {
            Helper.showNoInternetAlert(activity,
                activity.getString(R.string.no_internet_connection),
                object : OnNoInternetConnectionListener {
                    override fun onRetryApi() {
                        getdriverAvailableSlotsApi(activity, isDialogShow)
                    }
                })
        }
    }

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /*Add Driver availablity slots*/

    private var addDriverAvailablitySlotsResponse: MutableLiveData<RestObservable> =
        MutableLiveData()

    fun addDriverAvailablitySlotsResponse(): LiveData<RestObservable> {
        return addDriverAvailablitySlotsResponse
    }

    @SuppressLint("CheckResult")
    fun addDriverAvailablitySlotsApi(
        activity: Activity, timeSlots: String, isDialogShow: Boolean
    ) {
        if (Helper.isNetworkConnected(activity)) {
            apiService.ADD_DRIVER_SLOTS(timeSlots)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    addDriverAvailablitySlotsResponse.value =
                        RestObservable.loading(activity, isDialogShow)
                }
                .subscribe({ addDriverAvailablitySlotsResponse.value = RestObservable.success(it) },
                    {
                        addDriverAvailablitySlotsResponse.value = RestObservable.error(activity, it)
                    })
        } else {
            Helper.showNoInternetAlert(activity,
                activity.getString(R.string.no_internet_connection),
                object : OnNoInternetConnectionListener {
                    override fun onRetryApi() {
                        addDriverAvailablitySlotsApi(activity, timeSlots, isDialogShow)
                    }
                })
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //Get driver added available slots

    private var getDriverAddedAvailableSlotsResposne: MutableLiveData<RestObservable> =
        MutableLiveData()

    fun getDriverAddedAvailableSlotsResposne(): LiveData<RestObservable> {
        return getDriverAddedAvailableSlotsResposne
    }

    @SuppressLint("CheckResult")
    fun getDriverAddedAvailableSlotsApi(
        activity: Activity,
        isDialogShow: Boolean
    ) {
        if (Helper.isNetworkConnected(activity)) {
            apiService.GET_DRIVER_ADDED_SLOTS()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    getDriverAddedAvailableSlotsResposne.value =
                        RestObservable.loading(activity, isDialogShow)
                }
                .subscribe(
                    { getDriverAddedAvailableSlotsResposne.value = RestObservable.success(it) },
                    {
                        getDriverAddedAvailableSlotsResposne.value =
                            RestObservable.error(activity, it)
                    })
        } else {
            Helper.showNoInternetAlert(activity,
                activity.getString(R.string.no_internet_connection),
                object : OnNoInternetConnectionListener {
                    override fun onRetryApi() {
                        getDriverAddedAvailableSlotsApi(activity, isDialogShow)
                    }
                })
        }
    }

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //Get driver take status api call
    private var getDriverTakeStatusResponse: MutableLiveData<RestObservable> = MutableLiveData()
    fun getDriverTakeStatusResponse(): LiveData<RestObservable> {
        return getDriverTakeStatusResponse
    }

    @SuppressLint("CheckResult")
    fun getDriverTakeStatusApi(
        activity: Activity,
        isDialogShow: Boolean
    ) {
        if (Helper.isNetworkConnected(activity)) {
            apiService.GET_DRIVER_TAKE_ORDER_STATUS()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    getDriverTakeStatusResponse.value =
                        RestObservable.loading(activity, isDialogShow)
                }
                .subscribe(
                    { getDriverTakeStatusResponse.value = RestObservable.success(it) },
                    {
                        getDriverTakeStatusResponse.value = RestObservable.error(activity, it)
                    }
                )
        } else {

            Helper.showNoInternetAlert(activity,
                activity.getString(R.string.no_internet_connection),
                object : OnNoInternetConnectionListener {
                    override fun onRetryApi() {
                        getDriverTakeStatusApi(
                            activity,
                            isDialogShow
                        )
                    }
                })
        }
    }

    /****************************************************************************************************************************************************/
    //add bank details
    private var addBankDetailsResposne: MutableLiveData<RestObservable> = MutableLiveData()
    fun getAddBankDetailsResposne(): LiveData<RestObservable> {
        return addBankDetailsResposne
    }

    @SuppressLint("CheckResult")
    fun addBankDetailsApi(
        activity: Activity,
        beneficiaryName: String,
        bankName: String,
        bankBranch: String,
        accountNumber: String,
        accountType: String,
        id: String,
        isDialogShow: Boolean
    ) {
        if (Helper.isNetworkConnected(activity)) {
            apiService.ADD_UPDATE_BANK(
                beneficiaryName,
                accountNumber,
                bankName,
                bankBranch,
                accountType,
                id
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    addBankDetailsResposne.value = RestObservable.loading(activity, isDialogShow)
                }
                .subscribe(
                    { addBankDetailsResposne.value = RestObservable.success(it) },
                    { addBankDetailsResposne.value = RestObservable.error(activity, it) })
        } else {
            Helper.showNoInternetAlert(activity,
                activity.getString(R.string.no_internet_connection),
                object : OnNoInternetConnectionListener {
                    override fun onRetryApi() {
                        addBankDetailsApi(
                            activity,
                            beneficiaryName,
                            bankName,
                            bankBranch,
                            accountNumber,
                            accountType,
                            id,
                            isDialogShow
                        )
                    }
                })
        }
    }

    /*-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------*/
    //get bank details
    private var getBankDetailsResposne: MutableLiveData<RestObservable> = MutableLiveData()
    fun getBankDetailsResposne(): LiveData<RestObservable> {
        return getBankDetailsResposne
    }

    @SuppressLint("CheckResult")
    fun getBankDetailsApi(activity: Activity, isDialogShow: Boolean) {
        if (Helper.isNetworkConnected(activity)) {
            apiService.GET_BANK_DETAILS()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    getBankDetailsResposne.value = RestObservable.loading(activity, isDialogShow)
                }
                .subscribe(
                    { getBankDetailsResposne.value = RestObservable.success(it) },
                    { getBankDetailsResposne.value = RestObservable.error(activity, it) })
        } else {
            Helper.showNoInternetAlert(activity,
                activity.getString(R.string.no_internet_connection),
                object : OnNoInternetConnectionListener {
                    override fun onRetryApi() {
                        getBankDetailsApi(activity, isDialogShow)
                    }
                })
        }
    }


    /*-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------*/
    //  Get training Video links api call
    private var getTrainingVideoLinksResponse: MutableLiveData<RestObservable> = MutableLiveData()
    fun getTrainingVideoLinksResponse(): LiveData<RestObservable> {
        return getTrainingVideoLinksResponse
    }

    @SuppressLint("CheckResult")
    fun getTrainingVideoLinksApi(activity: Activity, isDialogShow: Boolean) {
        if (Helper.isNetworkConnected(activity)) {
            apiService.GET_TRAINING_LINKS()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    getTrainingVideoLinksResponse.value =
                        RestObservable.loading(activity, isDialogShow)
                }
                .subscribe(
                    { getTrainingVideoLinksResponse.value = RestObservable.success(it) },
                    { getTrainingVideoLinksResponse.value = RestObservable.error(activity, it) })
        } else {
            Helper.showNoInternetAlert(activity,
                activity.getString(R.string.no_internet_connection),
                object : OnNoInternetConnectionListener {
                    override fun onRetryApi() {
                        getTrainingVideoLinksApi(activity, isDialogShow)
                    }
                })
        }
    }
}


