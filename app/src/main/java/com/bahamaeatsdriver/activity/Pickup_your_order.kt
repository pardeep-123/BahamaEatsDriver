package com.bahamaeatsdriver.activity

import android.Manifest
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity
import com.bahamaeatsdriver.R
import com.bahamaeatsdriver.activity.Navigation.Contactus_Activity
import com.bahamaeatsdriver.activity.Navigation.Settings_Activity
import com.bahamaeatsdriver.activity.Navigation.TermAnd_Conditions
import com.bahamaeatsdriver.activity.Pofile.My_Profile_Activity
import com.bahamaeatsdriver.activity.login_register.Login_Activity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_pickup_your_order.*
import kotlinx.android.synthetic.main.fragment_navigation_drawl.*
import kotlinx.android.synthetic.main.order_details_dailog.*

class Pickup_your_order : FragmentActivity()/*, OnMapReadyCallback, View.OnClickListener*/ {

    //    lateinit var drawerLayout: DrawerLayout
    var currentLocation: Location? = null
    var fusedLocationProviderClient: FusedLocationProviderClient? = null
    var Navclick = ""
    var builder: AlertDialog.Builder? = null
    var temp = 0
    var phoneDriver: String? = ""
    lateinit var dialogOrderDeatail: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pickup_your_order)
//        iv_drawable.setOnClickListener(this)
//        Button_cancle.setOnClickListener(this)
//        Relativ_currentloc.setOnClickListener(this)
//        builder = AlertDialog.Builder(this)
//
//        Relative_offlineN.setOnClickListener(this)
//        Relative_OnlineN.setOnClickListener(this)
//        LL_deliveries.setOnClickListener(this)
//        LL_paymentstatus.setOnClickListener(this)
//
//        homelayout.setOnClickListener(this)
//        LL_support.setOnClickListener(this)
//        LL_TandC.setOnClickListener(this)
//        LL_settings.setOnClickListener(this)
//        LL_logout.setOnClickListener(this)
//        tv_orderDetails.setOnClickListener(this)
//        Relativ_profile.setOnClickListener(this)
//        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
//        fetchLocation()
////        drawerLayout = findViewById(R.id.drawerLayout)
////        Button_cancle = findViewById(R.id.Button_cancle)


    }
//
//    fun Button_Starttrip(view: View?) {
//        tv_adress.setCompoundDrawablesWithIntrinsicBounds(R.drawable.subway, 0, 0, 0)
//        Iv_search.visibility = View.GONE
//        Button_imhear.visibility = View.VISIBLE
//        Button_COMPLETEtrip.visibility = View.VISIBLE
//        Button_Starttrip.visibility = View.GONE
//        tv_head.text = "On Trip"
//    }
//
//
//    fun Button_COMPLETEtrip(view: View?) {
//        finish()
//    }
//
//    private fun fetchLocation() {
//        if (ActivityCompat.checkSelfPermission(
//                        this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                        this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION
//            ), REQUEST_CODE)
//            return
//        }
//        val task = fusedLocationProviderClient!!.lastLocation
//        task.addOnSuccessListener { location ->
//            if (location != null) {
//                currentLocation = location
//                val supportMapFragment = (supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?)!!
//                supportMapFragment.getMapAsync(this@Pickup_your_order)
//            }
//        }
//    }
//
//    override fun onMapReady(googleMap: GoogleMap) {
//        val latLng = LatLng(currentLocation!!.latitude, currentLocation!!.longitude)
//        val markerOptions = MarkerOptions().position(latLng).title("I am here!")
//        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng))
//        if (Navclick == "focus") {
//            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18f))
//        } else {
//            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10f))
//        }
//        googleMap.addMarker(markerOptions)
//    }
//
//    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
//        when (requestCode) {
//            REQUEST_CODE -> if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                fetchLocation()
//            }
//        }
//    }
//
//    companion object {
//        private const val REQUEST_CODE = 101
//    }
//
//    override fun onClick(p0: View?) {
//        val itemid = p0!!.id
//        when (itemid) {
//            R.id.tv_orderDetails -> {
//                dialogOrderDeatail = Dialog(this@Pickup_your_order)
//                dialogOrderDeatail.window!!.setBackgroundDrawableResource(android.R.color.transparent)
//                dialogOrderDeatail.setContentView(R.layout.order_details_dailog)
//                dialogOrderDeatail.setCancelable(false)
//                dialogOrderDeatail.btn_ok.setOnClickListener({
//                    dialogOrderDeatail.dismiss()
//                })
//                dialogOrderDeatail.show()
//            }
//            R.id.iv_drawable -> {
//                openCloseDrawer()
//            }
//            R.id.Button_cancle -> {
//                finish()
//            }
//            R.id.Relativ_currentloc -> {
//                Navclick = "focus"
//                fetchLocation()
//            }
//            R.id.Relative_offlineN -> {
//                Relative_OnlineN!!.setVisibility(View.VISIBLE)
//                Relative_offlineN!!.setVisibility(View.GONE)
//            }
//            R.id.Relative_OnlineN -> {
//                Relative_offlineN!!.setVisibility(View.VISIBLE)
//                Relative_OnlineN!!.setVisibility(View.GONE)
//            }
//            R.id.LL_deliveries -> {
//                temp = 1
//                startActivity(Intent(this, Deliveries_jobhistory::class.java))
//            }
//
//            R.id.LL_paymentstatus -> {
//                temp = 1
//                startActivity(Intent(this, Payment_Status::class.java))
//            }
//            R.id.homelayout -> {
//                temp = 0
//                startActivity(Intent(this, Home_Page::class.java))
//            }
//            R.id.LL_support -> {
//                temp = 1
//                startActivity(Intent(this, Contactus_Activity::class.java))
//            }
//            R.id.LL_TandC -> {
//                temp = 1
//                startActivity(Intent(this, TermAnd_Conditions::class.java))
//            }
//            R.id.LL_settings -> {
//                temp = 1
//                startActivity(Intent(this, Settings_Activity::class.java))
//            }
//            R.id.Relativ_profile -> {
//                temp = 1
//                startActivity(Intent(this, My_Profile_Activity::class.java))
//            }
//            R.id.LL_logout -> {
//                temp = 1
//                builder!!.setMessage("Logout").setTitle("Logout")
//
//                //Setting message manually and performing action on button click
//                builder!!.setMessage("Are you sure you want to logout?")
//                        .setCancelable(false)
//                        .setPositiveButton("Yes") { dialog, id -> startActivity(Intent(this, Login_Activity::class.java)) }
//                        .setNegativeButton("No") { dialog, id -> //  Action for 'NO' Button
//                            dialog.cancel()
//                        }
//                //Creating dialog box
//                val alert = builder!!.create()
//                //Setting the title manually
//                alert.show()
//            }
//        }
//    }
//
//    fun openCloseDrawer() {
//        if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
//            drawerLayout.closeDrawer(Gravity.LEFT)
//        } else {
//            drawerLayout.openDrawer(Gravity.LEFT)
//        }
//    }
//
//    fun phoneCall() {
//        try {
//            if (!phoneDriver!!.isEmpty()) {
////                val callIntent = Intent(Intent.ACTION_CALL)
////                callIntent.data = Uri.parse("tel:$phoneDriver")
////                startActivity(callIntent)
//            }
//        } catch (activityException: ActivityNotFoundException) {
//            Log.e("Calling a Phone Number", "Call failed", activityException)
//        }
//    }

}