package com.bahamaeatsdriver.activity.login_register

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bahamaeats.constant.Constants
import com.bahamaeats.network.RestObservable
import com.bahamaeats.network.Status
import com.bahamaeatsdriver.R
import com.bahamaeatsdriver.activity.Forgot_Password_Activity
import com.bahamaeatsdriver.activity.Home_Page
import com.bahamaeatsdriver.di.App
import com.bahamaeatsdriver.helper.extensions.launchActivity
import com.bahamaeatsdriver.helper.extensions.savePrefObject
import com.bahamaeatsdriver.helper.extensions.savePrefrence
import com.bahamaeatsdriver.helper.others.Helper
import com.bahamaeatsdriver.helper.others.Validator
import com.bahamaeatsdriver.model_class.driver_details.DriverDetails
import com.bahamaeatsdriver.model_class.login.LoginResponse
import com.bahamaeatsdriver.repository.BaseViewModel
import kotlinx.android.synthetic.main.activity_login.*
import javax.inject.Inject

class Login_Activity : AppCompatActivity(), View.OnClickListener, Observer<RestObservable> {

    @Inject
    lateinit var validator: Validator

    private val viewModel: BaseViewModel by lazy { ViewModelProvider(this).get(BaseViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        App.getinstance().getmydicomponent().inject(this)

        Button_login.setOnClickListener(this)
        Tv_signup.setOnClickListener(this)
        tv_forgotpass.setOnClickListener(this)
    }

    override fun onBackPressed() {
        finishAffinity()
    }

    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.Tv_signup -> {
                launchActivity<Select_country_mobileno>()
            }
            R.id.Button_login -> {
                val email = et_username.text.toString().trim()
                val password = et_password.text.toString().trim()
                if (validator.loginValid(this, email, password)) {
                    viewModel.loginApi(this, email, password, true)
                    viewModel.getLoginResponse().observe(this, this)
                }
            }
            R.id.tv_forgotpass -> {
                launchActivity<Forgot_Password_Activity>()
            }
        }
    }

    override fun onChanged(liveData: RestObservable?) {
        when (liveData!!.status) {
            Status.SUCCESS -> {
                if (liveData.data is LoginResponse) {
                    if (liveData.data.code == 403) {
                        Helper.showErrorAlert(this, liveData.data.message)
                    } else {
//                        if (liveData.data.body.isApproved == 0) {
//                            Helper.showErrorAlert(this, "Your account is not approved yet by admin.")
//                        } else {
                            if (liveData.data.body.isLicenceExists == 0) {
                                launchActivity<Identification_Activity>()
                                {
                                    putExtra("Typeof", "Normal")
                                    putExtra("token", liveData.data.body.token)
                                }
                            } else {
                                Helper.showSuccessToast(this, liveData.data.message)
                                launchActivity<Home_Page>()
                                savePrefrence(Constants.TOKEN, liveData.data.body.token)
                                savePrefObject(Constants.DRIVER_DETAILS, DriverDetails(liveData.data.body.email,
                                    liveData.data.body.firstName,liveData.data.body.id,liveData.data.body.image,liveData.data.body.dob,liveData.data.body.gender,liveData.data.body.driver_referrals_amount,
                                    liveData.data.body.referrals_code,liveData.data.body.isApproved,liveData.data.body.isDocumentVerified,
                                    liveData.data.body.isLicenceExists,liveData.data.body.lastName,liveData.data.body.fullName,liveData.data.body.token,liveData.data.body.username,liveData.data.body.isNotification,liveData.data.body.countryCodePhone,liveData.data.body.city,liveData.data.body.countryCode,liveData.data.body.contactNo))
                                finishAffinity()
                            }
//                        }
                    }
                }
            }
            Status.ERROR -> {

            }
            else -> {

            }
        }
    }

}