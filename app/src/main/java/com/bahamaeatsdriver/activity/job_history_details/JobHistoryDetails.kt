package com.bahamaeatsdriver.activity.job_history_details

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bahamaeats.constant.Constants
import com.bahamaeats.network.RestObservable
import com.bahamaeats.network.Status
import com.bahamaeatsdriver.Adapter.FoodQuantiytAdapter
import com.bahamaeatsdriver.R
import com.bahamaeatsdriver.activity.Navigation.Contactus_Activity
import com.bahamaeatsdriver.helper.extensions.launchActivity
import com.bahamaeatsdriver.helper.others.CommonMethods.convertTimeStampToDa
import com.bahamaeatsdriver.helper.others.Helper
import com.bahamaeatsdriver.model_class.job_history_details.JobHistoryyDetailsResponse
import com.bahamaeatsdriver.repository.BaseViewModel
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.activity_job_history_details.*


class JobHistoryDetails : AppCompatActivity(), Observer<RestObservable>, OnMapReadyCallback {

    private val viewModel: BaseViewModel by lazy { ViewModelProvider(this).get(BaseViewModel::class.java) }
    private var gmap: GoogleMap? = null
    private var latUser = ""
    private var longUser = ""
    private var latRestaurant = ""
    private var longRestaurant = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_job_history_details)
        iv_backArrow_payment.setOnClickListener {finish()}
        tv_support.setOnClickListener { launchActivity<Contactus_Activity>() }
        rl_root.setOnClickListener {}
        if (intent.getStringExtra("jobId") != null) {
            viewModel.jobHistoryDetailsApi(this, intent.getStringExtra("jobId")!!, true)
            viewModel.getJobHistoryDetailsResponse().observe(this, this)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onChanged(liveData: RestObservable?) {
        when (liveData!!.status) {
            Status.SUCCESS -> {
                if (liveData.data is JobHistoryyDetailsResponse) {
                    latUser = liveData.data.body.currentLat
                    longUser = liveData.data.body.currentLong
                    latRestaurant = liveData.data.body.fromLat
                    longRestaurant = liveData.data.body.fromLong
                    Glide.with(this).load(Constants.RESTAURANT_BASE_URL + liveData.data.body.restaurant.image).placeholder(R.drawable.placeholder_circle).into(iv_restaurantImage)

                    tv_orderPrice.text = "$ " + Helper.roundOffDecimalNew(liveData.data.body.order.netAmount.toFloat())
                    tv_orderId.text = liveData.data.body.order.id.toString()
                    if (liveData.data.body.order.orderDetails.size == 1)
                        tv_itemCount.text = liveData.data.body.order.orderDetails.size.toString() + " Item"
                    else
                        tv_itemCount.text = liveData.data.body.order.orderDetails.size.toString() + " Items"
                    tv_pickUpLocation.text = liveData.data.body.restaurant.address
                    Tv_pickupAdress.text = convertTimeStampToDa(liveData.data.body.order.driverConfirmedTimestamp.toLong())

                    tv_dropOffLocation.text = liveData.data.body.ToAddress
                    if (liveData.data.body.order.deliveredTimestamp != 0L)
                        tv_dropOffAdress.text = convertTimeStampToDa(liveData.data.body.order.deliveredTimestamp)
                    else
                        tv_dropOffAdress.text = "Deliver soon"
                    /** paymentMethod-1 for suncash
                     * paymentMethod-2 for paypal
                     * paymentMethod-4 for kanoo
                     * paymentMethod-5 for Atlantic
                     * paymentMethod-6 for IsLand Pay
                     * paymentMethod-7 for BE Wallet
                     * */
                    when (liveData.data.body.order.paymentMethod) {
                        "1" -> { tv_paymentMode.text = "Payment method: Suncash" }
                        "2" -> { tv_paymentMode.text = "Payment method: Paypal" }
                        "4" -> { tv_paymentMode.text = "Payment method: Kanoo" }
                        "5" -> { tv_paymentMode.text = "Payment method: Atlantic" }
                        "6" -> { tv_paymentMode.text = "Payment method: IsLand Pay" }
                        "7" -> { tv_paymentMode.text = "Payment method: "+getString(R.string.be_wallet) }
                        "8" -> { tv_paymentMode.text = "Payment method: "+getString(R.string.loyalty_bonus) }
                    }
                    tv_vatPercentage.text = "VAT(" + liveData.data.body.order.taxPercentage + "%) "
                    val serviceFee=liveData.data.body.order.serviceFee
                    if (serviceFee!=0.0){
                        view_serviceFee.visibility=View.VISIBLE
                        ll_serviceFee.visibility=View.VISIBLE
                    }else{
                        view_serviceFee.visibility=View.GONE
                        ll_serviceFee.visibility=View.GONE
                    }
                    tv_serviceFees.text = "$$serviceFee"

                    val deliveryFee=liveData.data.body.order.deliveryFee
                    if (deliveryFee!=0.0){
                        view_deliveryFee.visibility=View.VISIBLE
                        ll_deliveryRoot.visibility=View.VISIBLE
                    }else{
                        view_deliveryFee.visibility=View.GONE
                        ll_deliveryRoot.visibility=View.GONE
                    }
                    tv_deliveryCharges.text = "$$deliveryFee"

                     val taxFee=liveData.data.body.order.tax
                    if (taxFee!=0.0){
                        view_vat.visibility=View.VISIBLE
                        ll_vatRoot.visibility=View.VISIBLE
                        tv_vat.text = "$$taxFee"
                    }else{
                        view_vat.visibility=View.GONE
                        ll_vatRoot.visibility=View.GONE
                        tv_vat.text = "$" + Helper.roundOffDecimalNew(taxFee.toFloat())
                    }
                     val promoDiscount=liveData.data.body.order.promoDiscount
                    if (promoDiscount!=0.0){
                        view_promo.visibility=View.VISIBLE
                        ll_Promo.visibility=View.VISIBLE
                        tv_promo.text = "$$promoDiscount"
                    }else{
                        view_promo.visibility=View.GONE
                        ll_Promo.visibility=View.GONE
                        tv_promo.text = "$" + Helper.roundOffDecimalNew(promoDiscount.toFloat())
                    }
                    val tip=liveData.data.body.order.tip
                    if (tip!=0.0){
                        view_tip.visibility=View.VISIBLE
                        ll_tip.visibility=View.VISIBLE
                        tv_tip.text = "$$tip"
                    }else{
                        view_tip.visibility=View.GONE
                        ll_tip.visibility=View.GONE
                        tv_tip.text = "$" + Helper.roundOffDecimalNew(tip.toFloat())
                    }
                    val adapter = FoodQuantiytAdapter(this, liveData.data.body.order.orderDetails)
                    rv_quantity!!.adapter = adapter
                    mapp.onCreate(null)
                    mapp.onResume()
                    mapp.getMapAsync(this@JobHistoryDetails)
                }
            }
            Status.ERROR -> {

            }
            else -> {
            }
        }

    }

    override fun onMapReady(googleMap: GoogleMap?) {
        MapsInitializer.initialize(this)
        gmap = googleMap
        gmap!!.setMinZoomPreference(1F)
        if (gmap != null) {
            gmap!!.clear()
            if (latUser.isNotEmpty() && latRestaurant.isNotEmpty()) {
                /*val lstLatLngRoute = ArrayList<LatLng>()
                lstLatLngRoute.add(0, LatLng(latUser.toDouble(), longUser.toDouble()))
                lstLatLngRoute.add(1, LatLng(latRestaurant.toDouble(), longRestaurant.toDouble()))

                for (i in 0 until lstLatLngRoute.size) {
                    val markerIcon: Int
                    if (i == 0) {
                        markerIcon = R.drawable.source
                    } else {
                        markerIcon = R.drawable.destination
                    }
                    createMarker(lstLatLngRoute[i].latitude, lstLatLngRoute[i].longitude, markerIcon)
                }
                gmap!!.addPolyline(PolylineOptions().width(5f).add(LatLng(latUser.toDouble(), longUser.toDouble()), LatLng(latRestaurant.toDouble(), longRestaurant.toDouble())))
                val boundsBuilder = LatLngBounds.Builder()
                for (latLngPoint in lstLatLngRoute) boundsBuilder.include(latLngPoint)
                val routePadding = 50
                val latLngBounds = boundsBuilder.build()
                gmap!!.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, routePadding))
                gmap!!.uiSettings!!.isZoomGesturesEnabled = false*/

                /*val origin = LatLng(latUser.toDouble(), longUser.toDouble())
                val destination = LatLng(latRestaurant.toDouble(), longRestaurant.toDouble())
                DrawRouteMaps.getInstance(this).draw(origin, destination, gmap)
                DrawMarker.getInstance(this)
                    .draw(gmap, origin, R.drawable.source, "Origin Location")
                DrawMarker.getInstance(this)
                    .draw(gmap, destination, R.drawable.destination, "Destination Location")

                val bounds = LatLngBounds.Builder()
                    .include(origin)
                    .include(destination).build()
                val displaySize = Point()
                windowManager.defaultDisplay.getSize(displaySize)
                gmap!!.moveCamera(
                    CameraUpdateFactory.newLatLngBounds(
                        bounds,
                        displaySize.x,
                        200,
                        5
                    )
                )*/
                val cameraPosition = CameraPosition.Builder().target(LatLng(latUser.toDouble(), longUser.toDouble())).zoom(12f).build()
                gmap!!.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
                gmap!!.uiSettings.isZoomGesturesEnabled = false

            }
        }
    }
}