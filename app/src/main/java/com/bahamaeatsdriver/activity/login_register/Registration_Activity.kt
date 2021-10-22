package com.bahamaeatsdriver.activity.login_register

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bahamaeats.constant.Constants
import com.bahamaeats.network.RestObservable
import com.bahamaeats.network.Status
import com.bahamaeatsdriver.adapter.CityAdapter
import com.bahamaeatsdriver.R
import com.bahamaeatsdriver.activity.Navigation.TermAnd_Conditions
import com.bahamaeatsdriver.di.App
import com.bahamaeatsdriver.helper.extensions.getTokenPrefrence
import com.bahamaeatsdriver.helper.extensions.launchActivity
import com.bahamaeatsdriver.helper.others.Helper
import com.bahamaeatsdriver.helper.others.ImagePicker
import com.bahamaeatsdriver.helper.others.Validator
import com.bahamaeatsdriver.listeners.OnCitySelection
import com.bahamaeatsdriver.model_class.get_city.Body
import com.bahamaeatsdriver.model_class.get_city.GetCityResponse
import com.bahamaeatsdriver.model_class.signup.SignUpResponse
import com.bahamaeatsdriver.repository.BaseViewModel
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_registration.*
import kotlinx.android.synthetic.main.custom_city_dialog.view.*
import javax.inject.Inject

class Registration_Activity : ImagePicker(), View.OnClickListener, OnCitySelection,
    Observer<RestObservable> {
    private var imagePath = ""
    private var isTermsChecked = false
    private var isOpenDot = false
    private var popupWindow: PopupWindow? = null

    @Inject
    lateinit var validator: Validator
    private val viewModel: BaseViewModel by lazy { ViewModelProvider(this).get(BaseViewModel::class.java) }


    override fun uploadSlotsCodeFuncation() {
        //Not in use
    }
    override fun selectedImage(imagePath: String?, thumbnailVideoPath: String) {
        this.imagePath = imagePath!!
        Glide.with(this).load(imagePath).into(iv_profileimage)
    }

    override fun getUpdatedPhoneNoAfterVerify(contactNumber: String, updatedCountryCode: String) {
        //Not in use
    }

    private var countryCode = ""
    private var phone = ""

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        App.getinstance().getmydicomponent().inject(this)
        if (intent.getStringExtra("countryCode") != null && intent.getStringExtra("phone") != null) {
            countryCode = intent.getStringExtra("countryCode")!!
            phone = intent.getStringExtra("phone")!!
            et_phone.text = countryCode + phone
        }
        et_country.text = getString(R.string.countryName)
        rl_checkBox.setOnClickListener(this)
        btn_creataccount.setOnClickListener(this)
        iv_profileimage.setOnClickListener(this)
        tv_cityPicker.setOnClickListener(this)
        iv_backArrow.setOnClickListener(this)
    }

    fun tv_termcondition(view: View?) {
        launchActivity<TermAnd_Conditions>()
    }

    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.btn_creataccount -> {
                val fullname = et_username.text.toString().trim()
                val phone = et_phone.text.toString().trim()
                val email = et_email.text.toString().trim()
                val country = et_country.text.toString().trim()
                val city = tv_cityPicker.text.toString().trim()
                val password = et_password.text.toString().trim()
                val confirmPassword = et_confirmPassword.text.toString().trim()

                if (validator.signUpValid(
                        this,
                        fullname,
                        email,
                        password,
                        confirmPassword,
                        city,
                        country,
                        phone,
                        imagePath,
                        isTermsChecked
                    )
                ) {
                    viewModel.signUpApi(
                        this,
                        fullname,
                        email,
                        password,
                        phone,
                        city,
                        country,
                        Constants.SIMPLE_LOGIN,
                        Constants.ANDROID_DEVICE,
                        getTokenPrefrence(Constants.DEVICE_TOKEN, ""),
                        imagePath,
                        true
                    )
                    viewModel.getSignUpResponse().observe(this, this)
                }
            }
            R.id.iv_profileimage -> {
                checkPermissionCamera(false, "1", "")
            }
            R.id.iv_backArrow -> {
                finish()
            }

            R.id.tv_cityPicker -> {
                /**
                 * 323:CityCode For Bahamas only which is by default
                 */
                viewModel.getCityApiCall(this, "323", true)
                viewModel.getCityApiResponse().observe(this, this)
            }

            R.id.rl_checkBox -> {
                isTermsChecked = if (!isTermsChecked) {
                    iv_tick!!.visibility = View.VISIBLE
                    true
                } else {
                    iv_tick!!.visibility = View.GONE
                    false
                }
            }
        }
    }

    override fun onChanged(liveData: RestObservable?) {
        when (liveData!!.status) {
            Status.SUCCESS -> {
                if (liveData.data is SignUpResponse) {
                    if (liveData.data.body.isEmailAlreadyExist == 1) {
                        Helper.showSuccessToast(this, liveData.data.message)
                    } else {
                        if (liveData.data.body.isPhoneExist == 0) {
                            Helper.showSuccessToast(this, "Invalid phone number")
                        } else {
                            Helper.showSuccessToast(this, liveData.data.message)
                            launchActivity<Identification_Activity>()
                            {
                                putExtra("Typeof", "Normal")
                                putExtra("token", liveData.data.body.user.token)
                            }
                        }
                    }
                }

                if (liveData.data is GetCityResponse) {
                    if (!isOpenDot) {
                        openFiltertBy(tv_cityPicker, liveData.data.body)
                        isOpenDot = true
                    } else {
                        if (popupWindow != null)
                            popupWindow!!.dismiss()
                        isOpenDot = false
                    }
                }
            }

            Status.ERROR -> {

            }
            else -> {

            }
        }
    }

    override fun onCitySelect(cityName: String, position: Int) {
        tv_cityPicker.text = cityName
        isOpenDot = false
        popupWindow!!.dismiss()
    }

    private fun openFiltertBy(ivSort: TextView?, body: List<Body>) {
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val customView = inflater.inflate(R.layout.custom_city_dialog, null)
        popupWindow = PopupWindow(
            customView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        popupWindow!!.isOutsideTouchable = true
        val adapter = CityAdapter(this, body, this)
        customView!!.rv_cities.adapter = adapter
        popupWindow!!.showAsDropDown(ivSort)
    }

}