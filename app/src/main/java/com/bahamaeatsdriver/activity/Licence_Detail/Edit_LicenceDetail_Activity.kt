package com.bahamaeatsdriver.activity.Licence_Detail

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.Dialog
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.PopupWindow
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bahamaeats.constant.Constants
import com.bahamaeats.network.RestObservable
import com.bahamaeats.network.Status
import com.bahamaeatsdriver.Adapter.LicenseTypeAdapter
import com.bahamaeatsdriver.R
import com.bahamaeatsdriver.di.App
import com.bahamaeatsdriver.helper.others.Helper
import com.bahamaeatsdriver.helper.others.ImagePicker
import com.bahamaeatsdriver.helper.others.Validator
import com.bahamaeatsdriver.listeners.OnLicenseTypeSelection
import com.bahamaeatsdriver.model_class.get_driver_license.Body
import com.bahamaeatsdriver.model_class.get_driver_license.GetDriverLicense
import com.bahamaeatsdriver.model_class.license_type.LicenseTypeModel
import com.bahamaeatsdriver.model_class.update_license_details.UpdateLicenseDetails
import com.bahamaeatsdriver.repository.BaseViewModel
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_edit__licence_detail_.*
import kotlinx.android.synthetic.main.select_licence_type_dialog.view.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class Edit_LicenceDetail_Activity : ImagePicker(), View.OnClickListener, Observer<RestObservable>, OnLicenseTypeSelection {
    private var Type_image: String? = null
    private val viewModel: BaseViewModel by lazy { ViewModelProvider(this).get(BaseViewModel::class.java) }
    private var image_path = ""
    private var backImage = ""
    private var frontImage = ""
    private var licenseDetails: Body? = null

    @Inject
    lateinit var validator: Validator
    private var isOpenDot = false

    private var popupWindow: PopupWindow? = null
    private var licenseTypeList = ArrayList<LicenseTypeModel>()

    override fun selectedImage(imagePath: String?, thumbnailVideoPath: String) {
        image_path = imagePath!!
        if (Type_image == "Front") {
            Glide.with(this).load(imagePath).placeholder(R.drawable.app_logo_black).into(iv_front!!)
            frontImage = image_path
        } else {
            Glide.with(this).load(imagePath).placeholder(R.drawable.app_logo_black).into(iv_back!!)
            backImage = image_path
        }
    }

    override fun getUpdatedPhoneNoAfterVerify(contactNumber: String, updatedCountryCode: String) {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit__licence_detail_)
        App.getinstance().getmydicomponent().inject(this)
        licenseTypeList.add(LicenseTypeModel("1", "Automobile"))
        licenseTypeList.add(LicenseTypeModel("2", "Scooter"))
//        Ed_l_natiolaty.text = getString(R.string.countryName)

        tv_licence_issue = findViewById(R.id.Tv_licence_issue)
        tv_Licence_expire = findViewById(R.id.Tv_Licence_expire)
        tv_licence_birth = findViewById(R.id.tv_licenceBirth)

        if (intent.getStringExtra("from") != null && intent.getStringExtra("from")!!.equals("1")) {
            if (intent.getSerializableExtra("licenseDetails") != null) {
                licenseDetails = intent.getSerializableExtra("licenseDetails") as Body?
                ed_L_number.setText(licenseDetails!!.licenseNo)
                tv_licence_issue.setText(licenseDetails!!.issueDate)
                tv_Licence_expire.setText(licenseDetails!!.expiryDate)
                Ed_l_natiolaty.setText(licenseDetails!!.nationality)
                Ed_l_Licencetype.setText(licenseDetails!!.licenseType)
                tv_licence_birth.text = licenseDetails!!.dob
                Glide.with(this).load(licenseDetails!!.frontPhoto).placeholder(R.drawable.placeholder_rectangle).into(iv_front)
                Glide.with(this).load(licenseDetails!!.backPhoto).placeholder(R.drawable.placeholder_rectangle).into(iv_back)
            }
        } else if (intent.getStringExtra("from") != null && intent.getStringExtra("from")!!.equals("2")) {
            viewModel.getDriverLicenseDetailsApi(this, true)
            viewModel.getDriverLicenseDetailsResposne().observe(this, this)

        }
        iv_Linc_edit!!.visibility = View.GONE
        LL_cancle_update_button!!.visibility = View.VISIBLE
        iv_back_image!!.visibility = View.VISIBLE
        iv_front_image!!.visibility = View.VISIBLE

        ed_L_number.isEnabled = true
        tv_licence_birth.isEnabled = true
        tv_licence_issue.isEnabled = true
        tv_Licence_expire.isEnabled = true
        Ed_l_natiolaty.isEnabled = true
        Ed_l_Licencetype.isEnabled = true

        iv_Linc_edit.setOnClickListener(this)
        iv_backArrow_licendedetail!!.setOnClickListener(this)
        tv_licence_issue.setOnClickListener(this)
        tv_licence_birth.setOnClickListener(this)
        tv_Licence_expire.setOnClickListener(this)
        Ed_l_Licencetype.setOnClickListener(this)
        btn_update.setOnClickListener(this)
        Tv_L_hedder.setText("EDIT License Details")
        if (Tv_licence_issue.text.toString().trim().isEmpty()) {
            isIssueDateSelected = false
        } else {
            isIssueDateSelected = true
        }

    }


    fun Button_L_cancle(view: View?) {
        onBackPressed()
        finish()
    }

    fun iv_front_image(view: View?) {
        Type_image = "Front"
//        image()
        checkPermissionCamera(false, "", "")

    }

    fun iv_back_image(view: View?) {
        Type_image = "Back"
//        image()
        checkPermissionCamera(false, "", "")

    }

    @SuppressLint("ValidFragment")
    class DatePickerFragment_static : DialogFragment(), OnDateSetListener {
        lateinit var dpd: DatePickerDialog

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            val calendar = Calendar.getInstance()
            val year = calendar[Calendar.YEAR]
            val month = calendar[Calendar.MONTH]
            val day = calendar[Calendar.DAY_OF_MONTH]
            if (datetype == "birthdate") {
                //below 18 yers   dpd.getDatePicker().setMaxDate(System.currentTimeMillis() - 568025136000L);
//                dpd.datePicker.maxDate = System.currentTimeMillis()
                dpd = DatePickerDialog(activity!!, this, year, month, day)
                calendar[year - 18, month] = day
                val value = calendar.timeInMillis
                dpd.datePicker.maxDate = value
            } else if (datetype == "Issuedate") {
                dpd = DatePickerDialog(activity!!, this, year, month, day)
                calendar[year, month] = day
                val value = calendar.timeInMillis
                dpd.datePicker.maxDate = value
            } else if (datetype == "expiredate") {
                if (year_ == 0) {
                    dpd = DatePickerDialog(activity!!, this, year, month, day)
                    calendar[year, month] = day
                } else {
                    dpd = DatePickerDialog(activity!!, this, year_, monthOfYear_, dayOfMonth_)
                    calendar[year_, monthOfYear_] = dayOfMonth_
                }
                val value = calendar.timeInMillis
                dpd.datePicker.maxDate = value
            }
            return dpd
        }

        override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int) {
            val cal = Calendar.getInstance()
            cal.timeInMillis = 0
            cal[year, monthOfYear, dayOfMonth, 0, 0] = 0
            val Date = cal.time
            Log.e("date", "asa  " + cal.time)
            if (datetype == "birthdate") {
                tv_licence_birth!!.text = SimpleDateFormat(Constants.LICENSE_DATE_FORMAT).format(Date)
            } else if (datetype == "Issuedate") {
                isIssueDateSelected = true
                year_ = year + 1
                monthOfYear_ = monthOfYear
                dayOfMonth_ = dayOfMonth
                tv_licence_issue.text = SimpleDateFormat(Constants.LICENSE_DATE_FORMAT).format(Date)
            } else if (datetype == "expiredate") {
                tv_Licence_expire!!.text = SimpleDateFormat(Constants.LICENSE_DATE_FORMAT).format(Date)
            }
        }
    }

    companion object {
        var datetype: String? = null
        lateinit var tv_licence_issue: TextView
        lateinit var tv_Licence_expire: TextView
        lateinit var tv_licence_birth: TextView
        var isIssueDateSelected: Boolean = false
        var year_: Int = 0
        var monthOfYear_: Int = 0
        var dayOfMonth_: Int = 0
    }

    override fun onClick(p0: View?) {
        val Itemid = p0!!.id
        when (Itemid) {
            R.id.Tv_licence_issue -> {
                datetype = "Issuedate"
                val fragment: DialogFragment = DatePickerFragment_static()
                fragment.show(supportFragmentManager, "Date Picker")
            }
            R.id.tv_licenceBirth -> {
                datetype = "birthdate"
                val fragment: DialogFragment = DatePickerFragment_static()
                fragment.show(supportFragmentManager, "Date Picker")

            }
            R.id.iv_backArrow_licendedetail -> {
                finish()
            }

            R.id.Ed_l_Licencetype -> {
                if (!isOpenDot) {
                    openFiltertBy(Ed_l_Licencetype)
                    isOpenDot = true
                } else {
                    if (popupWindow != null)
                        popupWindow!!.dismiss()
                    isOpenDot = false
                }
            }
            R.id.btn_update -> {
                val licenseNumber = ed_L_number.text.toString().trim()
                val dob = tv_licence_birth.text.toString().trim()
                val isuueOn = Tv_licence_issue.text.toString().trim()
                val expiryDate = Tv_Licence_expire.text.toString().trim()
                val naitionality = Ed_l_natiolaty.text.toString().trim()
                val licenseType = Ed_l_Licencetype.text.toString().trim()
                if (validator.editLicenseValid(this, licenseType, licenseNumber, dob, isuueOn, expiryDate, naitionality)) {
                    viewModel.editLicenseApi(this, licenseDetails!!.id.toString(), licenseType, licenseNumber, dob, isuueOn, expiryDate, naitionality, frontImage!!, backImage!!, true)
                    viewModel.getEditLicenseResponse().observe(this, this)
                }
            }
            R.id.Tv_Licence_expire -> {
                datetype = "expiredate"
                if (isIssueDateSelected) {
                    val fragment: DialogFragment = DatePickerFragment_static()
                    fragment.show(supportFragmentManager, "Date Picker")
                } else {
                    Helper.showErrorAlert(this, "Select issue date first")
                }

            }

        }
    }

    private fun openFiltertBy(edLLicencetype: TextView?) {
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val customView = inflater.inflate(R.layout.select_licence_type_dialog, null)
        popupWindow = PopupWindow(customView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        popupWindow!!.setOutsideTouchable(true)
        val adapter = LicenseTypeAdapter(this, licenseTypeList, this)
        customView!!.rv_licenseType.setAdapter(adapter)
        popupWindow!!.showAsDropDown(edLLicencetype)
    }

    override fun OnTypeSelection(type: String, position: Int) {
        Ed_l_Licencetype.text = type
        isOpenDot = false
        popupWindow!!.dismiss()
    }

    override fun onChanged(liveData: RestObservable?) {
        when (liveData!!.status) {
            Status.SUCCESS -> {
                if (liveData.data is GetDriverLicense) {
                    licenseDetails=liveData.data.body
                    ed_L_number.setText(licenseDetails!!.licenseNo)
                    tv_licence_issue.setText(licenseDetails!!.issueDate)
                    tv_Licence_expire.setText(licenseDetails!!.expiryDate)
                    Ed_l_natiolaty.setText(licenseDetails!!.nationality)
                    Ed_l_Licencetype.setText(licenseDetails!!.licenseType)
                    tv_licence_birth.text = licenseDetails!!.dob
                    Glide.with(this).load(licenseDetails!!.frontPhoto).placeholder(R.drawable.placeholder_rectangle).into(iv_front)
                    Glide.with(this).load(licenseDetails!!.backPhoto).placeholder(R.drawable.placeholder_rectangle).into(iv_back)
                }
                if (liveData.data is UpdateLicenseDetails) {
                    Helper.showSuccessToast(this, liveData.data.message)
                    finish()
                }
            }


            Status.ERROR -> {

            }
            else -> {

            }
        }
    }


}