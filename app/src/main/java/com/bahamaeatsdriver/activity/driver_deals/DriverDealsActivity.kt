package com.bahamaeatsdriver.activity.driver_deals

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bahamaeats.network.RestObservable
import com.bahamaeats.network.Status
import com.bahamaeatsdriver.R
import com.bahamaeatsdriver.adapter.DriverDealsAdapter
import com.bahamaeatsdriver.listeners.OnDealSelection
import com.bahamaeatsdriver.model_class.driver_deals.All
import com.bahamaeatsdriver.model_class.driver_deals.Body
import com.bahamaeatsdriver.model_class.driver_deals.DriverDealsResponse
import com.bahamaeatsdriver.model_class.like_merchant_deal.LikeUnlikeDealResponse
import com.bahamaeatsdriver.repository.BaseViewModel
import kotlinx.android.synthetic.main.activity_driver_deals.*

class DriverDealsActivity : AppCompatActivity(), OnDealSelection, Observer<RestObservable> {
    var listPostion=-1

    /***
     * type=0 for all
     * type=1 for favourite
     */
    private var type=0
    private var isDealFav=0
    private val viewModel: BaseViewModel by lazy { ViewModelProvider(this).get(BaseViewModel::class.java) }
    private var dealsListing=ArrayList< All>()
    private lateinit var allDataFromApi:Body
    private lateinit var driverDealsAdapter:DriverDealsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_driver_deals)
        iv_back_btn.setOnClickListener { finish() }
        tv_all.setOnClickListener {
            if (type!=0){
                type=0
                tv_fav.setTextColor(Color.parseColor("#808080"))
                tv_all.setTextColor(Color.parseColor("#FFFFFF"))
                callDealApi()
            }

        }
        tv_fav.setOnClickListener {
            if (type!=1){
                type=1
                tv_all.setTextColor(Color.parseColor("#808080"))
                tv_fav.setTextColor(Color.parseColor("#FFFFFF"))
                callDealApi()
            }

        }
        callDealApi()
    }
    private fun callDealApi(){
        viewModel.driverDealsResposne(this, true)
        viewModel.getDriverDealsResposne().observe(this, this)
    }


    override fun onChanged(liveData: RestObservable?) {
        when (liveData!!.status) {
            Status.SUCCESS -> {
                if (liveData.data is LikeUnlikeDealResponse) {
                    if (type==0){
                        dealsListing=liveData.data.body.all
                    }
                    else{
                        dealsListing=liveData.data.body.favourite
                    }
                    if (dealsListing.isEmpty())
                        no_dataAvailable.visibility=View.VISIBLE
                    else
                        no_dataAvailable.visibility=View.GONE
                    driverDealsAdapter.updateList(dealsListing)

                }
                if (liveData.data is DriverDealsResponse) {
                    dealsListing.clear()
                    allDataFromApi=liveData.data.body
                    if (type==0)
                        dealsListing=liveData.data.body.all
                    else
                        dealsListing=liveData.data.body.favourite
                    if (dealsListing.isEmpty())
                        no_dataAvailable.visibility=View.VISIBLE
                    else
                        no_dataAvailable.visibility=View.GONE
                    driverDealsAdapter = DriverDealsAdapter(this@DriverDealsActivity,dealsListing, this)
                    rv_deals.adapter = driverDealsAdapter
                }
            }
        }
    }

    override fun OnDealSelection(dealDetails: All, position: Int, merchantId:String) {
        listPostion=position
        if (dealDetails.favouritecount==1)
            isDealFav=0
        else
            isDealFav=1
        viewModel.driverLikeMerchantDealApi(this,merchantId,isDealFav.toString(),true)
        viewModel.driverLikeMerchantDealResponse().observe(this,this)
    }
}