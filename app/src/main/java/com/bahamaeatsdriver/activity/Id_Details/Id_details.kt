package com.bahamaeatsdriver.activity.Id_Details

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.DatePicker
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bahamaeats.constant.Constants
import com.bahamaeats.network.RestObservable
import com.bahamaeats.network.Status
import com.bahamaeatsdriver.R
import com.bahamaeatsdriver.activity.Documentation
import com.bahamaeatsdriver.activity.login_register.Identification_Activity
import com.bahamaeatsdriver.di.App
import com.bahamaeatsdriver.helper.extensions.equalsIgnoreCase
import com.bahamaeatsdriver.helper.others.Validator
import com.bahamaeatsdriver.model_class.add_idDetails.AddIdDetailsResponse
import com.bahamaeatsdriver.place_picker.LocationActivity
import com.bahamaeatsdriver.repository.BaseViewModel
import kotlinx.android.synthetic.main.activity_id_details.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


class Id_details : AppCompatActivity(), View.OnClickListener, Observer<RestObservable> {

    private var backImage: String? = null
    private var frontImage: String? = null
    private val viewModel: BaseViewModel by lazy { ViewModelProvider(this).get(BaseViewModel::class.java) }
    private var LOCATION_REQUEST = 1001

    @Inject
    lateinit var validator: Validator
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_id_details)
        App.getinstance().getmydicomponent().inject(this)
        iv_backArrow_iddetail.setOnClickListener(this)
        btn_continue.setOnClickListener(this)
        et_address.setOnClickListener(this)
        /**
         * TypeofActivity=Normal(from registration process)
         * TypeofActivity=From_profile(from )
         */
        if (intent.getStringExtra("type") != null) {
            frontImage = intent.getStringExtra("frontImage")
            backImage = intent.getStringExtra("backImage")
        }
        tv_dob = findViewById(R.id.Tv_idbirthdate)
        tv_issue_date = findViewById(R.id.tv_id_issudate)
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
    }

    @SuppressLint("ValidFragment")
    class DatePickerFragment_static : DialogFragment(), OnDateSetListener {
        private lateinit var dpd: DatePickerDialog
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
        var tv_Done: TextView? = null
        var datetype = ""
    }

    private fun openLocationActivity() {
        val intent = Intent(this, LocationActivity::class.java)
        startActivityForResult(intent, LOCATION_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == LOCATION_REQUEST) {
                val locaddress = data!!.getStringExtra("address")
                et_address.text = locaddress
            }
        }
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.et_address -> {
                openLocationActivity()
            }
            R.id.iv_backArrow_iddetail -> {
                finish()
            }

            R.id.btn_continue -> {
                val firstName = et_firstName.text.toString().trim()
                val lastName = et_lastName.text.toString().trim()
                val idCardNumber = et_idNumber.text.toString().trim()
                val dob = Tv_idbirthdate.text.toString().trim()
                val issueDate = tv_id_issudate.text.toString().trim()
                val addresss = et_address.text.toString().trim()

                if (validator.addIdCardDetailsValid(this, firstName, lastName, idCardNumber, dob, issueDate, addresss)) {
                    viewModel.addIdCardApi(this, idCardNumber, firstName, lastName, dob, issueDate, addresss, frontImage!!, backImage!!, true)
                    viewModel.getIdCardResponse().observe(this, this)
                }
            }
        }
    }


    override fun onChanged(liveData: RestObservable?) {
        when (liveData!!.status) {
            Status.SUCCESS -> {
                if (liveData.data is AddIdDetailsResponse) {
                   /* dialog = Dialog(this@Id_details)
                    dialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
                    dialog!!.setContentView(R.layout.res_verification_panding)
                    dialog!!.setCancelable(false)
                    tv_Done = dialog!!.findViewById(R.id.tv_Done)
                    tv_Done?.setOnClickListener {
                        if (Documentation.documentationActivity != null) {
                            Documentation.documentationActivity!!.finish()
                        }
                        if (Edit_Id_Details.editIdDetails != null) {
                            Edit_Id_Details.editIdDetails!!.finish()
                        }
                        Identification_Activity.identificationActivity!!.finish()
                        finish()
                    }
                    dialog!!.show()*/
                      if (Documentation.documentationActivity != null) {
                            Documentation.documentationActivity!!.finish()
                        }
                        if (Edit_Id_Details.editIdDetails != null) {
                            Edit_Id_Details.editIdDetails!!.finish()
                        }
                        Identification_Activity.identificationActivity!!.finish()
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