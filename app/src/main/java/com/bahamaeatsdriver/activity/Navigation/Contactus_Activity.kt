package com.bahamaeatsdriver.activity.Navigation

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bahamaeats.network.RestObservable
import com.bahamaeats.network.Status
import com.bahamaeatsdriver.R
import com.bahamaeatsdriver.helper.others.Helper
import com.bahamaeatsdriver.model_class.driver_support.DriverSupportResponse
import com.bahamaeatsdriver.repository.BaseViewModel
import kotlinx.android.synthetic.main.activity_contactus.*


class Contactus_Activity : AppCompatActivity(), View.OnClickListener, Observer<RestObservable> {
    private val viewModel: BaseViewModel by lazy { ViewModelProvider(this).get(BaseViewModel::class.java) }
private var callNumber=""
private var messageSupportLink=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contactus)
        rl_callSupport.setOnClickListener(this)
        rl_chatSupport.setOnClickListener(this)
        iv_backArrow_contactus.setOnClickListener(this)
    }

    override fun onResume() {
        super.onResume()
        viewModel.supportLinksApi(this, true)
        viewModel.supportLinksResposne().observe(this, this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.rl_callSupport -> {
                if (callNumber.isNotEmpty())
                { val intent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", callNumber, null))
                startActivity(intent)}
              /*  launchActivity<ShopStoreActivity> {
                    putExtra("from","Contactus_Activity")
                    putExtra("click","1")
                }*/
            }
            R.id.rl_chatSupport -> {
//                val link = "http://m.me/bahamaeats?ref=2062529467381132"
                if (messageSupportLink.isNotEmpty()){
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(messageSupportLink))
                    try {
                        startActivity(intent)
                    } catch (ex: android.content.ActivityNotFoundException) {
                        Helper.showSuccessToast(this,"Something went wrong")
                    }
                }
               /* launchActivity<ShopStoreActivity> {
                    putExtra("from","Contactus_Activity")
                    putExtra("click","0")
                }*/
            }
            R.id.iv_backArrow_contactus -> {
                finish()

            }
            R.id.Button_send -> {

            }
        }
    }
    override fun onChanged(liveData: RestObservable?) {
        when (liveData!!.status) {
            Status.SUCCESS -> {

                if (liveData.data is DriverSupportResponse) {
                    messageSupportLink=liveData.data.body.chatTeamLink
                    callNumber=liveData.data.body.callSupportLink
                }

            }
            else -> {

            }
        }
    }
}