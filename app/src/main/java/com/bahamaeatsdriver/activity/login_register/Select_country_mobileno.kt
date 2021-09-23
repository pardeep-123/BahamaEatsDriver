package com.bahamaeatsdriver.activity.login_register

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bahamaeats.network.RestObservable
import com.bahamaeats.network.Status
import com.bahamaeatsdriver.R
import com.bahamaeatsdriver.activity.verify_otp.VerifyOtpActivity
import com.bahamaeatsdriver.di.App
import com.bahamaeatsdriver.helper.extensions.launchActivity
import com.bahamaeatsdriver.helper.others.Helper
import com.bahamaeatsdriver.helper.others.Validator
import com.bahamaeatsdriver.model_class.get_otp.GetOtpResponse
import com.bahamaeatsdriver.repository.BaseViewModel
import com.ybs.countrypicker.CountryPicker
import kotlinx.android.synthetic.main.activity_select_country_mobileno.*
import javax.inject.Inject


class Select_country_mobileno : AppCompatActivity(), View.OnClickListener, Observer<RestObservable> {
    @Inject
    lateinit var validator: Validator
    private val viewModel: BaseViewModel by lazy { ViewModelProvider(this).get(BaseViewModel::class.java) }
    private var countrCode = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_country_mobileno)
        App.getinstance().getmydicomponent().inject(this)
        btn_next.setOnClickListener(this)
        tv_login.setOnClickListener(this)
        ccPicker.setOnClickListener(this)
    }


    /***
     * Register number method OnClick and API call
     */
    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.btn_next -> {
//                launchActivity<Otp_Fill_Activity>()
                val countryCode = ccPicker.text.toString().trim()
                val phone = et_phone.text.toString().trim()
                if (validator.getOtpValid(this, countrCode, phone)) {
                    viewModel.getOtpApiCall(this, countrCode, phone, true)
                    viewModel.getOtpApiResponse().observe(this, this)
                }
            }
            R.id.tv_login -> {
                finish()
            }
            R.id.ccPicker -> {
                val picker = CountryPicker.newInstance("Select Country") // dialog title
                picker.setListener { name, _, dialCode, flagDrawableResID ->
                    if (dialCode.contains("+")) {
                        val cccode = dialCode.replace("+", "")
                        countrCode = cccode
                        ccPicker.text = "$name ($dialCode)"
                        picker.dismiss()
                    } else {
                        ccPicker.text = "$name ($dialCode)"
                        countrCode = dialCode
                    }

                }
                picker.show(supportFragmentManager, "COUNTRY_PICKER")


                /*val picker = CountryPicker.newInstance("Select Country")  // dialog title
                picker.setListener { name, , , _ ->
                    textCountry.text = name
                    picker.dismiss()
                }
                picker.show(supportFragmentManager, "COUNTRY_PICKER")*/
            }
        }
    }

    override fun onChanged(liveData: RestObservable?) {
        when (liveData!!.status) {
            Status.SUCCESS -> {
                if (liveData.data is GetOtpResponse) {
                    if (liveData.data.code == 403) {
                        Helper.showErrorAlert(this, liveData.data.message)

                    } else {
                        Helper.showSuccessToast(this, liveData.data.message)
//                        Helper.showSuccessToast(this, "OTP sent successfully")
                        launchActivity<VerifyOtpActivity>()
                        {
                            putExtra("countryCode", liveData.data.body.countryCode)
                            putExtra("phone", liveData.data.body.contactNo)
                        }
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