package com.bahamaeatsdriver.activity.Id_Details

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.Dialog
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.DatePicker
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bahamaeats.constant.Constants
import com.bahamaeats.network.RestObservable
import com.bahamaeats.network.Status
import com.bahamaeatsdriver.R
import com.bahamaeatsdriver.activity.login_register.Identification_Activity
import com.bahamaeatsdriver.di.App
import com.bahamaeatsdriver.helper.extensions.equalsIgnoreCase
import com.bahamaeatsdriver.helper.others.ImagePicker
import com.bahamaeatsdriver.helper.others.Validator
import com.bahamaeatsdriver.model_class.get_id_details.Body
import com.bahamaeatsdriver.model_class.get_id_details.GetIdDetailsRespone
import com.bahamaeatsdriver.model_class.update_id_details.UpdateIdDetailsResponse
import com.bahamaeatsdriver.place_picker.LocationActivity
import com.bahamaeatsdriver.repository.BaseViewModel
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_edit_id_details.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class Edit_Id_Details : ImagePicker(), Observer<RestObservable>, View.OnClickListener {
    private lateinit var dialog: Dialog
    private var Type_image: String? = null
    private var backImage = ""
    private var frontImage = ""
    private var image_path = ""
    private var onClickFrom = ""
    private var idDetailsData: Body? = null
    private var LOCATION_REQUEST = 1002

    @Inject
    lateinit var validator: Validator
    private val viewModel: BaseViewModel by lazy { ViewModelProvider(this).get(BaseViewModel::class.java) }
    private lateinit var idDetailsRespone: GetIdDetailsRespone

    override fun selectedImage(imagePath: String?, thumbnailVideoPath: String) {
        image_path = imagePath!!
        if (Type_image == "Front") {
            Glide.with(this).load(imagePath).placeholder(R.drawable.app_logo_black).into(iv_frontID!!)
            frontImage = image_path
        } else {
            Glide.with(this).load(imagePath).placeholder(R.drawable.app_logo_black).into(iv_backID!!)
            backImage = image_path
        }
    }

    override fun getUpdatedPhoneNoAfterVerify(contactNumber: String, updatedCountryCode: String) {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_id_details)
        App.getinstance().getmydicomponent().inject(this)
        editIdDetails = this
        tv_issue_date = findViewById(R.id.tv_issueDate)
        tv_dob = findViewById(R.id.tv_Dob)

        tv_dob.setOnClickListener {
            datetype = "birthdate"
            val fragment: DialogFragment = DatePickerFragment_static()
            fragment.show(supportFragmentManager, "Date Picker")
        }
        tv_issue_date.setOnClickListener {
            datetype = "Issuedate"
            val fragment: DialogFragment = DatePickerFragment_static()
            fragment.show(supportFragmentManager, "Date Picker")
        }

        iv_edit_id.setOnClickListener(this)
        ed_id_address.setOnClickListener(this)
        btn_moveToAddIdDeatils.setOnClickListener(this)
    }

    override fun onResume() {
        super.onResume()
        /****
         * Get Id card Details
         */
        viewModel.getIdDetailsApi(this, true)
        viewModel.getIdDetails().observe(this, this)
    }

    fun iv_backArrow_editid(view: View?) {
        if (onClickFrom.isEmpty()) {
            finish()
        } else if (onClickFrom.equalsIgnoreCase("Edit")) {
            onClickFrom = ""
            tv_id_hedder!!.setText("ID DETAILS")
            iv_edit_id!!.setVisibility(View.VISIBLE)
            iv_front_imageID!!.setVisibility(View.GONE)
            iv_back_imageID!!.setVisibility(View.GONE)
            LL_cancle_update_button!!.setVisibility(View.GONE)
            ed_idfirstname!!.setEnabled(false)
            Ed_id_lastname!!.setEnabled(false)
            Ed_id_number!!.setEnabled(false)
            tv_issue_date.setEnabled(false)
            tv_dob.setEnabled(false)
            ed_id_address!!.setEnabled(false)
            ed_id_address!!.isClickable=false
        } else {
            finish()
        }
    }

    fun Button_update(view: View?) {
        val firstName = ed_idfirstname.text.toString().trim()
        val lastName = Ed_id_lastname.text.toString().trim()
        val idCardNumber = Ed_id_number.text.toString().trim()
        val dob = tv_dob.text.toString().trim()
        val issueDate = tv_issue_date.text.toString().trim()
        val addresss = ed_id_address.text.toString().trim()
        if (validator.addIdCardDetailsValid(this, firstName, lastName, idCardNumber, dob, issueDate, addresss)) {
            viewModel.updateIdCardApi(this, idDetailsRespone.body.id.toString(), idCardNumber, firstName, lastName, dob, issueDate, addresss, frontImage, backImage, true)
            viewModel.getUpdateIdCardResponse().observe(this, this)
        }
    }

    fun Button_id_cancle(view: View?) {
        if (onClickFrom.isEmpty()) {
            finish()
        } else if (onClickFrom.equalsIgnoreCase("Edit")) {
            onClickFrom = ""
            tv_id_hedder!!.setText("ID DETAILS")
            iv_edit_id!!.setVisibility(View.VISIBLE)
            iv_front_imageID!!.setVisibility(View.GONE)
            iv_back_imageID!!.setVisibility(View.GONE)
            LL_cancle_update_button!!.setVisibility(View.GONE)
            ed_idfirstname!!.setEnabled(false)
            Ed_id_lastname!!.setEnabled(false)
            Ed_id_number!!.setEnabled(false)
            tv_issue_date.setEnabled(false)
            tv_dob.setEnabled(false)
            ed_id_address!!.setEnabled(false)
            ed_id_address.isClickable = false
        } else {
            finish()
        }
    }

    fun iv_front_imageID(view: View?) {
        Type_image = "Front"
        checkPermissionCamera(false, "", "")
    }

    fun iv_back_imageID(view: View?) {
        Type_image = "Back"
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
            if (datetype.equalsIgnoreCase("birthdate")) {
                dpd = DatePickerDialog(requireActivity(), this, year - 18, month, day)
                calendar[year - 18, month] = day
                val value = calendar.timeInMillis
                dpd.datePicker.maxDate = value
            } else if (datetype.equalsIgnoreCase("Issuedate")) {
                dpd = DatePickerDialog(requireActivity(), this, year, month, day)
                calendar[year, month] = day
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
            if (datetype.equalsIgnoreCase("birthdate")) {
                tv_dob.text = SimpleDateFormat(Constants.LICENSE_DATE_FORMAT).format(Date)
            } else if (datetype.equalsIgnoreCase("Issuedate")) {
                tv_issue_date.text = SimpleDateFormat(Constants.LICENSE_DATE_FORMAT).format(Date)
            }
        }
    }

    companion object {
        lateinit var tv_dob: TextView
        lateinit var tv_issue_date: TextView
        var datetype = ""
        var editIdDetails: Edit_Id_Details? = null

    }

    override fun onClick(p0: View?) {
        val v1 = p0!!.id
        when (v1) {
            R.id.iv_edit_id -> {
                if (idDetailsData != null && idDetailsData!!.id != 0) {
                    onClickFrom = "Edit"
                    tv_id_hedder!!.setText("EDIT ID DETAILS")
                    iv_edit_id!!.setVisibility(View.GONE)
                    iv_front_imageID!!.setVisibility(View.VISIBLE)
                    iv_back_imageID!!.setVisibility(View.VISIBLE)
                    LL_cancle_update_button!!.setVisibility(View.VISIBLE)
                    ed_idfirstname!!.setEnabled(true)
                    Ed_id_lastname!!.setEnabled(true)
                    Ed_id_number!!.setEnabled(true)
                    tv_dob.setEnabled(true)
                    tv_issue_date.setEnabled(true)
                    ed_id_address!!.setEnabled(true)
                    ed_id_address!!.isClickable = true
                }
            }
            R.id.btn_moveToAddIdDeatils -> {
                val intent = Intent(this@Edit_Id_Details, Identification_Activity::class.java)
                intent.putExtra("Typeof", "Idproof")
                startActivity(intent)
            }
            R.id.ed_id_address -> {
                openLocationActivity()
            }

        }
    }

    override fun onChanged(liveData: RestObservable?) {
        when (liveData!!.status) {
            Status.SUCCESS -> {
                if (liveData.data is UpdateIdDetailsResponse) {
                    onClickFrom = ""
                    tv_id_hedder!!.setText("ID DETAILS")
                    iv_edit_id!!.setVisibility(View.VISIBLE)
                    iv_front_imageID!!.setVisibility(View.GONE)
                    iv_back_imageID!!.setVisibility(View.GONE)
                    LL_cancle_update_button!!.setVisibility(View.GONE)
                    ed_idfirstname!!.setEnabled(false)
                    Ed_id_lastname!!.setEnabled(false)
                    Ed_id_number!!.setEnabled(false)
                    tv_issue_date.setEnabled(false)
                    tv_dob.setEnabled(false)
                    ed_id_address!!.setEnabled(false)
                    ed_id_address!!.isClickable = false
                }
                if (liveData.data is GetIdDetailsRespone) {
                    if (liveData.data.body.id != 0) {
                        if (onClickFrom.equalsIgnoreCase("Edit")) {
                            Log.e("GetIdDetailsRespone", "data fill on set on resume in edit case")
                        } else {
                            ll_details.setEnabled(true)
                            rl_rootAddIdDestils.visibility = View.GONE
                            idDetailsData = liveData.data.body
                            idDetailsRespone = liveData.data
                            ed_idfirstname.setText(liveData.data.body.firstName)
                            Ed_id_lastname.setText(liveData.data.body.lastName)
                            Ed_id_number.setText(liveData.data.body.idNumber)
                            tv_issue_date.setText(liveData.data.body.issueDate)
                            tv_dob.setText(liveData.data.body.dob)
                            ed_id_address.setText(liveData.data.body.address)
                            Glide.with(this).load(liveData.data.body.frontPhoto).placeholder(R.drawable.placeholder_circle).into(iv_frontID!!)
                            Glide.with(this).load(liveData.data.body.backPhoto).placeholder(R.drawable.placeholder_circle).into(iv_backID!!)
                        }

                    } else {
                        ll_details.setEnabled(false)
                        rl_rootAddIdDestils.visibility = View.VISIBLE
                        ed_idfirstname.setHint("")
                        Ed_id_lastname.setHint("")
                        Ed_id_number.setHint("")
                        tv_issue_date.setHint("")
                        tv_dob.setHint("")
                        ed_id_address.setHint("")
                    }
                }
            }
            Status.ERROR -> {
            }
            else -> {
            }
        }
    }

    override fun onBackPressed() {
        if (onClickFrom.isEmpty()) {
            finish()
        } else if (onClickFrom.equalsIgnoreCase("Edit")) {
            onClickFrom = ""
            tv_id_hedder!!.setText("ID DETAILS")
            iv_edit_id!!.setVisibility(View.VISIBLE)
            iv_front_imageID!!.setVisibility(View.GONE)
            iv_back_imageID!!.setVisibility(View.GONE)
            LL_cancle_update_button!!.setVisibility(View.GONE)
            ed_idfirstname!!.setEnabled(false)
            Ed_id_lastname!!.setEnabled(false)
            Ed_id_number!!.setEnabled(false)
            tv_issue_date.setEnabled(false)
            tv_dob.setEnabled(false)
            ed_id_address!!.setEnabled(false)
            ed_id_address!!.isClickable = false
        } else {
            finish()
        }
    }

    fun openLocationActivity() {
        val intent = Intent(this, LocationActivity::class.java)
        startActivityForResult(intent, LOCATION_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == LOCATION_REQUEST) {
                val locaddress = data!!.getStringExtra("address")
                ed_id_address.text = locaddress
                val latitude = data.getStringExtra("latitude")
                val longitude = data.getStringExtra("longitude")
               /* if (latitude != null) {
                    Completed_Address(latitude.toDouble(), longitude.toDouble());
                }*/

            }
        }
    }

    fun Completed_Address(center_lat: Double, center_lang: Double) {
        val geocoder: Geocoder
        val addresses: List<Address>?
        geocoder = Geocoder(this, Locale.getDefault())
        try {
            addresses = geocoder.getFromLocation(center_lat, center_lang, 1)
            if (addresses != null && addresses.size > 0) {
//                et_country.text = addresses[0].getCountryName()
//                et_state.text = addresses[0].getAdminArea()
//                et_city.text = addresses[0].getSubAdminArea()
//                et_zipCode.text = addresses[0].getPostalCode()

            } else {
            }
        } catch (e: java.lang.Exception) {
        }
    }

}