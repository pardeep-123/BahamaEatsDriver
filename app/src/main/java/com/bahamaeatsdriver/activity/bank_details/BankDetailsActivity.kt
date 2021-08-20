package com.bahamaeatsdriver.activity.bank_details

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bahamaeats.network.RestObservable
import com.bahamaeats.network.Status
import com.bahamaeatsdriver.Adapter.LicenseTypeAdapter
import com.bahamaeatsdriver.R
import com.bahamaeatsdriver.di.App
import com.bahamaeatsdriver.helper.others.Validator
import com.bahamaeatsdriver.listeners.OnLicenseTypeSelection
import com.bahamaeatsdriver.model_class.add_bank_details.AddUpdateBankDetails
import com.bahamaeatsdriver.model_class.get_bank_details.GetBankDetails
import com.bahamaeatsdriver.model_class.license_type.LicenseTypeModel
import com.bahamaeatsdriver.repository.BaseViewModel
import kotlinx.android.synthetic.main.activity_bank_details.*
import kotlinx.android.synthetic.main.select_licence_type_dialog.view.*
import java.util.*
import javax.inject.Inject

class BankDetailsActivity : AppCompatActivity(), View.OnClickListener, Observer<RestObservable>, OnLicenseTypeSelection {
    private var isOpenDot = false
    private var id = ""
    private var popupWindow: PopupWindow? = null
    private var licenseTypeList = ArrayList<LicenseTypeModel>()
    private val viewModel: BaseViewModel by lazy { ViewModelProvider(this).get(BaseViewModel::class.java) }

    @Inject
    lateinit var validator: Validator
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bank_details)
        App.getinstance().getmydicomponent().inject(this)
        licenseTypeList.add(LicenseTypeModel("1", "Savings"))
        licenseTypeList.add(LicenseTypeModel("2", "Checkings"))
        setOnCLickListiners()
        getBankDetailsApiCall()
    }

    private fun getBankDetailsApiCall() {
        viewModel.getBankDetailsApi(this, true)
        viewModel.getBankDetailsResposne().observe(this, this)
    }

    private fun enableEditableViews(value: Boolean) {
        //value-true means to make editable view
        et_beneficiaryName.isEnabled = value
        et_bankName.isEnabled = value
        et_bankBranch.isEnabled = value
        et_accountNumber.isEnabled = value
        et_confirmAccountNumber.isEnabled = value
        et_accountType.isClickable = value
        if (value)
            btn_submit.visibility = View.VISIBLE
        else
            btn_submit.visibility = View.GONE
    }

    private fun setOnCLickListiners() {
        iv_back.setOnClickListener(this)
        et_accountType.setOnClickListener(this)
        btn_submit.setOnClickListener(this)
        iv_editBankDetails.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.iv_back -> {
                finish()
            }
            R.id.iv_editBankDetails -> {
                et_confirmAccountNumber.visibility = View.VISIBLE
                et_accountType.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ccp_down_arrow, 0)
                enableEditableViews(true)
            }
            R.id.btn_submit -> {
                val beneficiaryName = et_beneficiaryName.text.toString().trim()
                val bankName = et_bankName.text.toString().trim()
                val bankBranch = et_bankBranch.text.toString().trim()
                val accountNumber = et_accountNumber.text.toString().trim()
                val confirmAccountNumber = et_confirmAccountNumber.text.toString().trim()
                val accountType = et_accountType.text.toString().trim()
                if (validator.addBankDetails(this, accountType, beneficiaryName, bankName, accountNumber, confirmAccountNumber, bankBranch)) {
                    viewModel.addBankDetailsApi(this, beneficiaryName, bankName, bankBranch, accountNumber, accountType, id, true)
                    viewModel.getAddBankDetailsResposne().observe(this, this)
                }
            }
            R.id.et_accountType -> {
                if (!isOpenDot) {
                    openFiltertBy(et_accountType)
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
                if (liveData.data is AddUpdateBankDetails) {
                    id = liveData.data.body.id.toString()
                    enableEditableViews(false)
                    iv_editBankDetails.visibility = View.VISIBLE
                    et_confirmAccountNumber.visibility = View.GONE
                    et_accountType.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                }
                if (liveData.data is GetBankDetails) {
                    if (liveData.data.body != null) {
                        et_accountType.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                        et_confirmAccountNumber.visibility = View.GONE
                        iv_editBankDetails.visibility = View.VISIBLE
                        tv_verified.visibility = View.VISIBLE
                        val bankDetais = liveData.data.body
                        id=bankDetais.id.toString()
                        if (bankDetais.isVerified == 1)
                            tv_verified.text = getString(R.string.bank_details_are_verified)
                        else
                            tv_verified.text = getString(R.string.bank_details_under_review)
                        et_beneficiaryName.setText(bankDetais.accountHolderName)
                        et_bankName.setText(bankDetais.bankName)
                        et_bankBranch.setText(bankDetais.branchName)
                        et_confirmAccountNumber.setText(bankDetais.accountNumber)
                        et_accountNumber.setText(bankDetais.accountNumber)
                        et_accountType.text = bankDetais.accountType
                        enableEditableViews(false)
                    } else {
                        et_confirmAccountNumber.visibility = View.VISIBLE
                        tv_verified.visibility = View.GONE
                        iv_editBankDetails.visibility = View.GONE
                        enableEditableViews(true)
                        et_accountType.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ccp_down_arrow, 0)
                    }

                }
            }
            else -> {
                Log.d( "onChanged: ","Error")
            }
        }
    }

    override fun OnTypeSelection(type: String, position: Int) {
        et_accountType.text = type
        isOpenDot = false
        popupWindow!!.dismiss()
    }
}