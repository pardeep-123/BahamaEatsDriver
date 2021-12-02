package com.bahamaeatsdriver.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bahamaeats.constant.Constants.Companion.RESTAURANT_BASE_URL
import com.bahamaeatsdriver.R
import com.bahamaeatsdriver.activity.Deliveries_jobhistory
import com.bahamaeatsdriver.helper.others.CommonMethods
import com.bahamaeatsdriver.helper.others.Helper
import com.bahamaeatsdriver.model_class.job_history.PastJobHistory
import com.bahamaeatsdriver.model_class.job_history.UpComingJobHistory
import com.bumptech.glide.Glide
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.res_jobhistory.view.*


class ItemsAdapter(
    var context: Deliveries_jobhistory, var type: String, val pastJobHistory: List<PastJobHistory>,
    val upComingJobHistory: UpComingJobHistory, val onClickListener: Deliveries_jobhistory
) : RecyclerView.Adapter<ItemsAdapter.MyViewHolder>(), OnMapReadyCallback {
    private var gmap: GoogleMap? = null
    private var latUser = ""
    private var longUser = ""
    private var latRestaurant = ""
    private var longRestaurant = ""
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyViewHolder {
        val itemView = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.res_jobhistory, viewGroup, false)
        return MyViewHolder(itemView)
    }

    /***
     * type=past for :=>pastJobHistory: List<PastJobHistory>
     * type=upcoming  :=>upComingJobHistory: UpComingJobHistory
     */
    override fun getItemCount(): Int {
        return if (type == "past") {
            pastJobHistory.size
        } else {
            1
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(myViewHolder: MyViewHolder, i: Int) {
        if (type == "past") {
            latUser = pastJobHistory[i].currentLat
            longUser = pastJobHistory[i].currentLong
            latRestaurant = pastJobHistory[i].fromLat
            longRestaurant = pastJobHistory[i].fromLong

            myViewHolder.Tv_time.text = CommonMethods.convertToNewFormat2(pastJobHistory[i].order.createdAt) + upComingJobHistory.vehicle
            if(pastJobHistory[i].order.driverNetAmount.isNotEmpty())
            myViewHolder.itemView.tv_price.text = "$" + Helper.roundOffDecimalNew(pastJobHistory[i].order.driverNetAmount.toFloat())
            else
            myViewHolder.itemView.tv_price.text = "$" + Helper.roundOffDecimalNew(pastJobHistory[i].order.netAmount.toFloat())

            myViewHolder.itemView.tv_restaurantName.text = pastJobHistory[i].restaurant.name
            if (pastJobHistory[i].order.orderDetails.size == 1)
                myViewHolder.itemView.tv_orders.text = pastJobHistory[i].order.orderDetails.size.toString() + " Item"
            else
                myViewHolder.itemView.tv_orders.text = pastJobHistory[i].order.orderDetails.size.toString() + " Items"
            Glide.with(context).load(RESTAURANT_BASE_URL + pastJobHistory[i].restaurant.image).placeholder(R.drawable.placeholder_circle).into(myViewHolder.itemView.iv_restaurantImage)
            myViewHolder.itemView.ratingBar.rating = pastJobHistory[i].rating.toFloat()
            myViewHolder.itemView.tv_Status.text = pastJobHistory[i].order.status.toString()
            myViewHolder.itemView.setOnClickListener { onClickListener.onPastJobHistoryClick(i,
                pastJobHistory[i]
            ) }
        } else {
            latUser = upComingJobHistory.currentLat
            longUser = upComingJobHistory.currentLong
            latRestaurant = upComingJobHistory.fromLat
            longRestaurant = upComingJobHistory.fromLong
            Glide.with(context).load(RESTAURANT_BASE_URL + upComingJobHistory.restaurant.image).placeholder(R.drawable.placeholder_circle).into(myViewHolder.itemView.iv_restaurantImage)
            myViewHolder.Tv_time.text = CommonMethods.convertToNewFormat2(upComingJobHistory.order.createdAt) + upComingJobHistory.vehicle
//            myViewHolder.itemView.tv_price.text = "$" + Helper.roundOffDecimalNew(upComingJobHistory.order.netAmount.toFloat())
            myViewHolder.itemView.tv_price.text = "$" + if (upComingJobHistory.order.driverNetAmount.isNotEmpty()) Helper.roundOffDecimalNew(upComingJobHistory.order.driverNetAmount.toFloat()) else Helper.roundOffDecimalNew(upComingJobHistory.order.netAmount.toFloat())
            myViewHolder.itemView.ratingBar.visibility = View.GONE
            myViewHolder.itemView.ratingBar.rating = upComingJobHistory.rating.toFloat()
            myViewHolder.itemView.tv_Status.text = upComingJobHistory.rideStatus.toString()
            myViewHolder.itemView.tv_restaurantName.text = upComingJobHistory.restaurant.name

            if (upComingJobHistory.order.orderDetails.size == 1)
                myViewHolder.itemView.tv_orders.text = upComingJobHistory.order.orderDetails.size.toString() + " Item"
            else
                myViewHolder.itemView.tv_orders.text = upComingJobHistory.order.orderDetails.size.toString() + " Items"
            myViewHolder.itemView.setOnClickListener { onClickListener.onUpComingJobHistoryClick(i, upComingJobHistory)
            }
        }

        myViewHolder.itemView.rl_root.setOnClickListener { onClickListener.onUpComingJobHistoryClick(i, upComingJobHistory) }
    }


    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tv_Status: TextView
        var Tv_time: TextView
        var ratingBar: RatingBar
        var map: MapView

        init {
            tv_Status = itemView.findViewById(R.id.tv_Status)
            ratingBar = itemView.findViewById(R.id.ratingBar)
            Tv_time = itemView.findViewById(R.id.Tv_time)
            map = itemView.findViewById(R.id.mapView)

            if (map != null) {
                map.onCreate(null)
                map.onResume()
                map.getMapAsync(this@ItemsAdapter)
            }
        }
    }

    override fun onViewRecycled(holder: MyViewHolder) {
    }

    override fun onMapReady(googleMap: GoogleMap?) {

        MapsInitializer.initialize(onClickListener)
        gmap = googleMap
        try {
            if (gmap != null) {
                gmap!!.clear()
                val origin = LatLng(latUser.toDouble(), longUser.toDouble())
                val destination = LatLng(latRestaurant.toDouble(), longRestaurant.toDouble())
               /* DrawRouteMaps.getInstance(context).draw(origin, destination, gmap)
                val bounds = LatLngBounds.Builder().include(origin).include(destination).build()
                val displaySize = Point()
                context.windowManager.defaultDisplay.getSize(displaySize)
                gmap!!.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, displaySize.x, 180, 5))*/

                val cameraPosition = CameraPosition.Builder().target(origin).zoom(12f).build()
//                gmap!!.addMarker(MarkerOptions().position(origin).icon(BitmapDescriptorFactory.fromResource(R.drawable.source)))
                gmap!!.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
                gmap!!.uiSettings.isZoomGesturesEnabled = false
//                DrawMarker.getInstance(context).draw(gmap, origin, R.drawable.source, "Origin Location")
//                DrawMarker.getInstance(context).draw(gmap, destination, R.drawable.destination, "Destination Location")
            }
        } catch (e: Exception) {
        }
    }

}