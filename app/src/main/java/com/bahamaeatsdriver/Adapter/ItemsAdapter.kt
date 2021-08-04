package com.bahamaeatsdriver.Adapter

import android.content.Context
import android.graphics.Point
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bahamaeats.constant.Constants.Companion.IMAGE_URL
import com.bahamaeats.constant.Constants.Companion.RESTAURANT_BASE_URL
import com.bahamaeatsdriver.R
import com.bahamaeatsdriver.activity.Deliveries_jobhistory
import com.bahamaeatsdriver.drawroute.DrawMarker
import com.bahamaeatsdriver.drawroute.DrawRouteMaps
import com.bahamaeatsdriver.helper.others.CommonMethods
import com.bahamaeatsdriver.helper.others.Helper
import com.bahamaeatsdriver.model_class.job_history.PastJobHistory
import com.bahamaeatsdriver.model_class.job_history.UpComingJobHistory
import com.bumptech.glide.Glide
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.res_jobhistory.view.*


class ItemsAdapter(var context: Deliveries_jobhistory, var type: String, val pastJobHistory: List<PastJobHistory>,
                   val upComingJobHistory: UpComingJobHistory, val onClickListener: Deliveries_jobhistory)
    : RecyclerView.Adapter<ItemsAdapter.MyViewHolder>(), OnMapReadyCallback {
    var gmap: GoogleMap? = null
    var latUser = ""
    var longUser = ""
    var firstMarker: Marker? = null
    var secondMarker: Marker? = null

    var latRestaurant = ""
    var longRestaurant = ""
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyViewHolder {
        val itemView = LayoutInflater.from(viewGroup.context).inflate(R.layout.res_jobhistory, viewGroup, false)
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

    override fun onBindViewHolder(myViewHolder: MyViewHolder, i: Int) {
        /*AIzaSyDLLJ3Ck9wLZRvHLlvdWnhnD1XpUMkB-go*/



        if (type == "past") {
            latUser = pastJobHistory.get(i).currentLat
            longUser = pastJobHistory.get(i).currentLong
            latRestaurant = pastJobHistory.get(i).fromLat
            longRestaurant = pastJobHistory.get(i).fromLong

            myViewHolder.Tv_time.text = CommonMethods.convertToNewFormat2(pastJobHistory.get(i).order.createdAt)+upComingJobHistory.vehicle
            myViewHolder.itemView.tv_price.text = "$" + Helper.roundOffDecimalNew(pastJobHistory.get(i).order.netAmount.toFloat())
            myViewHolder.itemView.tv_restaurantName.text =pastJobHistory.get(i).restaurant.name
            if (pastJobHistory.get(i).order.orderDetails.size==1)
            myViewHolder.itemView.tv_orders.text =pastJobHistory.get(i).order.orderDetails.size.toString()+" Item"
            else
            myViewHolder.itemView.tv_orders.text =pastJobHistory.get(i).order.orderDetails.size.toString()+" Items"
            Glide.with(context).load(RESTAURANT_BASE_URL +pastJobHistory.get(i).restaurant.image).placeholder(R.drawable.placeholder_circle).into(myViewHolder.itemView.iv_restaurantImage)
            myViewHolder.itemView.ratingBar.rating = pastJobHistory.get(i).rating.toFloat()
            myViewHolder.itemView.tv_Status.text = pastJobHistory.get(i).order.status.toString()
            val userLat = pastJobHistory.get(i).order.latitude
            val userLong = pastJobHistory.get(i).order.longitude
            val resLat = pastJobHistory.get(i).currentLong
            val resLong = pastJobHistory.get(i).currentLat
            val url_ = "https://maps.googleapis.com/maps/api/staticmap?center=" + resLong + "," + resLat + "&zoom=12&size=400x400&key=AIzaSyDLLJ3Ck9wLZRvHLlvdWnhnD1XpUMkB-go"

//            val uriImage = "https://maps.googleapis.com/maps/api/staticmap?center=0xff0000ff|weight:5|fillcolor:0xFFFF0033|" + userLat + "," + userLong + "|" + resLat + "," + resLong + "&zoom=12&size=400x400&key=AIzaSyDLLJ3Ck9wLZRvHLlvdWnhnD1XpUMkB-go"

            myViewHolder.itemView.setOnClickListener {
                onClickListener.onPastJobHistoryClick(i, pastJobHistory.get(i))
            }
        } else {
            latUser = upComingJobHistory.currentLat
            longUser = upComingJobHistory.currentLong
            latRestaurant = upComingJobHistory.fromLat
            longRestaurant = upComingJobHistory.fromLong
//upComingJobHistory.vehicle
            Glide.with(context).load(RESTAURANT_BASE_URL +upComingJobHistory.restaurant.image).placeholder(R.drawable.placeholder_circle).into(myViewHolder.itemView.iv_restaurantImage)
            myViewHolder.Tv_time.text = CommonMethods.convertToNewFormat2(upComingJobHistory.order.createdAt)+upComingJobHistory.vehicle
            myViewHolder.itemView.tv_price.text = "$" + Helper.roundOffDecimalNew(upComingJobHistory.order.netAmount.toFloat())
            myViewHolder.itemView.ratingBar.visibility = View.GONE
            myViewHolder.itemView.ratingBar.rating = upComingJobHistory.rating.toFloat()
            myViewHolder.itemView.tv_Status.text = upComingJobHistory.rideStatus.toString()

            myViewHolder.itemView.tv_restaurantName.text =upComingJobHistory.restaurant.name
            if (upComingJobHistory.order.orderDetails.size==1)
                myViewHolder.itemView.tv_orders.text =upComingJobHistory.order.orderDetails.size.toString()+" Item"
            else
                myViewHolder.itemView.tv_orders.text =upComingJobHistory.order.orderDetails.size.toString()+" Items"
            myViewHolder.itemView.setOnClickListener {
                onClickListener.onUpComingJobHistoryClick(i, upComingJobHistory)
            }

        }

        myViewHolder.itemView.rl_root.setOnClickListener {
            onClickListener.onUpComingJobHistoryClick(i, upComingJobHistory)
        }
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
                map.onCreate(null);
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
//        gmap!!.setMinZoomPreference(1F)
        try {
            if (gmap != null) {
                gmap!!.clear()
              /*  if (!latUser.isEmpty() && !latRestaurant.isEmpty()) {
                    val userLocation = LatLng(latUser.toDouble(), longUser.toDouble())
                    val restauranLocation = LatLng(latRestaurant.toDouble(), longRestaurant.toDouble())
                    val lstLatLngRoute = ArrayList<LatLng>()
                    lstLatLngRoute.add(0, LatLng(latUser.toDouble(), longUser.toDouble()))
                    lstLatLngRoute.add(1, LatLng(latRestaurant.toDouble(), longRestaurant.toDouble()))

                    for (i in 0 until lstLatLngRoute.size) {
                        val markerIcon: Int
                        if (i == 0) {
//                            markerIcon = R.drawable.b_
//                            markerIcon = R.drawable.bb_
                            markerIcon = R.drawable.source
                        } else {
//                            markerIcon = R.drawable.a_
//                            markerIcon = R.drawable.aa_
                            markerIcon = R.drawable.destination
                        }
                        createMarker(lstLatLngRoute[i].latitude, lstLatLngRoute[i].longitude,markerIcon)
                    }
                    gmap!!.addPolyline(PolylineOptions().add(LatLng(latUser.toDouble(), longUser.toDouble()), LatLng(latRestaurant.toDouble(), longRestaurant.toDouble())).color(ContextCompat.getColor(context,R.color.Greenapp)))
                    val boundsBuilder = LatLngBounds.Builder()
                    for (latLngPoint in lstLatLngRoute) boundsBuilder.include(latLngPoint)
                    val routePadding = 40
                    val latLngBounds = boundsBuilder.build()
                    gmap!!.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, routePadding))
                    gmap!!.uiSettings!!.isZoomGesturesEnabled=false
                }*/


                    val origin =LatLng(latUser.toDouble(), longUser.toDouble())
                    val destination = LatLng(latRestaurant.toDouble(), longRestaurant.toDouble())
                    DrawRouteMaps.getInstance(context).draw(origin, destination, gmap)
                    DrawMarker.getInstance(context)
                        .draw(gmap, origin, R.drawable.source, "Origin Location")
                    DrawMarker.getInstance(context)
                        .draw(gmap, destination, R.drawable.destination, "Destination Location")

                    val bounds = LatLngBounds.Builder()
                        .include(origin)
                        .include(destination).build()
                    val displaySize = Point()
                    context.windowManager.defaultDisplay.getSize(displaySize)
                    gmap!!.moveCamera(
                        CameraUpdateFactory.newLatLngBounds(
                            bounds,
                            displaySize.x, 80,
                            5
                        )
                    )
                    gmap!!.uiSettings.isZoomGesturesEnabled = false
            }
        } catch (e: Exception) {
        }
    }

    protected fun createMarker(latitude: Double, longitude: Double, markerIcon: Int): Marker? {
        return gmap!!.addMarker(MarkerOptions()
                .position(LatLng(latitude, longitude))
                .anchor(0.4f, 0.4f)
                .icon(BitmapDescriptorFactory.fromResource(markerIcon)))
    }


}