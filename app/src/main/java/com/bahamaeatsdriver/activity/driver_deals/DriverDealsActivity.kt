package com.bahamaeatsdriver.activity.driver_deals

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bahamaeats.network.RestObservable
import com.bahamaeats.network.Status
import com.bahamaeatsdriver.R
import com.bahamaeatsdriver.adapter.DriverDealsAdapter
import com.bahamaeatsdriver.listeners.OnDealSelection
import com.bahamaeatsdriver.model_class.driver_deals.Body
import com.bahamaeatsdriver.model_class.driver_deals.DriverDealsResponse
import com.bahamaeatsdriver.model_class.like_merchant_deal.LikeUnlikeDealResponse
import com.bahamaeatsdriver.repository.BaseViewModel
import kotlinx.android.synthetic.main.activity_driver_deals.*

class DriverDealsActivity : AppCompatActivity(), OnDealSelection, Observer<RestObservable> {
    var listPostion=-1
    var isDealFav=0
    lateinit var merchantDealDetails:Body
    private val viewModel: BaseViewModel by lazy { ViewModelProvider(this).get(BaseViewModel::class.java) }
    lateinit var dealsListing:ArrayList< Body>
    private lateinit var driverDealsAdapter:DriverDealsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_driver_deals)
        iv_back_btn.setOnClickListener { finish() }
        viewModel.driverDealsResposne(this, true)
        viewModel.getDriverDealsResposne().observe(this, this)
    }


    override fun onChanged(liveData: RestObservable?) {
        when (liveData!!.status) {
            Status.SUCCESS -> {
                if (liveData.data is LikeUnlikeDealResponse) {
                    /*  favouriteRestaurants.removeAt(removeFromFavListPosistion)
                    yourfavouriteAdapter.notifyDataSetChanged()*/
                    merchantDealDetails.favouritecount=liveData.data.body.status
                    dealsListing.set(listPostion,merchantDealDetails)
                    driverDealsAdapter.notifyDataSetChanged()
                }
                if (liveData.data is DriverDealsResponse) {
                    dealsListing=liveData.data.body
                    driverDealsAdapter = DriverDealsAdapter(this@DriverDealsActivity,dealsListing, this)
                    rv_deals.adapter = driverDealsAdapter
                }
            }
        }
    }

    override fun OnDealSelection(dealDetails: Body, position: Int,merchantId:String) {
        listPostion=position
        merchantDealDetails=dealDetails
        if (dealDetails.favouritecount==1)
            isDealFav=0
        else
            isDealFav=1
        viewModel.driverLikeMerchantDealApi(this,merchantId,isDealFav.toString(),true)
        viewModel.driverLikeMerchantDealResponse().observe(this,this)
    }
}