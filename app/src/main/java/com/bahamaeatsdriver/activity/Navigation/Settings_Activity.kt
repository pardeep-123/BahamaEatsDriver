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
import com.bahamaeatsdriver.activity.Documentation
import com.bahamaeatsdriver.activity.ForgotPassword.Fill_old_newPassword
import com.bahamaeatsdriver.activity.bank_details.BankDetailsActivity
import com.bahamaeatsdriver.activity.driver_availability.DriverAvailability
import com.bahamaeatsdriver.activity.driver_documentation.AddDocumentationActivity
import com.bahamaeatsdriver.helper.extensions.getprefObject
import com.bahamaeatsdriver.helper.extensions.savePrefObject
import com.bahamaeatsdriver.model_class.login.LoginResponse
import com.bahamaeatsdriver.model_class.notification_status_change.UpdateNotificationStatus
import com.bahamaeatsdriver.repository.BaseViewModel

class Settings_Activity : AppCompatActivity(), Observer<RestObservable> {
    var Iv_offnotification: ImageView? = null
    var iv_on_notification: ImageView? = null

    private val viewModel: BaseViewModel by lazy { ViewModelProvider(this).get(BaseViewModel::class.java) }
    private lateinit var driverDetails: LoginResponse

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        driverDetails = getprefObject(Constants.DRIVER_DETAILS)


        Iv_offnotification = findViewById(R.id.Iv_offnotification)
        iv_on_notification = findViewById(R.id.iv_on_notification)


        if (driverDetails.body.isNotification == 0) {
            Iv_offnotification!!.setVisibility(View.VISIBLE)
            iv_on_notification!!.setVisibility(View.GONE)
        } else {
            Iv_offnotification!!.setVisibility(View.GONE)
            iv_on_notification!!.setVisibility(View.VISIBLE)
        }
        iv_on_notification?.setOnClickListener {
//            Iv_offnotification!!.setVisibility(View.VISIBLE)
//            iv_on_notification!!.setVisibility(View.GONE)
            updateNotificationStatusApiCall("0")

        }
        Iv_offnotification?.setOnClickListener {
//            Iv_offnotification!!.setVisibility(View.GONE)
//            iv_on_notification!!.setVisibility(View.VISIBLE)
            updateNotificationStatusApiCall("1")
        }

    }

    private fun updateNotificationStatusApiCall(type: String) {
        viewModel.changeNotificationStatusApi(this, type, true)
        viewModel.getchangeNotificationStatusResponse().observe(this, this)
    }

    fun iv_backArrow_setting(view: View?) {
        onBackPressed()
        finish()
    }

    fun Relativ_changepassword(view: View?) {
        startActivity(Intent(this@Settings_Activity, Fill_old_newPassword::class.java))
    }

    fun Relativ_TandCond(view: View?) {
        startActivity(Intent(this@Settings_Activity, TermAnd_Conditions::class.java))
    }

    fun Relativ_Aboutus(view: View?) {
        startActivity(Intent(this@Settings_Activity, AboutUs::class.java))
    }
    fun Relativ_availability(view: View?) {
        startActivity(Intent(this@Settings_Activity, DriverAvailability::class.java))

    }  fun goToBackDetails(view: View?) {
        startActivity(Intent(this@Settings_Activity, BankDetailsActivity::class.java))
    }

    fun Relativ_documents(view: View?) {
//        startActivity(Intent(this@Settings_Activity, Documentation::class.java))
        startActivity(Intent(this@Settings_Activity, AddDocumentationActivity::class.java))
    }

    override fun onChanged(liveData: RestObservable?) {
        when (liveData!!.status) {
            Status.SUCCESS -> {
                if (liveData.data is UpdateNotificationStatus) {
                    driverDetails.body.isNotification = liveData.data.body.isNotification
                    if (liveData.data.body.isNotification == 0) {
                        Iv_offnotification!!.setVisibility(View.VISIBLE)
                        iv_on_notification!!.setVisibility(View.GONE)
                    } else {
                        Iv_offnotification!!.setVisibility(View.GONE)
                        iv_on_notification!!.setVisibility(View.VISIBLE)
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