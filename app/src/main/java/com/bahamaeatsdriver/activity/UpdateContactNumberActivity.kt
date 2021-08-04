package com.bahamaeatsdriver.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bahamaeats.network.RestObservable
import com.bahamaeats.network.Status
import com.bahamaeatsdriver.R
import com.bahamaeatsdriver.activity.Otp.Otp_Fill_Activity
import com.bahamaeatsdriver.di.App
import com.bahamaeatsdriver.helper.others.Helper
import com.bahamaeatsdriver.helper.others.Validator
import com.bahamaeatsdriver.model_class.update_phone_number.UpdatePhoneNumberResponse
import com.bahamaeatsdriver.repository.BaseViewModel
import kotlinx.android.synthetic.main.activity_update_contact_number.*
import kotlinx.android.synthetic.main.toolbar_layout.*
import javax.inject.Inject

class UpdateContactNumberActivity : AppCompatActivity(), View.OnClickListener, Observer<RestObservable> {
    @Inject
    lateinit var validator: Validator
    private val viewModel: BaseViewModel by lazy { ViewModelProvider(this).get(BaseViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_contact_number)
        App.getinstance().getmydicomponent().inject(this)
        iv_backArrow.setOnClickListener(this)
        btn_next.setOnClickListener(this)
    }

    /***
     * Register number method OnClick and API call
     */
    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.btn_next -> {
                val countryCode = ccPicker.selectedCountryCode
                val phone = et_phone.text.toString().trim()
                if (validator.getOtpValid(this, countryCode, phone)) {
                    viewModel.getupdatePhoneOtpApiCall(this, countryCode, phone, true)
                    viewModel.getupdatePhoneOtpResponse().observe(this, this)
                }
            }
            R.id.iv_backArrow -> {
                finish()
            }

        }
    }

    override fun onChanged(liveData: RestObservable?) {
        when (liveData!!.status) {
            Status.SUCCESS -> {
                if (liveData.data is UpdatePhoneNumberResponse) {
                    if (liveData.data.code == 403) {
                        Helper.showErrorAlert(this, liveData.data.message)
                    } else {
                        Helper.showSuccessToast(this, liveData.data.message)
                        /*  launchActivity<Otp_Fill_Activity>()
                          {
                              putExtra("countryCode", liveData.data.body.countryCode)
                              putExtra("phone", liveData.data.body.contactNo)
                              putExtra("from", "UpdateContactNumberActivity")
                          }*/
                        val initData = Intent(this, Otp_Fill_Activity::class.java)
                        initData.putExtra("countryCode", liveData.data.body.countryCode)
                        initData.putExtra("phone", liveData.data.body.contactNo)
                        initData.putExtra("from", "UpdateContactNumberActivity")
                        initData.putExtra("updatedNumber", et_phone.text.toString().trim())
                        initData.putExtra("updatedCountryCode", ccPicker.selectedCountryCode)
                        initData.setFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT)
                        startActivity(initData)
                        finish()
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