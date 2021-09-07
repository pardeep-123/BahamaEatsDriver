package com.bahamaeatsdriver.activity.Otp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bahamaeats.network.RestObservable
import com.bahamaeats.network.Status
import com.bahamaeatsdriver.R
import com.bahamaeatsdriver.activity.login_register.Registration_Activity
import com.bahamaeatsdriver.di.App
import com.bahamaeatsdriver.helper.extensions.launchActivity
import com.bahamaeatsdriver.helper.others.Helper
import com.bahamaeatsdriver.helper.others.ImagePicker.Companion.pickPhoneNumberResultCode
import com.bahamaeatsdriver.helper.others.Validator
import com.bahamaeatsdriver.model_class.resend_otp.ResendOtpResponse
import com.bahamaeatsdriver.model_class.updated_phone_verify_otp.UpdatePhoneVerifyOtpResponse
import com.bahamaeatsdriver.model_class.verify_otp.VerifyOtpResponse
import com.bahamaeatsdriver.repository.BaseViewModel
import kotlinx.android.synthetic.main.activity_otp_.*
import kotlinx.android.synthetic.main.toolbar_layout.*
import javax.inject.Inject


class Otp_Fill_Activity : AppCompatActivity(), TextWatcher, View.OnClickListener,
    Observer<RestObservable> {

    @Inject
    lateinit var validator: Validator
    private val viewModel: BaseViewModel by lazy { ViewModelProvider(this).get(BaseViewModel::class.java) }
    private var countryCode = ""
    private var phone = ""
    private var from = ""
    private var updatedNumber = ""
    private var updatedCountryCode = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp_)
        App.getinstance().getmydicomponent().inject(this)
        if (intent.getStringExtra("countryCode") != null && intent.getStringExtra("phone") != null) {
            countryCode = intent.getStringExtra("countryCode")!!
            phone = intent.getStringExtra("phone")!!
        }
        if (intent.getStringExtra("from") != null) {
            from = intent.getStringExtra("from")!!
            updatedNumber = intent.getStringExtra("updatedNumber")!!
            updatedCountryCode = intent.getStringExtra("updatedCountryCode")!!
        }
        editTextone.addTextChangedListener(this)
        editTexttwo.addTextChangedListener(this)
        editTextthree.addTextChangedListener(this)
        editTextfour.addTextChangedListener(this)
        iv_backArrow.setOnClickListener(this)
        btn_ValidateOtp.setOnClickListener(this)
        tv_resend_otp.setOnClickListener(this)
        startTimer()
    }

    private fun startTimer() {
        val countDownTimer = object : CountDownTimer(60000, 1000) {
            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                ll_remaining_time.visibility = View.VISIBLE
                tv_Remaining_time.visibility = View.VISIBLE
                tv_validateCodeLabel.visibility = View.VISIBLE
                Log.d("Otp_Fill_Activity", "millisUntilFinished $millisUntilFinished")
                tv_Remaining_time.text = "" + millisUntilFinished / 1000
                tv_resend_otp.isClickable = false
                tv_resend_otp.isClickable = false
                tv_resend_otp.setTextColor(
                    ContextCompat.getColor(
                        this@Otp_Fill_Activity,
                        R.color.colorTextView
                    )
                )
            }

            override fun onFinish() {
                tv_resend_otp.isClickable = true
                tv_resend_otp.setTextColor(
                    ContextCompat.getColor(
                        this@Otp_Fill_Activity,
                        R.color.White
                    )
                )
                ll_remaining_time.visibility = View.INVISIBLE
                tv_Remaining_time.visibility = View.INVISIBLE
                tv_validateCodeLabel.visibility = View.INVISIBLE
            }
        }
        countDownTimer.start()
    }

    override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

    }

    override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

    }

    @SuppressLint("SetTextI18n")
    override fun afterTextChanged(editable: Editable) {
        if (editable.length == 1) {
            btn_ValidateOtp!!.text = "DONE"
//            tv_resend_otp!!.visibility = View.VISIBLE
            //  tv_Remaining_time.setVisibility(View.GONE);
            if (editTextone.length() == 1) {
                editTexttwo.requestFocus()
            }
            if (editTexttwo.length() == 1) {
                editTextthree.requestFocus()
            }
            if (editTextthree.length() == 1) {
                editTextfour.requestFocus()
            }
        } else if (editable.isEmpty()) {
            btn_ValidateOtp!!.text = "DONE"
            if (editTextfour.length() == 0) {
                editTextthree.requestFocus()
            }
            if (editTextthree.length() == 0) {
                editTexttwo.requestFocus()
            }
            if (editTexttwo.length() == 0) {
                editTextone.requestFocus()
            }
        }
    }

    /***
     * OnLick to go back
     */
    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.iv_backArrow -> {
                finish()
            }
            R.id.tv_resend_otp -> {
                viewModel.resendOtpApiCall(this, countryCode, phone, true)
                viewModel.getresendApiResponse().observe(this, this)
            }
            R.id.btn_ValidateOtp -> {
                val otp: String =
                    editTextone.text.toString() + editTexttwo.text.toString() + editTextthree.text.toString() + editTextfour.text.toString()
                if (!validator.isNotNull(otp)) {
                    Helper.showErrorAlert(this, "Enter OTP first")
                } else if (otp.length < 4) {
                    Helper.showErrorAlert(this, "Enter 4 digit OTP")
                } else {
                    if (from.isNotEmpty() && from == "UpdateContactNumberActivity") {
                        viewModel.updatedPhoneVerifyOtpResponsneApi(
                            this@Otp_Fill_Activity,
                            countryCode,
                            phone,
                            otp,
                            true
                        )
                        viewModel.getUpdatedPhoneVerifyOtp().observe(this, this)
                    } else {
                        viewModel.verifyOtprApi(
                            this@Otp_Fill_Activity,
                            countryCode,
                            phone,
                            otp,
                            true
                        )
                        viewModel.getverifyOtp().observe(this, this)
                    }
                }
            }
        }
    }

    /**
     * When get success and response call 200 OK "onChanged" will call
     */
    @SuppressLint("SetTextI18n")
    override fun onChanged(liveData: RestObservable?) {
        when (liveData!!.status) {
            Status.SUCCESS -> {
                if (liveData.data is VerifyOtpResponse) {
                    /***
                     * Go To Register page add required fields
                     */
                    launchActivity<Registration_Activity>()
                    {
                        putExtra("countryCode", liveData.data.body.countryCode)
                        putExtra("phone", liveData.data.body.contactNo)
                    }
                    Helper.showSuccessToast(this, liveData.data.message)
                }
                if (liveData.data is UpdatePhoneVerifyOtpResponse) {

                    Helper.showSuccessToast(this, liveData.data.message)
                    //set the result code and close the activity
                    val result = Intent()
                    result.putExtra("phone", updatedNumber)
                    result.putExtra("updatedCountryCode", updatedCountryCode)
                    setResult(pickPhoneNumberResultCode, result)
                    finish()
                }

                if (liveData.data is ResendOtpResponse) {
                    /***
                     * Reset  the input fields and get the OTP again
                     */
                    editTextone.setText("")
                    editTexttwo.setText("")
                    editTextthree.setText("")
                    editTextfour.setText("")
                    btn_ValidateOtp!!.text = "VALIDATE"
                    Helper.showSuccessToast(this, liveData.data.message)
                    startTimer()
                }
            }

            Status.ERROR -> {

            }
            else -> {

            }
        }
    }
}