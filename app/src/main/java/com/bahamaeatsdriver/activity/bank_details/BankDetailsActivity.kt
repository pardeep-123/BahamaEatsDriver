package com.bahamaeatsdriver.activity.bank_details

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.PopupWindow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bahamaeats.network.RestObservable
import com.bahamaeats.network.Status
import com.bahamaeatsdriver.R
import com.bahamaeatsdriver.adapter.LicenseTypeAdapter
import com.bahamaeatsdriver.di.App
import com.bahamaeatsdriver.helper.others.Validator
import com.bahamaeatsdriver.listeners.OnLicenseTypeSelection
import com.bahamaeatsdriver.model_class.add_bank_details.AddUpdateBankDetails
import com.bahamaeatsdriver.model_class.get_bank_details.GetBankDetails
import com.bahamaeatsdriver.model_class.license_type.LicenseTypeModel
import com.bahamaeatsdriver.repository.BaseViewModel
import kotlinx.android.synthetic.main.activity_bank_details.*
import kotlinx.android.synthetic.main.payout_email_popup_layouts.*
import kotlinx.android.synthetic.main.select_licence_type_dialog.view.*
import java.util.*
import javax.inject.Inject
import android.app.DatePickerDialog.OnDateSetListener
import android.widget.DatePicker
import com.bahamaeats.constant.Constants
import java.text.SimpleDateFormat

class BankDetailsActivity : AppCompatActivity(), View.OnClickListener, Observer<RestObservable>,
    OnLicenseTypeSelection {
    private var isOpenDot = false
    private var id = ""
    private var popupWindow: PopupWindow? = null
    private var licenseTypeList = ArrayList<LicenseTypeModel>()
    private val viewModel: BaseViewModel by lazy { ViewModelProvider(this).get(BaseViewModel::class.java) }
companion object{
     var textViewDob:TextView?=null
}

    @Inject
    lateinit var validator: Validator
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bank_details)
        App.getinstance().getmydicomponent().inject(this)
        textViewDob=findViewById(R.id.tv_dobs)
        licenseTypeList.add(LicenseTypeModel("1", "Savings"))
        licenseTypeList.add(LicenseTypeModel("2", "Checkings"))
        setOnCLickListiners()
        getBankDetailsApiCall()
        textViewDob!!.setOnClickListener {
            val fragment: DialogFragment = DatePickerFragment_static()
            fragment.show(supportFragmentManager, "Date Picker")
        }
    }

    private fun getBankDetailsApiCall() {
        viewModel.getBankDetailsApi(this, true)
        viewModel.getBankDetailsResposne().observe(this, this)
    }

    private fun enableEditableViews(value: Boolean) {
        //value-true means to make editable view
        /*  et_beneficiaryName.isEnabled = value
          et_bankName.isEnabled = value
          et_bankBranch.isEnabled = value
          et_accountNumber.isEnabled = value
          et_confirmAccountNumber.isEnabled = value
          et_accountType.isClickable = value*/
        et_firstName.isEnabled = value
        et_lastName.isEnabled = value
        et_email.isEnabled = value
        et_nib.isEnabled = value
        et_confirmNib.isEnabled = value
         textViewDob!!.isClickable = value
        textViewDob!!.setFocusable(value)
        if (value) {
            btn_submit.visibility = View.VISIBLE
            et_confirmNib.visibility = View.VISIBLE
        } else {
            et_confirmNib.visibility = View.GONE
            btn_submit.visibility = View.GONE
        }
    }

    private fun setOnCLickListiners() {
        iv_back.setOnClickListener(this)
//        et_accountType.setOnClickListener(this)
        btn_submit.setOnClickListener(this)
        iv_editBankDetails.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.iv_back -> {
                finish()
            }
            R.id.iv_editBankDetails -> {
//                et_confirmAccountNumber.visibility = View.VISIBLE
//                et_accountType.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ccp_down_arrow, 0)
                enableEditableViews(true)
            }
            R.id.btn_submit -> {
                /* val beneficiaryName = et_beneficiaryName.text.toString().trim()
                 val bankName = et_bankName.text.toString().trim()
                 val bankBranch = et_bankBranch.text.toString().trim()
                 val accountNumber = et_accountNumber.text.toString().trim()
                 val confirmAccountNumber = et_confirmAccountNumber.text.toString().trim()
                 val accountType = et_accountType.text.toString().trim()
                  if (validator.addBankDetails(this, accountType, beneficiaryName, bankName, accountNumber, confirmAccountNumber, bankBranch)) {
                     viewModel.addBankDetailsApi(this, beneficiaryName, bankName, bankBranch, accountNumber, accountType, id, true)
                     viewModel.getAddBankDetailsResposne().observe(this, this)
                 }*/
                val firstName = et_firstName.text.toString().trim()
                val lastName = et_lastName.text.toString().trim()
                val dob =  textViewDob!!.text.toString().trim()
                val email = et_email.text.toString().trim()
                val nib = et_nib.text.toString().trim()
                val confirmNib = et_confirmNib.text.toString().trim()

                if (validator.addPayoutDetails(
                        this,
                        firstName,
                        lastName,
                        dob,
                        email,
                        nib,
                        confirmNib
                    )
                ) {
                    val dialog = Dialog(this)
                    dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
                    dialog.window!!.setGravity(Gravity.CENTER)
                    dialog.window!!.setLayout(
                        WindowManager.LayoutParams.MATCH_PARENT,
                        WindowManager.LayoutParams.WRAP_CONTENT
                    )
                    dialog.setContentView(R.layout.payout_email_popup_layouts)
                    dialog.setCancelable(false)
                    dialog.show()
                    dialog.tv_yes.setOnClickListener {
                        viewModel.addEditBankDetailsApi(
                            this,
                            firstName,
                            lastName,
                            dob,
                            email,
                            nib,
                            id,
                            true
                        )
                        viewModel.addEditBankDetailsResposne().observe(this, this)
                        dialog.dismiss()
                    }
                    dialog.tv_no.setOnClickListener {
                        dialog.dismiss()
                    }

                }
            }
            /*  R.id.et_accountType -> {
                  if (!isOpenDot) {
                      openFiltertBy(et_accountType)
                      isOpenDot = true
                  } else {
                      if (popupWindow != null)
                          popupWindow!!.dismiss()
                      isOpenDot = false
                  }
              }*/
        }
    }

    private fun openFiltertBy(etLicensetype: TextView?) {
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val customView = inflater.inflate(R.layout.select_licence_type_dialog, null)
        popupWindow = PopupWindow(
            customView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        popupWindow!!.isOutsideTouchable = true
        val adapter = LicenseTypeAdapter(this, licenseTypeList, this)
        customView!!.rv_licenseType.adapter = adapter
        popupWindow!!.showAsDropDown(etLicensetype)
    }

    override fun onChanged(liveData: RestObservable?) {

        when (liveData!!.status) {
            Status.SUCCESS -> {
                 if (liveData.data is AddUpdateBankDetails) {
                     id = liveData.data.body.id.toString()
                     enableEditableViews(false)
                     btn_submit.visibility = View.GONE
//                     et_confirmAccountNumber.visibility = View.GONE
//                     et_accountType.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                 }

                if (liveData.data is GetBankDetails) {
                    if (liveData.data.body != null) {
//                        et_accountType.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
//                        et_confirmAccountNumber.visibility = View.GONE
                        iv_editBankDetails.visibility = View.VISIBLE
                        tv_verified.visibility = View.VISIBLE
                        val bankDetais = liveData.data.body
                        id = bankDetais.id.toString()
                        if (bankDetais.isVerified == 1)
                            tv_verified.text = getString(R.string.bank_details_are_verified)
                        else
                            tv_verified.text = getString(R.string.bank_details_under_review)
                        et_firstName.setText(bankDetais.firstName)
                        et_lastName.setText(bankDetais.lastName)
                         textViewDob!!.setText(bankDetais.dob)
                        et_email.setText(bankDetais.email)
                        et_nib.setText(bankDetais.nibNumber)
                        et_confirmNib.setText(bankDetais.nibNumber)
//                        et_accountType.text = bankDetais.accountType
                        enableEditableViews(false)
                    } else {
//                        et_confirmAccountNumber.visibility = View.VISIBLE
                        tv_verified.visibility = View.GONE
                        iv_editBankDetails.visibility = View.GONE
                        enableEditableViews(true)
//                        et_accountType.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ccp_down_arrow, 0)
                    }

                }
            }
            else -> {
                Log.d("onChanged: ", "Error")
            }
        }
    }

    override fun OnTypeSelection(type: String, position: Int) {
        /*  et_accountType.text = type
          isOpenDot = false
          popupWindow!!.dismiss()*/
    }

    @SuppressLint("ValidFragment")
    class DatePickerFragment_static : DialogFragment(), OnDateSetListener {
        lateinit var dpd: DatePickerDialog
        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            val calendar = Calendar.getInstance()
            val year = calendar[Calendar.YEAR]
            val month = calendar[Calendar.MONTH]
            val day = calendar[Calendar.DAY_OF_MONTH]
            dpd = DatePickerDialog(requireActivity(), this, year - 18, month, day)
            calendar[year - 18, month] = day
            val value = calendar.timeInMillis
            dpd.datePicker.maxDate = value
            return dpd
        }

        override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int) {
            val cal = Calendar.getInstance()
            cal.timeInMillis = 0
            cal[year, monthOfYear, dayOfMonth, 0, 0] = 0
            val Date = cal.time
            Log.e("date", "asa  " + cal.time)
             textViewDob!!.text=(SimpleDateFormat(Constants.PAYOUT_DOB_FORMAT).format(Date).toString())
        }
    }
}