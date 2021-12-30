package com.bahamaeatsdriver.activity.referral_code

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bahamaeats.constant.Constants
import com.bahamaeats.network.RestObservable
import com.bahamaeats.network.Status
import com.bahamaeatsdriver.BuildConfig
import com.bahamaeatsdriver.R
import com.bahamaeatsdriver.helper.extensions.getprefObject
import com.bahamaeatsdriver.helper.others.Helper
import com.bahamaeatsdriver.model_class.driver_details.DriverDetails
import com.bahamaeatsdriver.model_class.login.LoginResponse
import com.bahamaeatsdriver.model_class.profile_details.DriverProfileDetailsResposne
import com.bahamaeatsdriver.repository.BaseViewModel
import kotlinx.android.synthetic.main.activity_share_referral.*

class ShareReferralActivity : AppCompatActivity(), Observer<RestObservable> {

    private var referralCode = ""
    private var numberOfRide = ""
    private var rewardAmount = ""
    private val viewModel: BaseViewModel by lazy { ViewModelProvider(this).get(BaseViewModel::class.java) }
    private lateinit var driverDetails: DriverDetails

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share_referral)
        driverDetails = getprefObject(Constants.DRIVER_DETAILS)
        iv_back.setOnClickListener { finish() }
        iv_copyToClipBoard.setOnClickListener {
            if (referralCode.isNotEmpty()) {
                val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clipData = ClipData.newPlainText("text", tv_referralCode.text.toString())
                clipboardManager.setPrimaryClip(clipData)
                Helper.showSuccessToast(this, "Copied to clipboard")
            }
        }
        btn_inviteContacts.setOnClickListener {  try {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name))

//            var shareMessage = "\nLet me recommend you this application \n\n"
            var shareMessage ="\nEarn $" + numberOfRide + " for each new BE Driver you refer who completes "+ rewardAmount+" deliveries \n\n"
            shareMessage = """ ${shareMessage}https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID} """.trimIndent() + referralCode
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
            startActivity(Intent.createChooser(shareIntent, "choose one"))
        } catch (e: Exception) {
            e.toString()
        }
        }
        getDriverProfileDetails()

    }

        override fun onChanged(liveData: RestObservable?) {
        when (liveData!!.status) {
            Status.SUCCESS -> {
                if (liveData.data is DriverProfileDetailsResposne) {
                    numberOfRide=liveData.data.body.driver_ride_complete_count
                    rewardAmount=liveData.data.body.driver_referrals_amount
                    referralCode=liveData.data.body.referrals_code
                    tv_referralLabel.text = "Invite your friends and get $" + rewardAmount + " credits when they complete their "+ numberOfRide+if (numberOfRide=="1")" ride" else " rides"
                    tv_referralCode.text=referralCode
                }
            }
            Status.ERROR -> {
            }
            else -> {

            }
        }
    }
    /*****
     * Get Profil Details
     */
    private fun getDriverProfileDetails() {
        viewModel.getDriverDetailsResposneApi(this, driverDetails.id, true)
        viewModel.getDriverDetailsResposne().observe(this, this)
    }
}