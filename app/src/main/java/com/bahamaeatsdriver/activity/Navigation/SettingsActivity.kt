package com.bahamaeatsdriver.activity.Navigation

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bahamaeats.constant.Constants
import com.bahamaeats.network.RestObservable
import com.bahamaeats.network.Status
import com.bahamaeatsdriver.R
import com.bahamaeatsdriver.activity.bank_details.BankDetailsActivity
import com.bahamaeatsdriver.activity.driver_availability.DriverAvailability
import com.bahamaeatsdriver.activity.driver_documentation.AddDocumentationActivity
import com.bahamaeatsdriver.activity.forgot_password.ForgotPasswordActivity
import com.bahamaeatsdriver.activity.referral_code.ShareReferralActivity
import com.bahamaeatsdriver.helper.extensions.getprefObject
import com.bahamaeatsdriver.helper.extensions.launchActivity
import com.bahamaeatsdriver.helper.extensions.savePrefObject
import com.bahamaeatsdriver.helper.others.Helper
import com.bahamaeatsdriver.model_class.driver_details.DriverDetails
import com.bahamaeatsdriver.model_class.get_current_ride.Body
import com.bahamaeatsdriver.model_class.get_current_ride.GetCurrentRideResponse
import com.bahamaeatsdriver.model_class.notification_status_change.UpdateNotificationStatus
import com.bahamaeatsdriver.model_class.profile_details.DriverProfileDetailsResposne
import com.bahamaeatsdriver.repository.BaseViewModel
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity(), Observer<RestObservable> {
    private var Iv_offnotification: ImageView? = null
    private var iv_on_notification: ImageView? = null
    private val viewModel: BaseViewModel by lazy { ViewModelProvider(this).get(BaseViewModel::class.java) }
    private lateinit var driverDetails: DriverDetails
    private var currentRideData: Body? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        driverDetails = getprefObject(Constants.DRIVER_DETAILS)
        Iv_offnotification = findViewById(R.id.Iv_offnotification)
        iv_on_notification = findViewById(R.id.iv_on_notification)
        if (driverDetails.isNotification == 0) {
            Iv_offnotification!!.visibility = View.VISIBLE
            iv_on_notification!!.visibility = View.GONE
        } else {
            Iv_offnotification!!.visibility = View.GONE
            iv_on_notification!!.visibility = View.VISIBLE
        }
        iv_on_notification?.setOnClickListener {
            updateNotificationStatusApiCall("0")

        }
        Iv_offnotification?.setOnClickListener {
            updateNotificationStatusApiCall("1")
        }
        currentRideApiCall()
    }

   private fun currentRideApiCall() {
        //rideStatus=  //1=>New 2=>Started 3=>Completed 4=>Cancelled */
    viewModel.currentRideApi(this, false)
    viewModel.getCurrentRideResposne().observe(this, this)
}
    override fun onResume() {
        super.onResume()
        getDriverProfileDetails()
    }
    private fun updateNotificationStatusApiCall(type: String) {
        viewModel.changeNotificationStatusApi(this, type, true)
        viewModel.getchangeNotificationStatusResponse().observe(this, this)
    }

    /*****
     * Get Profile Details
     */
    private fun getDriverProfileDetails() {
        viewModel.getDriverDetailsResposneApi(this, driverDetails.id, true)
        viewModel.getDriverDetailsResposne().observe(this, this)
    }

    fun iv_backArrow_setting(view: View?) {
        onBackPressed()
        finish()
    }

    fun Relativ_changepassword(view: View?) {
        startActivity(Intent(this@SettingsActivity, ForgotPasswordActivity::class.java))
    }

    fun Relativ_TandCond(view: View?) {
        startActivity(Intent(this@SettingsActivity, TermAnd_Conditions::class.java))
    }

    fun Relativ_Aboutus(view: View?) {
        startActivity(Intent(this@SettingsActivity, AboutUs::class.java))
    }

    fun Relativ_availability(view: View?) {

        if (currentRideData!=null){
            Helper.showErrorAlertWithoutTitle(this,getString(R.string.complete_ride_message))
        }else
        startActivity(Intent(this@SettingsActivity, DriverAvailability::class.java))

    }

    fun onClickInviteFriends(view: View?) {
        launchActivity<ShareReferralActivity> ()
    }

    fun goToBackDetails(view: View?) {
        startActivity(Intent(this@SettingsActivity, BankDetailsActivity::class.java))
    }

    fun Relativ_documents(view: View?) {
//        startActivity(Intent(this@Settings_Activity, Documentation::class.java))
        startActivity(Intent(this@SettingsActivity, AddDocumentationActivity::class.java))
    }

    override fun onChanged(liveData: RestObservable?) {
        when (liveData!!.status) {
            Status.SUCCESS -> {
                if (liveData.data is GetCurrentRideResponse) {
                    if (liveData.data.code == 403) {

                    } else {
                        currentRideData = liveData.data.body

                    }

                }


                if (liveData.data is DriverProfileDetailsResposne) {
                    /**
                     * driver_referrals_status: 0 ->off 1-> on
                     */
                    if (liveData.data.body.driver_referrals_status==0)
                    {
                        Relativ_inviteFriends.visibility=View.GONE
                        view_botttom.visibility=View.GONE
                    }
                        else
                    {
                        Relativ_inviteFriends.visibility=View.VISIBLE
                        view_botttom.visibility=View.VISIBLE
                    }
                }
                if (liveData.data is UpdateNotificationStatus) {
                    driverDetails.isNotification = liveData.data.body.isNotification
                    if (liveData.data.body.isNotification == 0) {
                        Iv_offnotification!!.visibility = View.VISIBLE
                        iv_on_notification!!.visibility = View.GONE
                    } else {
                        Iv_offnotification!!.visibility = View.GONE
                        iv_on_notification!!.visibility = View.VISIBLE
                    }
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