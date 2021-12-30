package com.bahamaeatsdriver.activity.driver_licence_detail

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.PopupWindow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bahamaeats.constant.Constants
import com.bahamaeats.network.RestObservable
import com.bahamaeats.network.Status
import com.bahamaeatsdriver.adapter.LicenseTypeAdapter
import com.bahamaeatsdriver.R
import com.bahamaeatsdriver.activity.Documentation
import com.bahamaeatsdriver.activity.Home_Page
import com.bahamaeatsdriver.activity.login_register.Identification_Activity
import com.bahamaeatsdriver.activity.login_register.Login_Activity
import com.bahamaeatsdriver.di.App
import com.bahamaeatsdriver.helper.extensions.launchActivity
import com.bahamaeatsdriver.helper.extensions.savePrefObject
import com.bahamaeatsdriver.helper.extensions.savePrefrence
import com.bahamaeatsdriver.helper.others.Helper
import com.bahamaeatsdriver.helper.others.Validator
import com.bahamaeatsdriver.listeners.OnLicenseTypeSelection
import com.bahamaeatsdriver.model_class.add_license_details.AddLicenseDetails
import com.bahamaeatsdriver.model_class.license_type.LicenseTypeModel
import com.bahamaeatsdriver.repository.BaseViewModel
import kotlinx.android.synthetic.main.activity_fill_license_detail.*
import kotlinx.android.synthetic.main.select_licence_type_dialog.view.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class Fill_LicenseDetail_Activity : AppCompatActivity(), View.OnClickListener, Observer<RestObservable>, OnLicenseTypeSelection {

    private lateinit var dialog: Dialog
    private var tv_Done: TextView? = null
    private var TypeofActivity: String? = null
    private var token: String? = null
    private var backImage: String? = null
    private var frontImage: String? = null
    private val viewModel: BaseViewModel by lazy { ViewModelProvider(this).get(BaseViewModel::class.java) }
    private var isOpenDot = false
    private var popupWindow: PopupWindow? = null

    @Inject
    lateinit var validator: Validator
    private var licenseTypeList = ArrayList<LicenseTypeModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fill_license_detail)
        App.getinstance().getmydicomponent().inject(this)
        licenseTypeList.add(LicenseTypeModel("1", "Automobile"))
        licenseTypeList.add(LicenseTypeModel("2", "Scooter"))

        tTv_isuueon = findViewById(R.id.tv_isuueon)
        tTv_edpiredate = findViewById(R.id.tv_expiryDate)
        ttv_birthdate = findViewById(R.id.et_dob)

        iv_edit_Licence.setOnClickListener(this)
        et_licenseType.setOnClickListener(this)
        btn_Licenceconfirm.setOnClickListener(this)
        et_naitionality.text = getString(R.string.countryName)

        try {
            TypeofActivity = intent.getStringExtra("type")
            token = intent.getStringExtra("token")
        } catch (e: Exception) {
        }
        /**
         * TypeofActivity=Normal(from registration process)
         * TypeofActivity=From_profile(from )
         */
        if (TypeofActivity.equals("Normal")) {
            if (intent.getStringExtra("frontImage") != null) {
                frontImage = intent.getStringExtra("frontImage")
                backImage = intent.getStringExtra("backImage")
            }
        } else if (TypeofActivity.equals("From_profile")) {
            iv_edit_Licence.visibility = View.VISIBLE
        }
    }

    fun iv_backArrow_licendedetail(view: View?) {
        finish()
    }


    fun Tv_isuueon(view: View?) {
        datetype = "Issuedate"
        val fragment: DialogFragment = DatePickerFragment_static()
        fragment.show(supportFragmentManager, "Date Picker")
    }

    /**
     * poonam
     */
    fun Tv_edpiredate(view: View?) {
        datetype = "expiredate"
        if (isIssueDateSelected) {
            val fragment: DialogFragment = DatePickerFragment_static()
            fragment.show(supportFragmentManager, "Date Picker")
        } else {
            Helper.showErrorAlert(this, "Select issue date first")
        }

    }

    fun tv_birthdate_l(view: View?) {
        datetype = "birthdate"
        val fragment: DialogFragment = DatePickerFragment_static()
        fragment.show(supportFragmentManager, "Date Picker")
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
                //below 18 yers
//                 dpd.getDatePicker().setMaxDate(System.currentTimeMillis() - 568025136000L);
                dpd = DatePickerDialog(requireActivity(), this, year - 18, month, day)
//                calendar.add(Calendar.YEAR, -40)
//                dpd.datePicker.maxDate = System.currentTimeMillis()
                calendar[year - 18, month] = day
                val value = calendar.timeInMillis
                dpd.datePicker.maxDate = value
            } else if (datetype == "Issuedate") {
                dpd = DatePickerDialog(requireActivity(), this, year, month, day)
                calendar[year, month] = day
                val value = calendar.timeInMillis
                dpd.datePicker.maxDate = value
            } else if (datetype == "expiredate") {
                dpd = DatePickerDialog(requireActivity(), this, year_, monthOfYear_, dayOfMonth_)
                calendar[year_, monthOfYear_] = dayOfMonth_
                val value = calendar.timeInMillis
                dpd.datePicker.minDate = value
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
                ttv_birthdate!!.text = SimpleDateFormat(Constants.LICENSE_DATE_FORMAT).format(Date)
            } else if (datetype == "Issuedate") {
                isIssueDateSelected = true
                year_ = year + 1
                monthOfYear_ = monthOfYear
                dayOfMonth_ = dayOfMonth
                tTv_isuueon.text = SimpleDateFormat(Constants.LICENSE_DATE_FORMAT).format(Date)
            } else if (datetype == "expiredate") {
                tTv_edpiredate.text = SimpleDateFormat(Constants.LICENSE_DATE_FORMAT).format(Date)
            }
        }
    }

    companion object {
        var datetype: String? = null
        var isIssueDateSelected: Boolean = false
        lateinit var tTv_isuueon: TextView
        lateinit var tTv_edpiredate: TextView
        var ttv_birthdate: TextView? = null
        var year_: Int = 0
        var monthOfYear_: Int = 0
        var dayOfMonth_: Int = 0
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.btn_Licenceconfirm -> {
                if (TypeofActivity.equals("From_profile")) {
                    finish()
                } else if (TypeofActivity.equals("Licence")) {
                    dialog = Dialog(this@Fill_LicenseDetail_Activity)
                    dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
                    dialog.setContentView(R.layout.res_verification_panding)
                    dialog.setCancelable(true)
                    tv_Done = dialog.findViewById(R.id.tv_Done)
                    tv_Done?.setOnClickListener {
                        Documentation.documentationActivity!!.finish()
                        Identification_Activity.identificationActivity!!.finish()
                        finish()
                    }

                    dialog.show()
                } else if (TypeofActivity.equals("Normal")) {
                    val licenseNumber = et_licenseNumber.text.toString().trim()
                    val dob = et_dob.text.toString().trim()
                    val isuueOn = tv_isuueon.text.toString().trim()
                    val expiryDate = tv_expiryDate.text.toString().trim()
                    val naitionality = et_naitionality.text.toString().trim()
                    val licenseType = et_licenseType.text.toString().trim()

                    val vehicalModel = et_vehicalModel.text.toString().trim()
                    val vehicalMake = et_vehicalMake.text.toString().trim()
                    val vehicalColor = et_vehicalColor.text.toString().trim()

                    if (validator.addLicenseValid(this, licenseType, licenseNumber, dob, isuueOn, expiryDate, naitionality,vehicalColor,vehicalMake,vehicalModel)) {
                        viewModel.addLicenseApi(this, licenseType, licenseNumber, dob, isuueOn, expiryDate, naitionality, frontImage!!, backImage!!, token!!, true)
                        viewModel.getAddLicenseResponse().observe(this, this)
                    }

                } else {
                    dialog = Dialog(this@Fill_LicenseDetail_Activity)
                    dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
                    dialog.setContentView(R.layout.res_verification_panding)
                    dialog.setCancelable(true)
                    tv_Done = dialog.findViewById(R.id.tv_Done)
                    tv_Done?.setOnClickListener { onBackPressed() }
                    dialog.show()
                }
            }
            R.id.iv_edit_Licence -> {
                startActivity(Intent(this@Fill_LicenseDetail_Activity, Edit_LicenceDetail_Activity::class.java))
            }
            R.id.et_licenseType -> {
                if (!isOpenDot) {
                    openFiltertBy(et_licenseType)
                    isOpenDot = true
                } else {
                    if (popupWindow != null)
                        popupWindow!!.dismiss()
                    isOpenDot = false
                }
            }
        }

    }

    private fun openFiltertBy(etLicensetype: TextView?) {
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val customView = inflater.inflate(R.layout.select_licence_type_dialog, null)
        popupWindow = PopupWindow(customView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        popupWindow!!.isOutsideTouchable = true
        val adapter = LicenseTypeAdapter(this, licenseTypeList, this)
        customView!!.rv_licenseType.adapter = adapter
        popupWindow!!.showAsDropDown(etLicensetype)
    }


    override fun onChanged(liveData: RestObservable?) {
        when (liveData!!.status) {
            Status.SUCCESS -> {
                if (liveData.data is AddLicenseDetails) {
                   /* dialog = Dialog(this@Fill_LicenseDetail_Activity)
                    dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
                    dialog.setContentView(R.layout.res_verification_panding)
                    dialog.setCancelable(false)
                    tv_Done = dialog.findViewById(R.id.tv_Done)

                    tv_Done?.setOnClickListener {
                        Helper.showSuccessToast(this, liveData.data.message)
                        launchActivity<Login_Activity>()
                        finishAffinity()
                    }

                    dialog.show()*/
                    Helper.showSuccessToast(this, liveData.data.message)
                    launchActivity<Home_Page>()
                    savePrefrence(Constants.TOKEN, token!!)
                    finishAffinity()
                }
            }


            Status.ERROR -> {

            }
            else -> {

            }
        }
    }

    override fun OnTypeSelection(type: String, position: Int) {
        et_licenseType.text = type
        isOpenDot = false
        popupWindow!!.dismiss()
       /* if (type.equals("Automobile")){
            et_vehicalModel.visibility=View.VISIBLE
            et_vehicalMake.visibility=View.VISIBLE
            et_vehicalColor.visibility=View.VISIBLE
        }else{
            et_vehicalModel.visibility=View.GONE
            et_vehicalMake.visibility=View.VISIBLE
            et_vehicalColor.visibility=View.VISIBLE
        }*/
    }

}