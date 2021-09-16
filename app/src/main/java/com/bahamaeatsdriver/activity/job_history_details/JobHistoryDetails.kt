package com.bahamaeatsdriver.activity.job_history_details

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Point
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bahamaeats.constant.Constants
import com.bahamaeats.network.RestApiInterface
import com.bahamaeats.network.RestObservable
import com.bahamaeats.network.Status
import com.bahamaeatsdriver.Adapter.FoodQuantiytAdapter
import com.bahamaeatsdriver.R
import com.bahamaeatsdriver.activity.Navigation.Contactus_Activity
import com.bahamaeatsdriver.drawroute.DrawMarker
import com.bahamaeatsdriver.drawroute.DrawRouteMaps
import com.bahamaeatsdriver.helper.extensions.launchActivity
import com.bahamaeatsdriver.helper.others.CommonMethods.convertTimeStampToDa
import com.bahamaeatsdriver.helper.others.Helper
import com.bahamaeatsdriver.model_class.job_history_details.JobHistoryyDetailsResponse
import com.bahamaeatsdriver.model_class.map_poliline.Route
import com.bahamaeatsdriver.repository.BaseViewModel
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_job_history_details.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


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
        iv_backArrow_payment.setOnClickListener {
            finish()
        }
        tv_support.setOnClickListener {
            launchActivity<Contactus_Activity>()
        }
        rl_root.setOnClickListener {
        }
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
                    if (liveData.data.body.order.paymentMethod == "1") {
                        tv_paymentMode.text = "Payment method: Suncash"
                    } else if (liveData.data.body.order.paymentMethod == "2") {
                        tv_paymentMode.text = "Payment method: Paypal"
                    } else if (liveData.data.body.order.paymentMethod == "4") {
                        tv_paymentMode.text = "Payment method: Kanoo"
                    } else if (liveData.data.body.order.paymentMethod == "5") {
                        tv_paymentMode.text = "Payment method: Atlantic"
                    } else if (liveData.data.body.order.paymentMethod == "6") {
                        tv_paymentMode.text = "Payment method: IsLand Pay"
                    }else if (liveData.data.body.order.paymentMethod == "7") {
                        tv_paymentMode.text = "Payment method: "+getString(R.string.be_wallet)
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

    protected fun createMarker(latitude: Double, longitude: Double, markerIcon: Int): Marker? {
        return gmap!!.addMarker(MarkerOptions()
                .position(LatLng(latitude, longitude))
                .anchor(0.5f, 0.5f)
                .icon(BitmapDescriptorFactory.fromResource(markerIcon)))
    }

    // todo ------------------------------- Polyline --------------------------------- //
    private var polyLineList: List<LatLng>? = null
    var polylineOptions: PolylineOptions? = null
    var blackPolyline: Polyline? = null
    var greyPolyLine: Polyline? = null
    var blackPolylineOptions: PolylineOptions? = null


    @SuppressLint("CheckResult")
    private fun drawpoliline(pickuplatlng: LatLng, dropofflatlng: LatLng) {
        Log.e("DGSdgsdsg", "safsfa")
        Log.e("DGSdgsdsg", "pickuplatlng==>" + pickuplatlng.latitude)
        Log.e("DGSdgsdsg", "pickuplatlng==>" + pickuplatlng.longitude)
        Log.e("DGSdgsdsg", "dropofflatlng==>" + dropofflatlng.latitude)
        Log.e("DGSdgsdsg", "dropofflatlng==>" + dropofflatlng.longitude)

        val apiInterface: RestApiInterface
        val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl("https://maps.googleapis.com/")
                .build()
        apiInterface = retrofit.create(RestApiInterface::class.java)
        apiInterface.getDirections("driving", "less_driving", pickuplatlng.latitude.toString() + "," + pickuplatlng.longitude, dropofflatlng.latitude.toString() + "," + dropofflatlng.longitude, resources.getString(R.string.google_maps_key)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe {
            val routeList: List<Route> = it.routes
            Log.e("DGsdsgdgsgd", "" + routeList.size)
            for (route in routeList) {
                val polyLine = route.overviewPolyline.points
                polyLineList = decodePoly(polyLine)
                if (greyPolyLine != null) {
                    greyPolyLine!!.remove()
                }
                if (blackPolyline != null) {
                    blackPolyline!!.remove()
                }
                drawPolyLineAndAnimateCar(pickuplatlng, dropofflatlng)
                createZoomRoute(pickuplatlng, dropofflatlng)
            }
        }
    }

    private fun drawPolyLineAndAnimateCar(pickuplatlng: LatLng, dropofflatlng: LatLng) {
        runOnUiThread {
            val builder = LatLngBounds.Builder()
            for (latLng in polyLineList!!) {
                builder.include(latLng)
            }
            polylineOptions = PolylineOptions()
            polylineOptions!!.color(Color.BLACK)
            polylineOptions!!.width(8f)
            polylineOptions!!.startCap(SquareCap())
            polylineOptions!!.endCap(SquareCap())
            polylineOptions!!.jointType(JointType.ROUND)
            polylineOptions!!.addAll(polyLineList)
            greyPolyLine = gmap!!.addPolyline(polylineOptions)
            blackPolylineOptions = PolylineOptions()
            blackPolylineOptions!!.width(8f)
            blackPolylineOptions!!.color(Color.BLACK)
            blackPolylineOptions!!.startCap(SquareCap())
            blackPolylineOptions!!.endCap(SquareCap())
            blackPolylineOptions!!.jointType(JointType.ROUND)
            blackPolyline = gmap!!.addPolyline(blackPolylineOptions)
        }
    }

    fun createZoomRoute(driverLatLong: LatLng, dropLatLong: LatLng) {
        val lstLatLngRoute: MutableList<LatLng> = java.util.ArrayList()
        lstLatLngRoute.add(driverLatLong)
        lstLatLngRoute.add(dropLatLong)
        zoomRoute(lstLatLngRoute)
    }

    private fun zoomRoute(lstLatLngRoute: MutableList<LatLng>) {
        if (gmap == null || lstLatLngRoute == null || lstLatLngRoute.isEmpty()) return
        val boundsBuilder = LatLngBounds.Builder()
        for (latLngPoint in lstLatLngRoute) boundsBuilder.include(latLngPoint)
        val routePadding = 20
        val latLngBounds = boundsBuilder.build()
        gmap!!.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, routePadding))
    }

    private fun decodePoly(encoded: String): List<LatLng> {
        val poly: MutableList<LatLng> = java.util.ArrayList()
        var index = 0
        val len = encoded.length
        var lat = 0
        var lng = 0
        while (index < len) {
            var b: Int
            var shift = 0
            var result = 0
            do {
                b = encoded[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lat += dlat
            shift = 0
            result = 0
            do {
                b = encoded[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lng += dlng
            val p = LatLng(lat.toDouble() / 1E5,
                    lng.toDouble() / 1E5)
            poly.add(p)
        }
        return poly
    }

}