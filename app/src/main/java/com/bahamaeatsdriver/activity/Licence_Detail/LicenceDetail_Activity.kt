package com.bahamaeatsdriver.activity.Licence_Detail

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bahamaeats.network.RestObservable
import com.bahamaeats.network.Status
import com.bahamaeatsdriver.R
import com.bahamaeatsdriver.helper.extensions.launchActivity
import com.bahamaeatsdriver.model_class.get_driver_license.Body
import com.bahamaeatsdriver.model_class.get_driver_license.GetDriverLicense
import com.bahamaeatsdriver.repository.BaseViewModel
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_edit__licence_detail_.*

class LicenceDetail_Activity : AppCompatActivity(), View.OnClickListener, Observer<RestObservable> {
    private val viewModel: BaseViewModel by lazy { ViewModelProvider(this).get(BaseViewModel::class.java) }
    private var licenseDetails: Body? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit__licence_detail_)
        iv_backArrow_licendedetail!!.setOnClickListener(this)
        iv_Linc_edit.setOnClickListener(this)
        Tv_licence_issue.setOnClickListener(this)
        tv_licenceBirth.setOnClickListener(this)
        Tv_Licence_expire.setOnClickListener(this)
    }

    override fun onResume() {
        super.onResume()
        /*****
         * Get Profil Details
         */
        viewModel.getDriverLicenseDetailsApi(this, true)
        viewModel.getDriverLicenseDetailsResposne().observe(this, this)
    }


    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.iv_Linc_edit -> {
                if (licenseDetails != null) {
                    launchActivity<Edit_LicenceDetail_Activity>()
                    {
                        putExtra("licenseDetails", licenseDetails)
                        putExtra("from", "1")
                    }
                }
            }
            R.id.iv_backArrow_licendedetail -> {
                finish()
            }
        }
    }

    override fun onChanged(liveData: RestObservable?) {
        when (liveData!!.status) {
            Status.SUCCESS -> {
                if (liveData.data is GetDriverLicense) {
                    licenseDetails = liveData.data.body
                    Glide.with(this).load(liveData.data.body.frontPhoto).placeholder(R.drawable.placeholder_rectangle).into(iv_front)
                    Glide.with(this).load(liveData.data.body.backPhoto).placeholder(R.drawable.placeholder_rectangle).into(iv_back)
                    ed_L_number.setText(liveData.data.body.licenseNo)
                    Tv_licence_issue.text = liveData.data.body.issueDate
                    Tv_Licence_expire.text = liveData.data.body.expiryDate
                    Ed_l_natiolaty.setText(liveData.data.body.nationality)
                    Ed_l_Licencetype.text = liveData.data.body.licenseType
                    tv_licenceBirth.text = liveData.data.body.dob
                }
            }

            Status.ERROR -> {

            }
            else -> {

            }
        }
    }
}