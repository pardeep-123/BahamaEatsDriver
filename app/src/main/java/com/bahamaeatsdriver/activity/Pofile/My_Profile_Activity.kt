package com.bahamaeatsdriver.activity.Pofile

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bahamaeats.constant.Constants
import com.bahamaeats.network.RestObservable
import com.bahamaeats.network.Status
import com.bahamaeatsdriver.R
import com.bahamaeatsdriver.activity.Id_Details.Edit_Id_Details
import com.bahamaeatsdriver.activity.Licence_Detail.LicenceDetail_Activity
import com.bahamaeatsdriver.activity.driver_documentation.AddDocumentationActivity
import com.bahamaeatsdriver.helper.extensions.getprefObject
import com.bahamaeatsdriver.helper.extensions.launchActivity
import com.bahamaeatsdriver.helper.extensions.savePrefObject
import com.bahamaeatsdriver.helper.others.CommonMethods.parseDateToddMMyyyy
import com.bahamaeatsdriver.model_class.login.LoginResponse
import com.bahamaeatsdriver.model_class.profile_details.DriverProfileDetailsResposne
import com.bahamaeatsdriver.repository.BaseViewModel
import com.bumptech.glide.Glide
import com.mikhaellopez.circularimageview.CircularImageView

class My_Profile_Activity : AppCompatActivity(), Observer<RestObservable> {
    private var Relativ_profile: RelativeLayout? = null
    private var Card_lincencinfo: CardView? = null
    private var tv_fullName: TextView? = null
    private var tv_countryCode: TextView? = null
    private var tv_ContactNumber: TextView? = null
    private var tv_email: TextView? = null
    private var tv_dateOfJoin: TextView? = null
    private var tv_Username: TextView? = null
    private var iv_Profile_image: CircularImageView? = null

    private var profileDetails: DriverProfileDetailsResposne? = null
    private val viewModel: BaseViewModel by lazy { ViewModelProvider(this).get(BaseViewModel::class.java) }
    private lateinit var driverDetails: LoginResponse

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_profile)
        driverDetails = getprefObject(Constants.DRIVER_DETAILS)
        Relativ_profile = findViewById(R.id.Relativ_profile)
        Card_lincencinfo = findViewById(R.id.Card_lincencinfo)
        tv_fullName = findViewById(R.id.tv_fullName)
        tv_countryCode = findViewById(R.id.tv_countryCode)
        tv_ContactNumber = findViewById(R.id.tv_ContactNumber)
        tv_email = findViewById(R.id.tv_email)
        tv_dateOfJoin = findViewById(R.id.tv_dateOfJoin)
        tv_Username = findViewById(R.id.tv_username)
        iv_Profile_image = findViewById(R.id.iv_profileImage)
    }

    override fun onResume() {
        super.onResume()
        /*****
         * Get Profil Details
         */
        viewModel.getDriverDetailsResposneApi(this, driverDetails.body.id, true)
        viewModel.getDriverDetailsResposne().observe(this, this)
    }

    fun Relativ_info_licens(view: View?) {
        val intent = Intent(this@My_Profile_Activity, LicenceDetail_Activity::class.java)
        intent.putExtra("type", "From_profile")
        startActivity(intent)
    }

    fun iv_backArrow_profile(view: View?) {
        finish()
    }

    fun iv_editprofile(view: View?) {
        if (profileDetails != null) {
            launchActivity<Edit_profile>()
            {
                putExtra("profileDetails", profileDetails)
            }
        }
    }

    fun Relativ_insurance(view: View?) {
        startActivity(Intent(this@My_Profile_Activity, Edit_Id_Details::class.java))
    }
    fun Relativ_documentation(view: View?) {
        startActivity(Intent(this@My_Profile_Activity, AddDocumentationActivity::class.java))
    }

    override fun onChanged(liveData: RestObservable?) {
        when (liveData!!.status) {
            Status.SUCCESS -> {
                if (liveData.data is DriverProfileDetailsResposne) {
                    profileDetails = liveData.data
                    tv_email!!.text = liveData.data.body.email
                    tv_countryCode!!.text = liveData.data.body.countryCode
                    tv_ContactNumber!!.text = liveData.data.body.contactNo
                    if (liveData.data.body.fullName.isNotEmpty()){
                        tv_fullName!!.text = driverDetails.body.fullName
                        tv_Username!!.text = driverDetails.body.fullName
                    }
                    else{
                        tv_fullName!!.text = driverDetails.body.firstName + " " + driverDetails.body.lastName
                        tv_Username!!.text = driverDetails.body.firstName + " " + driverDetails.body.lastName
                    }
                    Glide.with(this).load(liveData.data.body.image).placeholder(R.drawable.profileimage).into(iv_Profile_image!!)
                    tv_dateOfJoin!!.text = parseDateToddMMyyyy(liveData.data.body.createdAt, "MMM dd, yyyy ")
                    //Update data to prefrence
                    driverDetails.body.image = liveData.data.body.image
                    driverDetails.body.fullName = liveData.data.body.fullName
                    savePrefObject(Constants.DRIVER_DETAILS, driverDetails)
                }
            }

            Status.ERROR -> {

            }
            else -> {

            }
        }
    }

}