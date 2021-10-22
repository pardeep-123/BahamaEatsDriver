package com.bahamaeatsdriver.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.app.Dialog
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.*
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bahamaeats.constant.Constants
import com.bahamaeats.constant.Constants.Companion.ACCEPT_RIDE_STATUS
import com.bahamaeats.constant.Constants.Companion.IMAGE_URL
import com.bahamaeats.constant.Constants.Companion.REJECT_RIDE_STATUS
import com.bahamaeats.network.RestApiInterface
import com.bahamaeats.network.RestObservable
import com.bahamaeats.network.Status
import com.bahamaeatsdriver.adapter.OrderDetailsQuantiytAdapter
import com.bahamaeatsdriver.adapter.PhotoAdapter
import com.bahamaeatsdriver.R
import com.bahamaeatsdriver.activity.Navigation.Contactus_Activity
import com.bahamaeatsdriver.activity.Navigation.SettingsActivity
import com.bahamaeatsdriver.activity.Navigation.TermAnd_Conditions
import com.bahamaeatsdriver.activity.driver_availability.DriverAvailability
import com.bahamaeatsdriver.activity.driver_profile.My_Profile_Activity
import com.bahamaeatsdriver.activity.login_register.Login_Activity
import com.bahamaeatsdriver.activity.notification_listing.NotificationActivity
import com.bahamaeatsdriver.di.App
import com.bahamaeatsdriver.helper.extensions.clearPrefrences
import com.bahamaeatsdriver.helper.extensions.getprefObject
import com.bahamaeatsdriver.helper.extensions.launchActivity
import com.bahamaeatsdriver.helper.others.CommonMethods
import com.bahamaeatsdriver.helper.others.CommonMethods.convertToNewFormat5
import com.bahamaeatsdriver.helper.others.Helper
import com.bahamaeatsdriver.location.CheckLocationActivity
import com.bahamaeatsdriver.model_class.ImageVideoModel
import com.bahamaeatsdriver.model_class.accept_reject_ride.AcceptRejectRideRequest
import com.bahamaeatsdriver.model_class.add_on_list.AddOnsCustomModel
import com.bahamaeatsdriver.model_class.change_ride_status.ChangeRideStatusResponse
import com.bahamaeatsdriver.model_class.get_current_ride.Body
import com.bahamaeatsdriver.model_class.get_current_ride.GetCurrentRideResponse
import com.bahamaeatsdriver.model_class.get_take_driver_orderstatus.GetTakeDriverOrderStatus
import com.bahamaeatsdriver.model_class.get_take_driver_orderstatus.SelectedSlot
import com.bahamaeatsdriver.model_class.i_am_here.IAmHereResponse
import com.bahamaeatsdriver.model_class.login.LoginResponse
import com.bahamaeatsdriver.model_class.logout.LogoutResponse
import com.bahamaeatsdriver.model_class.map_poliline.Route
import com.bahamaeatsdriver.model_class.training_video_links.TrainingVideoLinksResponse
import com.bahamaeatsdriver.model_class.update_driver_online_status.UpdateDriverTakeOrderStatus
import com.bahamaeatsdriver.model_class.update_latitudeLongitude.UpdateDriverLatLongResponse
import com.bahamaeatsdriver.model_class.upload_receipt.UploadReceiptResponse
import com.bahamaeatsdriver.repository.BaseViewModel
import com.bahamaeatsdriver.services.SensorService
import com.bahamaeatsdriver.socket.SocketManager
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.yanzhenjie.album.Album
import com.yanzhenjie.album.AlbumFile
import com.yanzhenjie.album.api.widget.Widget
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_home_page.*
import kotlinx.android.synthetic.main.current_job_layout.*
import kotlinx.android.synthetic.main.current_job_layout.tv_uploadReceipt
import kotlinx.android.synthetic.main.fragment_navigation_drawl.*
import kotlinx.android.synthetic.main.messenger_popup_layout.*
import kotlinx.android.synthetic.main.notification_on_alert.*
import kotlinx.android.synthetic.main.order_details_dailog.tv_paymentMode
import kotlinx.android.synthetic.main.order_details_dailog_new.*
import kotlinx.android.synthetic.main.order_details_dailog_new.btn_ok
import kotlinx.android.synthetic.main.res_pickup_request.*
import kotlinx.android.synthetic.main.res_pickup_request.tv_totalAmount
import kotlinx.android.synthetic.main.upload_receipt_dialog_layout.*
import org.joda.time.Duration
import org.joda.time.format.DateTimeFormat
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin


class Home_Page : CheckLocationActivity(), OnMapReadyCallback, View.OnClickListener,
    SocketManager.Observer, Observer<RestObservable> {

    private lateinit var socketManager: SocketManager
    private var btnAcceptRide: TextView? = null
    private var btnRejectRide: TextView? = null
    private var btnOrderDetail: TextView? = null
    private lateinit var dialog: Dialog
    private lateinit var dialogOrderDeatail: Dialog
    private var builder: AlertDialog.Builder? = null
    private var temp = 0
    private var rideRequestId = ""
    private var isResuarantBePaymentAvailable = ""
    private var mLatitute = ""
    private var mLongitute = ""
    private var currentRideData: Body? = null
    private var changeRideStatus: com.bahamaeatsdriver.model_class.change_ride_status.Body? = null
    private val viewModel: BaseViewModel by lazy { ViewModelProvider(this).get(BaseViewModel::class.java) }
    private lateinit var driverDetails: LoginResponse
    private var mapFragment: SupportMapFragment? = null
    private var mBroadcastReceiver: BroadcastReceiver? = null
    private var googleMap: GoogleMap? = null
    private var latRestaurant = ""
    private var longRestaurant = ""
    private var latRestaurantAcceptJob = ""
    private var longRestaurantAcceptJob = ""
    private var anyActiveJobAvailable = 0
    private var loadMapOf = 0
    private var type = 0
    private lateinit var notificationOnDialog: Dialog
    private lateinit var uploadReceiptDialog: Dialog
    private lateinit var currentJobOptiondialog: Dialog
    private var isCall = "false"
    private var startlat: Double? = null
    private var startlong: Double? = null
    private var finishlat: Double? = null
    private var finishlong: Double? = null
    private var runningdriver = true
    private var driverMarker: Marker? = null
    private var dropMarker: Marker? = null
    private var countDownTimer: CountDownTimer? = null
    private var image_path = ""
    private var isReceiptUpload = 0
    private var takeOrderStatusLocal = ""
    private lateinit var relativeOnline: RelativeLayout
    private lateinit var relativeOffline: RelativeLayout
    private var videoUrl = ""
    private var mAlbumFiles = ArrayList<AlbumFile>()
    private var todaySlotsListing = mutableListOf<SelectedSlot>()
    private lateinit var photoAdapter: PhotoAdapter
    private var arrayImageVideo = ArrayList<ImageVideoModel>()

    companion object {
        var loginDiverId = ""
        var otherUserId = ""
    }

    private var mSensorService: SensorService? = null

    override fun onPermissionGranted() {
    }

    /***
     * Get Driver Current location
     */
    override fun onLocationGet(latitude: String?, longitude: String?) {
        startlat = latitude!!.toDouble()
        startlong = longitude!!.toDouble()
        mLatitute = latitude.toString()
        mLongitute = longitude.toString()
        if (mLatitute.isNotEmpty()) {
            /***
             * Update Location on server
             */
            viewModel.updateDriverLatLongApi(this, mLatitute, mLongitute, false)
            viewModel.getUpdateDriverLatLongResponse().observe(this, this)
            if (googleMap != null) {
                if (mLatitute.isNotEmpty()) {
                    if (driverMarker == null)
                        driverMarker = setMarkerdate(mLatitute.toDouble(), mLongitute.toDouble())
                } else {
                    checkPermissionLocation(this)
                }
            }
        }
        if (finishlat != null && finishlong != null && finishlat != 0.0 && finishlong != 0.0 && startlat != 0.0 && startlong != 0.0) {
            drawpoliline(LatLng(startlat!!, startlong!!), LatLng(finishlat!!, finishlong!!), "")
        }
    }

    override fun onStop() {
        super.onStop()
        socketManager.unRegister(this)
    }


    private fun updateDriverOnlineOfflineStatus(isTakeOrderStatus: String) {
        var isSelectExits = false
        val currentTime = SimpleDateFormat("HH:mm").format(Date())
        val onlineOfflineStatus =
            if (isTakeOrderStatus == "0") "Please unselect the slot to be offline" else "Please add slot to be online"
        if (todaySlotsListing.isNotEmpty()) {
            /*  for (j in todaySlotsListing.indices) {
                  if (todaySlotsListing[j].isSelected == 1) {
                      isSelectExits=true
                  }
              }*/
            for (i in todaySlotsListing.indices) {
                if (todaySlotsListing[i].isSelected == 0) {
                    if (currentTime > todaySlotsListing[i].openTime && currentTime < todaySlotsListing[i].closeTime) {
                        /*if (isSelectExits)
                        gotToAlertFuncation(onlineOfflineStatus, isTakeOrderStatus)
                        else
                        {
                        }*/
                        gotToAlertFuncation(onlineOfflineStatus, isTakeOrderStatus)
                    }
                } else {
                    if (currentTime > todaySlotsListing[i].openTime && currentTime < todaySlotsListing[i].closeTime) {
                        gotToAlertFuncation(onlineOfflineStatus, isTakeOrderStatus)
                    }

                }
            }
        } else {
            gotToAlertFuncation(onlineOfflineStatus, isTakeOrderStatus)
        }
//        viewModel.updateDriverOnlineStatusResposneApi(this, isTakeOrderStatus, true)
//        viewModel.updateDriverOnlineStatusResposne().observe(this, this)
    }

    private fun gotToAlertFuncation(onlineOfflineStatus: String, isTakeOrderStatus: String) {
//        if (isTakeOrderStatus == takeOrderStatusLocal) {
//            viewModel.updateDriverOnlineStatusResposneApi(this, isTakeOrderStatus, true)
//            viewModel.updateDriverOnlineStatusResposne().observe(this, this)
//        } else {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(onlineOfflineStatus).setCancelable(false)
            .setPositiveButton(getString(R.string.go_to_slot_page)) { _, _ ->
                launchActivity<DriverAvailability>()
//                    val initData = Intent(this, DriverAvailability::class.java)
//                    startActivityForResult(initData, uploadSlotcode)
            }
            .setNegativeButton(getString(R.string.cancel)) { dialog, _ -> dialog.cancel() }
        builder.create().show()
//        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        socketManager = App.getSocketManager()!!
        socketManager.onRegister(this)
        socketManager.getDriverTakeOrderStatus()
        checkPermissionLocation(this)
        dialog = Dialog(this)
        uploadReceiptDialog = Dialog(this@Home_Page)
        uploadReceiptDialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        uploadReceiptDialog.setContentView(R.layout.upload_receipt_dialog_layout)
        relativeOnline = findViewById(R.id.rl_online)
        relativeOffline = findViewById(R.id.rl_offline)
        currentJobOptiondialog = Dialog(this)
        notificationOnDialog = Dialog(this)
        builder = AlertDialog.Builder(this)
        iv_drawable.setOnClickListener(this)
        Relativ_moveToMapApp.setOnClickListener(this)
        relativ_livlocation.setOnClickListener(this)
        Relativ_currentloc.setOnClickListener(this)
        LL_deliveries.setOnClickListener(this)
        LL_paymentstatus.setOnClickListener(this)
        homelayout.setOnClickListener(this)
        LL_support.setOnClickListener(this)
        LL_notification.setOnClickListener(this)
        LL_TandC.setOnClickListener(this)
        LL_settings.setOnClickListener(this)
        ll_trainingVideo.setOnClickListener(this)
        LL_logout.setOnClickListener(this)
        Relativ_profile.setOnClickListener(this)
        Button_Starttrip.setOnClickListener(this)
        rl_rootLayoutNew.setOnClickListener(this)
        Button_cancle.setOnClickListener(this)
        Button_COMPLETEtrip.setOnClickListener(this)
        Button_imhear.setOnClickListener(this)
        iv_message.setOnClickListener(this)
        tv_uploadReceipt.setOnClickListener(this)
        iv_whatsApp.setOnClickListener(this)
        ll_call.setOnClickListener(this)
        tv_currentOrderDetails.setOnClickListener(this)

        relativeOnline.setOnClickListener { updateDriverOnlineOfflineStatus("0") }
        relativeOffline.setOnClickListener { updateDriverOnlineOfflineStatus("1") }
        mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment!!.getMapAsync { mapFragment!!.getMapAsync(this) }
        val timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                isCall = "true"
            }
        }, 0, 30000)
        mBroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                when (intent.action) {
                    "msg" -> if (Helper.isNetworkConnected(this@Home_Page)) {
                        if (!(context as Activity).isFinishing) {
                            if (intent.getStringExtra("type")!! == "3")
                                currentRideApiCall()
                            else if (intent.getStringExtra("type")!! == "16"){
//                                val status=intent.getStringExtra("take_order_status")!!
//                                updateDriverTakeOrderView(status.toInt())
                                val c1 = Calendar.getInstance()
                                //first day of week
                                c1[Calendar.DAY_OF_WEEK] = 1
                                //last day of week
                                c1[Calendar.DAY_OF_WEEK] = 7
                                val d = Date()
                                getDriverTakeStatusApicall(d.day.toString())
                            }

                        }
                    } else
                        Toast.makeText(
                            context,
                            "Please check your internet connection",
                            Toast.LENGTH_LONG
                        ).show()
                }
            }
        }
        val filter = IntentFilter("msg")
        registerReceiver(mBroadcastReceiver, filter)
    }

    private fun currentRideApiCall() {
        //rideStatus=  //1=>New 2=>Started 3=>Completed 4=>Cancelled */
        viewModel.currentRideApi(this@Home_Page, false)
        viewModel.getCurrentRideResposne().observe(this@Home_Page, this@Home_Page)
    }

    private fun startStep3(activity: Activity) {
        //And it will be keep running until you close the entire application from task manager.
        //This method will executed only once.
        mSensorService = SensorService(activity)
        val mServiceIntent = Intent(activity, SensorService::class.java)
        if (!isMyServiceRunning(activity, SensorService::class.java)) {
            // startService(mServiceIntent);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(mServiceIntent)
            } else {
                startService(mServiceIntent)
            }
        }
    }

    //***********************location background service code***************************//
    private fun isMyServiceRunning(activity: Activity, serviceClass: Class<*>): Boolean {
        val manager = activity.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                Log.d("TAG?", true.toString() + "isMyServiceRunning")
                return true
            }
        }
        Log.d("TAG?", false.toString() + "isMyServiceRunning")
        return false
    }


    @SuppressLint("SetTextI18n")
    override fun onResume() {
        super.onResume()
        val c1 = Calendar.getInstance()
        //first day of week
        c1[Calendar.DAY_OF_WEEK] = 1
        //last day of week
        c1[Calendar.DAY_OF_WEEK] = 7
        val d = Date()
        getDriverTakeStatusApicall(d.day.toString())
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            checkPermissionLocation(this)
            return
        } else {
            currentRideApiCall()
            driverDetails = getprefObject(Constants.DRIVER_DETAILS)
            loginDiverId = driverDetails.body.id.toString()
            if (driverDetails.body.image.contains("http"))
                Glide.with(this).load(driverDetails.body.image).placeholder(R.drawable.profileimage)
                    .into(iv_Profile_image)
            else
                Glide.with(this).load(IMAGE_URL + driverDetails.body.image)
                    .placeholder(R.drawable.profileimage).into(iv_Profile_image)

            if (driverDetails.body.fullName.isNotEmpty())
                tv_driverName.text = driverDetails.body.fullName
            else
                tv_driverName.text =
                    driverDetails.body.firstName + " " + driverDetails.body.lastName
            checkNotificationsPermissionIsEnable()
        }
    }

    private fun getDriverTakeStatusApicall(dayId: String) {
        viewModel.getDriverTakeStatusApi(this, dayId, false)
        viewModel.getDriverTakeStatusResponse().observe(this, this)
    }

    private fun checkNotificationsPermissionIsEnable() {
        if (driverDetails.body.isNotification == 0) {
            if (notificationOnDialog != null && notificationOnDialog.isShowing) {
                Log.e("notificationOnDialog:", "Already showing")
            } else {
                Log.e("notificationOnDialog:", "else:  " + "Already showing")
                notificationOnDialog = Dialog(this, R.style.Theme_Dialog)
                notificationOnDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                notificationOnDialog.setContentView(R.layout.notification_on_alert)
                notificationOnDialog.window!!.setLayout(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT
                )
                notificationOnDialog.setCancelable(false)
                notificationOnDialog.setCanceledOnTouchOutside(false)
                notificationOnDialog.window!!.setGravity(Gravity.CENTER)
                notificationOnDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                notificationOnDialog.tv_allow.setOnClickListener {
                    notificationOnDialog.dismiss()
                    launchActivity<SettingsActivity>()
                }

                notificationOnDialog.show()
            }
        }
    }

    override fun onMapReady(googleMap1: GoogleMap) {
        googleMap = googleMap1
        if (loadMapOf == 0) {
            if (googleMap != null) {
                googleMap!!.clear()
                if (mLatitute.isNotEmpty())
                    driverMarker = setMarkerdate(mLatitute.toDouble(), mLongitute.toDouble())
            }
        } else {
            if (googleMap != null) {
                googleMap!!.clear()
                if (latRestaurant.isNotEmpty() && longRestaurant.isNotEmpty()) {
                    val sydney = LatLng(latRestaurant.toDouble(), longRestaurant.toDouble())
                    val cameraPosition = CameraPosition.Builder().target(sydney).zoom(12f).build()
                    googleMap!!.addMarker(
                        MarkerOptions().position(sydney).title("Restaurant")
                            .icon(BitmapDescriptorFactory.defaultMarker())
                    )
                    googleMap!!.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
                    loadMapOf = 0
                }
            }
        }
        googleMap!!.isTrafficEnabled = false
        googleMap!!.isIndoorEnabled = false
        googleMap!!.isBuildingsEnabled = true
        googleMap!!.uiSettings.isZoomControlsEnabled = true
    }

    private fun createDestinationMarker(
        latitude: Double,
        longitude: Double,
        iconResID: Int,
        title: String?
    ): Marker? {
        var resizeBitmap = BitmapFactory.decodeResource(resources, iconResID)
        resizeBitmap = Bitmap.createScaledBitmap(resizeBitmap!!, 60, 50, true)
        return googleMap!!.addMarker(
            MarkerOptions().position(LatLng(latitude, longitude)).anchor(0.5f, 0.5f).title(title)
                .icon(BitmapDescriptorFactory.fromBitmap(resizeBitmap))
        )
    }

    private fun createMarker(
        latitude: Double,
        longitude: Double,
        iconResID: Int,
        title: String?
    ): Marker? {
        var resizeBitmap = BitmapFactory.decodeResource(resources, iconResID)
        resizeBitmap = Bitmap.createScaledBitmap(resizeBitmap!!, 50, 40, true)
        return googleMap!!.addMarker(
            MarkerOptions().position(LatLng(latitude, longitude)).anchor(0.5f, 0.5f).title(title)
                .icon(BitmapDescriptorFactory.fromBitmap(resizeBitmap))
        )
    }

    private fun setMarkerdate(LATITUDE: Double, LONGITUDE: Double): Marker? {
        val mMarker: Marker? = createMarker(LATITUDE, LONGITUDE, R.drawable.car_marker, "You")
        //first maker camera focuse
        val latLng = LatLng(LATITUDE, LONGITUDE)
        val cameraPosition = CameraPosition.fromLatLngZoom(latLng, 15.0f)
        val cu = CameraUpdateFactory.newCameraPosition(cameraPosition)
        googleMap!!.animateCamera(cu)
        return mMarker
    }

    // todo ------------------------------- Polyline --------------------------------- //
    private var polyLineList: List<LatLng>? = null
    private var polylineOptions: PolylineOptions? = null
    private var blackPolyline: Polyline? = null
    private var greyPolyLine: Polyline? = null
    private var blackPolylineOptions: PolylineOptions? = null

    @SuppressLint("CheckResult")
    private fun drawpoliline(pickuplatlng: LatLng, dropofflatlng: LatLng, dropTitle: String) {
        Log.e("DGSdgsdsg", "safsfa")
        Log.e("DGSdgsdsg", "pickuplatlng==>" + pickuplatlng.latitude)
        Log.e("DGSdgsdsg", "pickuplatlng==>" + pickuplatlng.longitude)
        Log.e("DGSdgsdsg", "dropofflatlng==>" + dropofflatlng.latitude)
        Log.e("DGSdgsdsg", "dropofflatlng==>" + dropofflatlng.longitude)
        val apiInterface: RestApiInterface
        val retrofit = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl("https://maps.googleapis.com/").build()
        apiInterface = retrofit.create(RestApiInterface::class.java)
        apiInterface.getDirections(
            "driving",
            "less_driving",
            pickuplatlng.latitude.toString() + "," + pickuplatlng.longitude,
            dropofflatlng.latitude.toString() + "," + dropofflatlng.longitude,
            resources.getString(R.string.google_maps_key)
        ).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe {
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
                drawPolyLineAndAnimateCar()
                createZoomRoute(pickuplatlng, dropofflatlng)
            }
        }
    }

    fun setDropMaker(dropTitle: String) {
        if (dropMarker != null) {
            dropMarker!!.remove()
            dropMarker = null
        }
        dropMarker = googleMap!!.addMarker(
            MarkerOptions().position(
                LatLng(
                    finishlat!!.toDouble(),
                    finishlong!!.toDouble()
                )
            ).title(dropTitle).icon(BitmapDescriptorFactory.fromResource(R.drawable.source))
        )
    }

    private fun drawPolyLineAndAnimateCar() {
        runOnUiThread {
            runningdriver = false
            val builder = LatLngBounds.Builder()
            for (latLng in polyLineList!!) {
                builder.include(latLng)
            }
            polylineOptions = PolylineOptions()
            polylineOptions!!.color(Color.BLACK)
            polylineOptions!!.width(9f)
            polylineOptions!!.startCap(SquareCap())
            polylineOptions!!.endCap(SquareCap())
            polylineOptions!!.jointType(JointType.ROUND)
            polylineOptions!!.addAll(polyLineList!!)
            greyPolyLine = googleMap!!.addPolyline(polylineOptions!!)
            blackPolylineOptions = PolylineOptions()
            blackPolylineOptions!!.width(9f)
            blackPolylineOptions!!.color(Color.BLACK)
            blackPolylineOptions!!.startCap(SquareCap())
            blackPolylineOptions!!.endCap(SquareCap())
            blackPolylineOptions!!.jointType(JointType.ROUND)
            blackPolyline = googleMap!!.addPolyline(blackPolylineOptions!!)
            runningdriver = true
        }
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
            val p = LatLng(
                lat.toDouble() / 1E5,
                lng.toDouble() / 1E5
            )
            poly.add(p)
        }
        return poly
    }

    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.Button_Starttrip -> {
                /****
                 * cancel the ride here with change ride status
                 */
                if (rideRequestId.isNotEmpty()) {
                    if (isResuarantBePaymentAvailable.isNotEmpty() && isResuarantBePaymentAvailable == "1") {
                        if (isReceiptUpload == 1) changeRideStatusMethod(
                            rideRequestId,
                            Constants.START_RIDE
                        )
                        else
                            Helper.showErrorAlert(
                                this,
                                "Please upload order receipt before start ride"
                            )
                    } else
                        changeRideStatusMethod(rideRequestId, Constants.START_RIDE)
                }
            }
            R.id.iv_whatsApp -> {
                val number =
                    currentRideData!!.user.countryCode + currentRideData!!.user.countryCodePhone
                val url = "https://api.whatsapp.com/send?phone=$number"
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(url)
                startActivity(i)
            }
            R.id.tv_uploadReceipt -> {
//                checkPermissionCamera(false, "2", "")
                uploadReceiptForm(this@Home_Page)
            }
            R.id.iv_message -> {
                val number = currentRideData!!.user.countryCodePhone
                /* val number = currentRideData!!.user.countryCodePhone
                 startActivity(Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", number, null)))*/
                val openMessengerDailog = Dialog(this)
                openMessengerDailog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                openMessengerDailog.setContentView(R.layout.messenger_popup_layout)
                openMessengerDailog.window!!.setLayout(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT
                )
                openMessengerDailog.setCancelable(true)
                openMessengerDailog.setCanceledOnTouchOutside(true)
                openMessengerDailog.window!!.setGravity(Gravity.BOTTOM)
                openMessengerDailog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                openMessengerDailog.iv_messageUser.setOnClickListener {
                    openMessengerDailog.dismiss()
                    val uri = Uri.parse("smsto:$number")
                    val it = Intent(Intent.ACTION_SENDTO, uri)
                    it.putExtra("sms_body", "")
                    startActivity(it)
                    /* try {
                        if (getDefaultSmsAppPackageName(this) != null) {
                            val smsUri = Uri.parse("smsto:" + number)
                            val intent = Intent(Intent.ACTION_VIEW, smsUri)
                            intent.type = "vnd.android-dir/mms-sms"
                            startActivity(intent)
                        }else
                            startActivity(Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", number, null)))

                    } catch (e: Exception) {
                        startActivity(Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", number, null)))
                    }*/

//                    startActivity(Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", number, null)))
                    /*val smsIntent = Intent(Intent.ACTION_VIEW)
                    smsIntent.type = "vnd.android-dir/mms-sms"
                    smsIntent.putExtra("address", number)
                    startActivity(smsIntent)*/
                }
                openMessengerDailog.iv_whatsAppUser.setOnClickListener {
                    openMessengerDailog.dismiss()
                    val url = "https://api.whatsapp.com/send?phone=$number"
                    val i = Intent(Intent.ACTION_VIEW)
                    i.data = Uri.parse(url)
                    startActivity(i)
                }
                openMessengerDailog.show()
            }
            R.id.ll_call -> {
                val phone = currentRideData!!.user.countryCodePhone
                val intent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null))
                startActivity(intent)
            }
            R.id.Button_cancle -> {
                /****
                 * cancel the ride here with change ride status
                 */
                if (rideRequestId.isNotEmpty()) {
                    viewModel.changeRideStatusApi(this, rideRequestId, Constants.CANCEL_RIDE, true)
                    viewModel.getChangeRideStatusResposne().observe(this, this)
                }
            }
            R.id.Button_COMPLETEtrip -> {
                /****
                 * cancel the ride here with change ride status
                 */
                if (rideRequestId.isNotEmpty()) {
                    viewModel.changeRideStatusApi(
                        this,
                        rideRequestId,
                        Constants.COMPLETE_RIDE,
                        true
                    )
                    viewModel.getChangeRideStatusResposne().observe(this, this)
                }
            }
            R.id.Button_imhear -> {
                /****
                 * I am here API call
                 */
                if (rideRequestId.isNotEmpty()) {
                    viewModel.iAmHereApi(this, rideRequestId, false)
                    viewModel.getIamHereResposne().observe(this, this)
                }
            }
            R.id.tv_currentOrderDetails -> {
                if (currentRideData != null && currentRideData!!.id != 0) {
                    showOrderDetailsPopUp(currentRideData!!)
                }
            }
            R.id.iv_drawable -> {
                openCloseDrawer()
            }
            /*R.id.Relative_offline -> {
                viewModel.updateDriverOnlineStatusResposneApi(this, "1", true)
                viewModel.updateDriverOnlineStatusResposne().observe(this, this)
            }*/
            /* R.id.Relative_Online -> {
                 viewModel.updateDriverOnlineStatusResposneApi(this, "0", true)
                 viewModel.updateDriverOnlineStatusResposne().observe(this, this)
             }*/
            R.id.relativ_livlocation -> {
                if (mLatitute.isNotEmpty() && mLatitute != "0.0") {
                    if (googleMap != null) {
                        val sydney = LatLng(mLatitute.toDouble(), mLongitute.toDouble())
                        val cameraPosition =
                            CameraPosition.Builder().target(sydney).zoom(12f).build()
                        googleMap!!.animateCamera(
                            CameraUpdateFactory.newCameraPosition(
                                cameraPosition
                            )
                        )
                    }
                } else {
                    checkPermissionLocation(this)
                }

            }
            R.id.Relativ_currentloc -> {
                if (mLatitute.isNotEmpty() && mLatitute != "0.0") {
                    val location = CameraUpdateFactory.newLatLngZoom(
                        LatLng(
                            mLatitute.toDouble(),
                            mLongitute.toDouble()
                        ), 15f
                    )
                    googleMap!!.animateCamera(location)
                } else {
                    checkPermissionLocation(this)
                }
            }
//            R.id.Relative_offlineN -> {
//                Relative_OnlineN!!.setVisibility(View.VISIBLE)
//                Relative_offlineN!!.setVisibility(View.GONE)
//            }
            R.id.Relativ_moveToMapApp -> {
                if (isGoogleMapsInstalled()) {
                    if (finishlat != null && finishlong != null && finishlat != 0.0 && finishlong != 0.0) {
                        val uri =
                            "http://maps.google.com/maps?saddr=$mLatitute,$mLongitute&daddr=$finishlat,$finishlong"
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
                        startActivity(intent)
                    }
                } else
                    Helper.showErrorAlert(
                        this,
                        "Unable to find google map. Please intsall the google map"
                    )
            }
            /* R.id.Relative_OnlineN -> {
                 Relative_offlineN!!.setVisibility(View.VISIBLE)
                 Relative_OnlineN!!.setVisibility(View.GONE)
             }*/
            R.id.LL_deliveries -> {
                temp = 1
                startActivity(Intent(this, Deliveries_jobhistory::class.java))
            }

            R.id.LL_paymentstatus -> {
                temp = 1
                startActivity(Intent(this, PaymentStatsActivity::class.java))
            }
            R.id.homelayout -> {
                temp = 0
                openCloseDrawer()
            }
            R.id.LL_support -> {
                temp = 1
                launchActivity<Contactus_Activity>()
            }
            R.id.LL_notification -> {
                temp = 1
                launchActivity<NotificationActivity>()
            }
            R.id.LL_TandC -> {
                temp = 1
                launchActivity<TermAnd_Conditions>()
            }
            R.id.LL_settings -> {
                temp = 1
                launchActivity<SettingsActivity>()
            }
            R.id.ll_trainingVideo -> {
                temp = 1
                viewModel.getTrainingVideoLinksApi(this, true)
                viewModel.getTrainingVideoLinksResponse().observe(this, this)
                /* launchActivity<DriverTrainingVideoActivity>() {
                     putExtra("videoUrl", videoUrl)
                 }*/
            }
            R.id.Relativ_profile -> {
                temp = 1
                launchActivity<My_Profile_Activity>()
            }
            R.id.LL_logout -> {
                temp = 1
                builder!!.setMessage(getString(R.string.logout))
                    .setTitle(getString(R.string.logout))
                builder!!.setMessage(getString(R.string.logout_alert))
                    .setCancelable(false)
                    .setPositiveButton(getString(R.string.yes)) { _, _ ->
                        viewModel.logoutApi(this, true)
                        viewModel.getlogoutResposne().observe(this, this)
                    }
                    .setNegativeButton(getString(R.string.no)) { dialog, _ -> dialog.cancel() }
                builder!!.create().show()
            }
        }
    }

    private fun changeRideStatusMethod(rideRequestId: String, ridestatus: String) {
        viewModel.changeRideStatusApi(this, rideRequestId, ridestatus, true)
        viewModel.getChangeRideStatusResposne().observe(this, this)
    }

    private fun isGoogleMapsInstalled(): Boolean {
        return try {
            val info: ApplicationInfo =
                packageManager.getApplicationInfo("com.google.android.apps.maps", 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    /***
     * Dialog to show order details of current job
     */
    @SuppressLint("SetTextI18n")
    private fun showOrderDetailsPopUp(currentRideData: Body) {
        dialogOrderDeatail = Dialog(this@Home_Page)
        dialogOrderDeatail.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialogOrderDeatail.setContentView(R.layout.order_details_dailog_new)
        dialogOrderDeatail.setCancelable(false)
        /***
         * Set data over Views
         */
        val adapterItemQuantity =
            OrderDetailsQuantiytAdapter(this, currentRideData.order.orderDetails)
        dialogOrderDeatail.rv_orderItems.adapter = adapterItemQuantity

        val houseNumber = currentRideData.userAddress.completeAddress
        val streetName =
            if (currentRideData.userAddress.streetName.isNotEmpty()) "/" + currentRideData.userAddress.streetName else ""
        val landmark =
            if (currentRideData.userAddress.deliveryInstructions.isNotEmpty()) "\n" + currentRideData.userAddress.deliveryInstructions else ""
        val userAddres =
            if (currentRideData.userAddress.address.isNotEmpty()) "\n" + currentRideData.userAddress.address else ""
        val finalAddress = houseNumber + streetName + landmark + userAddres
        dialogOrderDeatail.tv_userName.text =
            currentRideData.user.firstName + " " + currentRideData.user.lastName
        dialogOrderDeatail.tv_userAddress.text = finalAddress
        dialogOrderDeatail.tv_pickUpFrom.text = currentRideData.restaurant.name
        dialogOrderDeatail.tv_restaurantAddress.text = currentRideData.restaurant.address
        dialogOrderDeatail.tv_ContactNumber.text = "P: " + currentRideData.user.countryCodePhone
        dialogOrderDeatail.tv_userEmail.text = "Email: " + currentRideData.user.email
        dialogOrderDeatail.tv_orderPlaceDate.text = CommonMethods.parseDateToddMMyyyy(
            currentRideData.order.createdAt,
            Constants.ORDER_DATE_FORMAT
        )
        dialogOrderDeatail.tv_pickUpFromTime.text =
            CommonMethods.convertTimeToTimestamp(currentRideData.pickuptime.toLong())
        dialogOrderDeatail.tv_dropOffTime.text =
            CommonMethods.convertTimeToTimestamp(currentRideData.dropofftime.toLong())

        if (currentRideData.userAddress.deliveryInstructions.isNotEmpty()) {
            dialogOrderDeatail.tv_specialRequest.visibility = View.VISIBLE
            dialogOrderDeatail.tv_specialRequest.text =
                "Note: " + currentRideData.userAddress.deliveryInstructions
        } else
            dialogOrderDeatail.tv_specialRequest.visibility = View.GONE

        if (currentRideData.order.preparationTime.isNotEmpty()) {
            if (currentRideData.rideStatus == 2)
                dialogOrderDeatail.tv_preprationTime.visibility = View.GONE
            else if (currentRideData.rideStatus == 1 && currentRideData.response == 0 || currentRideData.response == 1)
                dialogOrderDeatail.tv_preprationTime.visibility = View.VISIBLE
            else if (currentRideData.rideStatus == 1 && currentRideData.response == 1)
                dialogOrderDeatail.tv_preprationTime.visibility = View.VISIBLE
            else
                dialogOrderDeatail.tv_preprationTime.visibility = View.GONE

            dialogOrderDeatail.tv_preprationTime.text =
                "Prepration time: " + currentRideData.order.preparationTime + " mins"
        }
        dialogOrderDeatail.tv_orderId.text = "#" + currentRideData.order.id.toString()
        dialogOrderDeatail.tv_orderPrice.text =
            "$ " + Helper.roundOffDecimalNew(currentRideData.order.totalAmount.toFloat())
//        dialogOrderDeatail.tv_totalAmountWithAll.text = "$ " + Helper.roundOffDecimalNew(currentRideData.order.netAmount.toFloat())
        if (currentRideData.order.driverNetAmount != null)
            dialogOrderDeatail.tv_totalAmountWithAll.text =
                "$ " + Helper.roundOffDecimalNew(currentRideData.order.driverNetAmount.toFloat())
        else
            dialogOrderDeatail.tv_totalAmountWithAll.text =
                "$ " + Helper.roundOffDecimalNew(currentRideData.order.netAmount.toFloat())
        /** paymentMethod-1 for suncash
         * paymentMethod-2 for paypal
         * paymentMethod-4 for kanoo
         * paymentMethod-5 for Atlantic
         * paymentMethod-6 for IsLand Pay
         * paymentMethod-7 for BE Wallet
         * paymentMethod-8 for Loyalty Bonus
         * */
        when (currentRideData.order.paymentMethod) {
            "1" -> {
                dialogOrderDeatail.tv_paymentMode.text = "Payment Mode: Suncash"
            }
            "2" -> {
                dialogOrderDeatail.tv_paymentMode.text = "Payment Mode: Paypal"
            }
            "4" -> {
                dialogOrderDeatail.tv_paymentMode.text = "Payment Mode: Kanoo"
            }
            "5" -> {
                dialogOrderDeatail.tv_paymentMode.text = "Payment Mode: Atlantic"
            }
            "7" -> {
                dialogOrderDeatail.tv_paymentMode.text =
                    "Payment Mode: " + getString(R.string.be_wallet)
            }
            "8" -> {
                dialogOrderDeatail.tv_paymentMode.text =
                    "Payment Mode: " + getString(R.string.loyalty_bonus)
            }
        }

        val listAddOnList = ArrayList<AddOnsCustomModel>()
        var count = 0
        for (i in currentRideData.order.orderDetails.indices) {
            for (j in currentRideData.order.orderDetails[i].categories.indices) {
                for (k in currentRideData.order.orderDetails[i].categories[j].addOnArray.indices) {
                    val addOnMOdel = AddOnsCustomModel(
                        currentRideData.order.orderDetails[i].categories[j].addOnArray[k].addon,
                        currentRideData.order.orderDetails[i].categories[j].addOnArray[k].price,
                        currentRideData.order.orderDetails[i].quantity
                    )
                    listAddOnList.add(count, addOnMOdel)
                    count++
                }
            }
        }

        /*if (listAddOnList != null && listAddOnList.isNotEmpty()) {
            dialogOrderDeatail.ll_addOnsLabel.visibility = View.VISIBLE
            val adapterItemAddOnsQuantity1 = OrderDetailsAddOnsQuantiytAdapter(this, listAddOnList)
            dialogOrderDeatail.rv_orderAddOnsItems.adapter = adapterItemAddOnsQuantity1
        } else {
            dialogOrderDeatail.ll_addOnsLabel.visibility = View.GONE
        }*/

        dialogOrderDeatail.btn_ok.setOnClickListener { dialogOrderDeatail.dismiss() }
        dialogOrderDeatail.show()
    }

    private fun uploadReceiptForm(homePage: Home_Page) {
        arrayImageVideo.clear()
        if (uploadReceiptDialog != null && uploadReceiptDialog.isShowing) {
            Log.e("uploadReceiptDialog:", "Already showing")
        } else {
            photoAdapter = PhotoAdapter(homePage, arrayImageVideo)
            uploadReceiptDialog.rvImageVideo.setLayoutManager(
                LinearLayoutManager(
                    homePage,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
            )
            uploadReceiptDialog.rvImageVideo.setAdapter(photoAdapter)
            uploadReceiptDialog.setCancelable(false)
            uploadReceiptDialog.setCanceledOnTouchOutside(false)
//            uploadReceiptDialog.et_totalAmount.text = currentRideData!!.order.netAmount
            if (currentRideData!!.order.driverNetAmount != null)
                uploadReceiptDialog.et_totalAmount.text = currentRideData!!.order.driverNetAmount
            else
                uploadReceiptDialog.et_totalAmount.text = currentRideData!!.order.netAmount
            uploadReceiptDialog.show()
        }
        uploadReceiptDialog.iv_uploadReceipt.setOnClickListener {
//            checkPermissionCamera(false, "2", "")
            mAlbumFiles = ArrayList()
            Album.image(homePage)
                .multipleChoice()
                .columnCount(4)
//                .selectCount(5)
                .selectCount(1)
                .camera(true)
                .checkedList(mAlbumFiles)
                .afterFilterVisibility(true)
                .checkedList(mAlbumFiles)
                .widget(
                    Widget.newDarkBuilder(homePage).title(getString(R.string.app_name))
                        .statusBarColor(ContextCompat.getColor(this, R.color.White))
                        .toolBarColor(ContextCompat.getColor(this, R.color.Greenapp))
                        .build()
                )
                .onResult { result ->
                    mAlbumFiles = result
                    for (i in 0 until mAlbumFiles.size) {
                        val imageVideoModel = ImageVideoModel()
                        val type = mAlbumFiles[i].mediaType
//                        if (arrayImageVideo.size == 5) {
                        if (arrayImageVideo.size == 1) {
//                            Helper.showSuccessToast(homePage, "Maximum  upload limit is 5")
                            Helper.showSuccessToast(homePage, "Maximum  upload limit is 1")
                        } else {
                            if (type == 1) {
                                imageVideoModel.setType("0")
                                imageVideoModel.setImageVideoPath(mAlbumFiles.get(i).path)
                                imageVideoModel.setIsAdded("true")
                                arrayImageVideo.add(imageVideoModel)
                                Log.d("uploadReceiptForm: ", arrayImageVideo.size.toString())
                            }
                        }
                    }
                    photoAdapter.notifyDataSetChanged()
                }
                .onCancel {
                }
                .start()

        }

        uploadReceiptDialog.btn_ok.setOnClickListener {
            if (uploadReceiptDialog.et_receiptNumber.text.toString().trim().isEmpty()) {
                Helper.showSuccessToast(homePage, "Please enter receipt number")
            }
//            else if (image_path.isEmpty()) {
            else if (arrayImageVideo.isEmpty()) {
                Helper.showSuccessToast(homePage, "Please add receipt image")
//            } else if (arrayImageVideo.isNotEmpty()&&arrayImageVideo.size>5) {
            } else if (arrayImageVideo.isNotEmpty() && arrayImageVideo.size > 1) {
//                Helper.showSuccessToast(homePage, "Maximum  upload limit is 5")
                Helper.showSuccessToast(homePage, "Maximum  upload limit is 1")
            } else {
                Log.d("uploadReceiptForm: ", arrayImageVideo.size.toString())
//                viewModel.uploadReceiptApi(homePage, image_path, currentRideData!!.orderId.toString(), uploadReceiptDialog.et_receiptNumber.text.toString().trim(), uploadReceiptDialog.et_totalAmount.text.toString().trim(),arrayImageVideo, true)
                viewModel.uploadReceiptApi(
                    homePage,
                    arrayImageVideo[0].imageVideoPath,
                    currentRideData!!.orderId.toString(),
                    uploadReceiptDialog.et_receiptNumber.text.toString().trim(),
                    uploadReceiptDialog.et_totalAmount.text.toString().trim(),
                    arrayImageVideo,
                    true
                )
                viewModel.getUploadReceiptResponse().observe(homePage, homePage)
            }
        }
    }

    @SuppressLint("RtlHardcoded")
    private fun openCloseDrawer() {
        if (drawerLayout.isDrawerOpen(Gravity.LEFT))
            drawerLayout.closeDrawer(Gravity.LEFT)
        else
            drawerLayout.openDrawer(Gravity.LEFT)
    }

    fun showViewsWhenRideIsAccepted() {
        card_pikup.visibility = View.VISIBLE
        rl_bottomOptionsRoot.visibility = View.GONE
        layoutCurrentJob.visibility = View.VISIBLE
        relativ_livlocation.visibility = View.GONE
        Relativ_currentloc.visibility = View.GONE
        Button_imhear.visibility = View.GONE
        Button_COMPLETEtrip.visibility = View.GONE
        Button_Starttrip.visibility = View.VISIBLE
    }

    @SuppressLint("SetTextI18n")
    private fun showViewsWhenRideIsStared() {
        card_pikup.visibility = View.VISIBLE
        rl_bottomOptionsRoot.visibility = View.GONE
        layoutCurrentJob.visibility = View.VISIBLE
        /***
         * Show Driver available information
         */
        relativ_livlocation.visibility = View.GONE
        Relativ_currentloc.visibility = View.GONE
        Button_imhear.visibility = View.VISIBLE
        Button_COMPLETEtrip.visibility = View.VISIBLE
        Button_Starttrip.visibility = View.GONE
        tv_uploadReceipt.visibility = View.GONE
    }

    private fun showViewsWhenCurrentRideIsAvailable() {
        hedder.setBackgroundColor(Color.TRANSPARENT)
        rl_bottomOptionsRoot.visibility = View.GONE
        layoutCurrentJob.visibility = View.GONE
        card_pikup.visibility = View.GONE
        relativ_livlocation.visibility = View.VISIBLE
        Relativ_currentloc.visibility = View.GONE
    }

    @SuppressLint("SetTextI18n")
    override fun onChanged(liveData: RestObservable?) {
        when (liveData!!.status) {
            Status.SUCCESS -> {
                if (liveData.data is TrainingVideoLinksResponse) {

                    if (liveData.data.body.isNotEmpty()) {
                        videoUrl = liveData.data.body[0].link
                    }
                    launchActivity<DriverTrainingVideoActivity> {
                        putExtra("videoUrl", videoUrl)
                    }
                }
                if (liveData.data is GetTakeDriverOrderStatus) {
                    takeOrderStatusLocal = liveData.data.body.takeOrderStatus.toString()
                    todaySlotsListing = liveData.data.body.selectedSlots
                    updateDriverTakeOrderView(liveData.data.body.takeOrderStatus)
                }
                if (liveData.data is UploadReceiptResponse) {
                    isReceiptUpload = 1
                    Helper.showSuccessToast(this, liveData.data.message)
                    tv_uploadReceipt.text = getString(R.string.uploaded_receipt)
                    if (uploadReceiptDialog != null && uploadReceiptDialog.isShowing) {
                        uploadReceiptDialog.dismiss()
                    }
                }
                if (liveData.data is UpdateDriverLatLongResponse) {
                    Log.e("updateDriverLatLng", "success" + liveData.data.message)
                    if (finishlat != null && finishlong != null && finishlat != 0.0 && finishlong != 0.0) {
                        drawpoliline(
                            LatLng(startlat!!, startlong!!),
                            LatLng(finishlat!!, finishlong!!),
                            "Restaurant"
                        )
                        if (dropMarker == null)
                            setDropMaker("Restaurant")
                    }
                }
                //Response when new job is avaliable for Accept and Reject
                if (liveData.data is AcceptRejectRideRequest) {
                    if (dialog.isShowing) {
                        dialog.dismiss()
                    }
                    if (liveData.data.code == 403) {
                        Log.e("AcceptRejectRideRequest", "success: 403" + liveData.data.message)
                    } else {
                        loadMapOf = 0
                        Log.e("AcceptRejectRideRequest", "success" + liveData.data.message)
                        /**
                         *  response :0=>Pending 1=>Accepted 2=>Rejected
                         */
                        if (liveData.data.body.response == 1) {
                            type = 1
                            latRestaurantAcceptJob = liveData.data.body.restaurant.latitude
                            longRestaurantAcceptJob = liveData.data.body.restaurant.longitude
                            Log.e(
                                "AcceptJob:..mLatitute: ",
                                mLatitute + "mLongitute: " + mLongitute + "restaurant.latitude: " + liveData.data.body.restaurant.latitude + "restaurant.longitude" + liveData.data.body.restaurant.longitude
                            )
                            anyActiveJobAvailable = 1
                            Helper.showSuccessAlert(this, liveData.data.message)
                            launchActivity<Home_Page>()
                            finish()
                        } else if (liveData.data.body.response == 2) {
                            clearAllFromMmMapSetDriverMaker()
                            Helper.showErrorAlertWithoutTitle(this, liveData.data.message)
                            anyActiveJobAvailable = 0
                            launchActivity<Home_Page>()
                            finish()
                        }

                    }
                }
                //Response when new job is avaliable and has been accepted/started
                if (liveData.data is GetCurrentRideResponse) {
                    if (liveData.data.code == 403) {
                        anyActiveJobAvailable = 0
                        Log.e("GetCurrentRideResponse", "success" + liveData.data.message)
                        showViewsWhenCurrentRideIsAvailable()
                    } else {
                        Log.e("GetCurrentRideResponse", "success" + liveData.data.message)
                        if (currentJobOptiondialog.isShowing) {
                            currentJobOptiondialog.dismiss()
                        }
                        /*  rideStatus: 1.New,2.Accepted,3.Completed,4.Cancelled,5.In-progress,6.Ready For Pickup
                         *  ////////////////////////////////////////////////////////////////////////////
                         *  response => 0=>Pending 1=>Accepted 2=>Rejected
                         *  rideStatus --> 1=>New 2=>Started 3=>Completed 4=>Cancelled
                         */
                        currentRideData = liveData.data.body
                        if (currentRideData!!.order.receiptUpload.isNotEmpty())
                            isReceiptUpload = 1
                        anyActiveJobAvailable = 1
                        otherUserId = liveData.data.body.userId.toString()
                        rideRequestId = currentRideData!!.id.toString()
                        isResuarantBePaymentAvailable = currentRideData!!.restaurant.isBe
                        Glide.with(this).load(currentRideData!!.user.photo)
                            .placeholder(R.drawable.placeholder_rectangle).into(Image_profile)
                        tv_currentOrderUsename.text = currentRideData!!.user.username
                        tv_currentOrderId.text = "Order ID: #" + currentRideData!!.order.id

                        /** paymentMethod-1 for suncash
                         * paymentMethod-2 for paypal
                         * paymentMethod-4 for kanoo
                         * paymentMethod-5 for Atlantic
                         * paymentMethod-6 for IsLand Pay
                         * paymentMethod-7 for BE Wallet
                         * paymentMethod-8 for Loyalty Bonus
                         */
                        when (currentRideData!!.order.paymentMethod) {
                            "1" -> {
                                tv_currentOrderPaymentMode.text = "Payment Mode: Suncash"
                            }
                            "2" -> {
                                tv_currentOrderPaymentMode.text = "Payment Mode: Paypal"
                            }
                            "4" -> {
                                tv_currentOrderPaymentMode.text = "Payment Mode: Kanoo"
                            }
                            "5" -> {
                                tv_currentOrderPaymentMode.text = "Payment Mode: Atlantic"
                            }
                            "6" -> {
                                tv_currentOrderPaymentMode.text = "Payment Mode: IsLand Pay"
                            }
                            "7" -> {
                                tv_currentOrderPaymentMode.text =
                                    "Payment Mode: " + getString(R.string.be_wallet)
                            }
                            "8" -> {
                                tv_currentOrderPaymentMode.text =
                                    "Payment Mode: " + getString(R.string.loyalty_bonus)
                            }
                        }
//                        tv_currentOrderTotal.text = "$" + Helper.roundOffDecimalNew(currentRideData!!.order.netAmount.toFloat())
                        if (currentRideData!!.order.driverNetAmount != null)
                            tv_currentOrderTotal.text =
                                "$" + Helper.roundOffDecimalNew(currentRideData!!.order.driverNetAmount.toFloat())
                        else
                            tv_currentOrderTotal.text =
                                "$" + Helper.roundOffDecimalNew(currentRideData!!.order.netAmount.toFloat())

                        //When new job is available for accept/reject
                        if (currentRideData!!.rideStatus == 1 && currentRideData!!.response == 0) {
                            finishlat = currentRideData!!.restaurant.latitude
                            finishlong = currentRideData!!.restaurant.longitude
                            showViewsWhenCurrentRideIsAvailable()
                            openNewJobDailog(currentRideData!!)
                        }
                        //when ride is accepted by the driver
                        else if (currentRideData!!.rideStatus == 1 && currentRideData!!.response == 1) {
                            finishlat = currentRideData!!.restaurant.latitude
                            finishlong = currentRideData!!.restaurant.longitude
                            if (startlat != null && startlat != 0.0 && startlong != null && startlong != 0.0) {
                                drawpoliline(
                                    LatLng(startlat!!, startlong!!),
                                    LatLng(finishlat!!, finishlong!!),
                                    "Restaurant"
                                )
                                setDropMaker("Restaurant")
                            }
                            tv_adress.text = currentRideData!!.restaurant.address
                            showViewsWhenRideIsAccepted()
                            showHildeUploadReceiptView(
                                currentRideData!!.order.receiptUpload,
                                currentRideData!!.restaurant.isBe,
                                ""
                            )
                        }
                        //when ride is accepted by the driver
                        else if (currentRideData!!.rideStatus == 2) {
                            finishlat = currentRideData!!.userAddress.latitude
                            finishlong = currentRideData!!.userAddress.longitude

                            val houseNumber = currentRideData!!.userAddress.completeAddress
                            val streetName =
                                if (currentRideData!!.userAddress.streetName.isNotEmpty()) "/" + currentRideData!!.userAddress.streetName else ""
                            val landmark =
                                if (currentRideData!!.userAddress.deliveryInstructions.isNotEmpty()) "\n" + currentRideData!!.userAddress.deliveryInstructions else ""
                            val userAddres =
                                if (currentRideData!!.userAddress.address.isNotEmpty()) "\n" + currentRideData!!.userAddress.address else ""
                            val finalAddress = houseNumber + streetName + landmark + userAddres
                            tv_adress.text = finalAddress
                            showViewsWhenRideIsStared()
                            tv_adress.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                            Iv_search.visibility = View.GONE
                            if (dropMarker != null)
                                dropMarker!!.remove()
                            drawpoliline(
                                LatLng(startlat!!, startlong!!),
                                LatLng(finishlat!!, finishlong!!),
                                "Deliver to"
                            )
                            setDropMaker("Deliver to")
                            Log.e(
                                "GetCur:..mLatitute: ",
                                currentRideData!!.fromLat + "mLongitute: " + currentRideData!!.fromLong + "restaurant.latitude: " + liveData.data.body.restaurant.latitude + "restaurant.longitude" + liveData.data.body.restaurant.longitude
                            )
                            startStep3(this)
                        } else if (currentRideData!!.rideStatus == 3) {
                            clearAllFromMmMapSetDriverMaker()
                            launchActivity<CompleteRideDetailsActivity>()

                        } else if (currentRideData!!.rideStatus == 4) {
                            clearAllFromMmMapSetDriverMaker()
                            showViewsWhenCurrentRideIsAvailable()
                        } else if (currentRideData!!.rideStatus == 5) {
                            Log.e("GetCurrentRideResponse", "5")
                        } else if (currentRideData!!.rideStatus == 6) {
                            Log.e("GetCurrentRideResponse", "6")
                        }
                    }
                }
                //Response of logout from app
                if (liveData.data is LogoutResponse) {
                    val mSensorService = SensorService(this)
                    val mServiceIntent = Intent(this, SensorService::class.java)
                    if (isMyServiceRunning(this, mSensorService::class.java))
                        stopService(mServiceIntent)
                    launchActivity<Login_Activity>()
                    finishAffinity()
                    clearPrefrences()
                    val nm =
                        getSystemService(AppCompatActivity.NOTIFICATION_SERVICE) as NotificationManager
                    nm.cancelAll()
                }
                //Response to send message to user that I am reached to user location
                if (liveData.data is IAmHereResponse) {
                    Log.e("IAmHereResponse::=", liveData.data.message)
                    Helper.showSuccessToast(this, "Location send")
                }
                //Response to change current job status like start/cancel/compelete
                if (liveData.data is ChangeRideStatusResponse) {
                    /***
                     *  rideStatus: 1.New,2.Accepted,3.Completed,4.Cancelled,5.In-progress,6.Ready For Pickup
                     *  ////////////////////////////////////////////////////////////////////////////
                     *  response => 0=>Pending 1=>Accepted 2=>Rejected
                     *  rideStatus --> 1=>New 2=>Started 3=>Completed 4=>Cancelled
                     */
                    otherUserId = liveData.data.body.userId.toString()
                    changeRideStatus = liveData.data.body
                    currentRideData!!.response = changeRideStatus!!.response
                    currentRideData!!.rideStatus = changeRideStatus!!.rideStatus

                    Glide.with(this).load(changeRideStatus!!.user.photo)
                        .placeholder(R.drawable.placeholder_rectangle).into(Image_profile)
                    tv_currentOrderUsename.text = changeRideStatus!!.user.username
                    tv_currentOrderId.text = "Order ID: #" + changeRideStatus!!.order.id
                    /**
                     * paymentMethod-1 for suncash
                     * paymentMethod-2 for paypal
                     * paymentMethod-4 for kanoo
                     * paymentMethod-5 for Atlantic
                     * paymentMethod-6 for IsLand Pay
                     * paymentMethod-7 for BE Wallet
                     * paymentMethod-8 for Loyalty Bonus
                     */
                    when (changeRideStatus!!.order.paymentMethod) {
                        1 -> {
                            tv_currentOrderPaymentMode.text = "Payment Mode: Suncash"
                        }
                        2 -> {
                            tv_currentOrderPaymentMode.text = "Payment Mode: Paypal"
                        }
                        4 -> {
                            tv_currentOrderPaymentMode.text = "Payment Mode: Kanoo"
                        }
                        5 -> {
                            tv_currentOrderPaymentMode.text = "Payment Mode: Atlantic"
                        }
                        6 -> {
                            tv_currentOrderPaymentMode.text = "Payment Mode: IsLand Pay"
                        }
                        7 -> {
                            tv_currentOrderPaymentMode.text =
                                "Payment Mode: " + getString(R.string.be_wallet)
                        }
                        8 -> {
                            tv_currentOrderPaymentMode.text =
                                "Payment Mode: " + getString(R.string.loyalty_bonus)
                        }
                    }
//                    tv_currentOrderTotal.text = "$" + Helper.roundOffDecimalNew(changeRideStatus!!.order.netAmount.toFloat())
                    if (changeRideStatus!!.order.driverNetAmount != null)
                        tv_currentOrderTotal.text =
                            "$" + Helper.roundOffDecimalNew(changeRideStatus!!.order.driverNetAmount.toFloat())
                    else
                        tv_currentOrderTotal.text =
                            "$" + Helper.roundOffDecimalNew(changeRideStatus!!.order.netAmount.toFloat())
                    val houseNumber = changeRideStatus!!.userAddress.completeAddress
                    val streetName =
                        if (changeRideStatus!!.userAddress.streetName.isNotEmpty()) "/" + changeRideStatus!!.userAddress.streetName else ""
                    val landmark =
                        if (changeRideStatus!!.userAddress.deliveryInstructions.isNotEmpty()) "\n" + changeRideStatus!!.userAddress.deliveryInstructions else ""
                    val userAddres =
                        if (changeRideStatus!!.userAddress.address.isNotEmpty()) "\n" + changeRideStatus!!.userAddress.address else ""
                    val finalAddress = houseNumber + streetName + landmark + userAddres
                    tv_adress.text = finalAddress
                    //rideStatus=2 when  driver click to start ride
                    if (changeRideStatus!!.rideStatus == 2) {
                        Helper.showSuccessAlert(this, liveData.data.message)
                        anyActiveJobAvailable = 1
                        finishlat = changeRideStatus!!.userAddress.latitude
                        finishlong = changeRideStatus!!.userAddress.longitude
                        val houseNumber = changeRideStatus!!.userAddress.completeAddress
                        val streetName =
                            if (changeRideStatus!!.userAddress.streetName.isNotEmpty()) "/" + changeRideStatus!!.userAddress.streetName else ""
                        val landmark =
                            if (changeRideStatus!!.userAddress.deliveryInstructions.isNotEmpty()) "\n" + changeRideStatus!!.userAddress.deliveryInstructions else ""
                        val userAddres =
                            if (changeRideStatus!!.userAddress.address.isNotEmpty()) "\n" + changeRideStatus!!.userAddress.address else ""
                        val finalAddress = houseNumber + streetName + landmark + userAddres
                        tv_adress.text = finalAddress
                        showViewsWhenRideIsStared()
                        tv_adress.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                        Iv_search.visibility = View.GONE
                        if (dropMarker != null) {
                            dropMarker!!.remove()
                            dropMarker = null
                        }
                        drawpoliline(
                            LatLng(startlat!!, startlong!!),
                            LatLng(finishlat!!, finishlong!!),
                            "Deliver to"
                        )
                        setDropMaker("Deliver to")
                        Log.e(
                            "GetCur:..mLatitute: ",
                            changeRideStatus!!.fromLat + "mLongitute: " + changeRideStatus!!.fromLong + "restaurant.latitude: " + liveData.data.body.restaurant.latitude + "restaurant.longitude" + liveData.data.body.restaurant.longitude
                        )
                        startStep3(this)
                    } else if (liveData.data.body.rideStatus == 3) {
                        anyActiveJobAvailable = 0
                        clearAllFromMmMapSetDriverMaker()
                        Helper.showSuccessToast(this, liveData.data.message)
                        showViewsWhenCurrentRideIsAvailable()
                        launchActivity<CompleteRideDetailsActivity>()
                        {
                            putExtra("data", changeRideStatus!!)
                            putExtra("currentLat", mLatitute)
                            putExtra("currentLong", mLongitute)
                        }
                    } else if (changeRideStatus!!.rideStatus == 4) {
                        anyActiveJobAvailable = 0
                        clearAllFromMmMapSetDriverMaker()
                        showViewsWhenCurrentRideIsAvailable()
                        Helper.showErrorAlertWithoutTitle(this, liveData.data.message)
                    } else if (changeRideStatus!!.rideStatus == 5) {
                        Log.e("ChangeRideStatusRspse", "In-progress")
                    } else if (changeRideStatus!!.rideStatus == 6) {
                        Log.e("ChangeRideStatusRspse", "Ready For Pickup")
                    }
                }
                //Response change driver online/offline status
                if (liveData.data is UpdateDriverTakeOrderStatus) {
                    takeOrderStatusLocal = liveData.data.body.takeOrderStatus.toString()
                    updateDriverTakeOrderView(liveData.data.body.takeOrderStatus)
                    /* val c1 = Calendar.getInstance()
                     //first day of week
                     c1[Calendar.DAY_OF_WEEK] = 1
                     //last day of week
                     c1[Calendar.DAY_OF_WEEK] = 7
                     val d = Date()
                     getDriverTakeStatusApicall(d.day.toString())*/
                }
            }

            Status.ERROR -> {

            }
            else -> {

            }
        }
    }

    private fun showHildeUploadReceiptView(
        receiptUpload: String,
        bePaymentAvailable: String,
        from: String
    ) {
        /**
         * from isEmpty=== use method called when ride is accepted
         */
        if (from.isEmpty()) {
            if (bePaymentAvailable == "1") {
                tv_uploadReceipt.visibility = View.VISIBLE
                if (receiptUpload.isEmpty()) tv_uploadReceipt.text =
                    getString(R.string.upload_receipt) else tv_uploadReceipt.text =
                    getString(R.string.uploaded_receipt)
            } else tv_uploadReceipt.visibility = View.GONE
        } else {
            if (bePaymentAvailable == "1") {
                if (receiptUpload.isEmpty()) tv_uploadReceipt.visibility =
                    View.VISIBLE else tv_uploadReceipt.visibility = View.GONE
            }
        }
    }

    private fun clearAllFromMmMapSetDriverMaker() {
        finishlat = 0.0
        finishlong = 0.0
        otherUserId = ""
        if (googleMap != null) {
            googleMap!!.clear()
            driverMarker = setMarkerdate(mLatitute.toDouble(), mLongitute.toDouble())
        }
    }

    override fun selectedImage(imagePath: String?, thumbnailVideoPath: String) {
        image_path = imagePath!!
        if (uploadReceiptDialog != null && uploadReceiptDialog.isShowing) {
            Glide.with(this).load(image_path).placeholder(R.drawable.placeholder_circle)
                .into(uploadReceiptDialog.iv_uploadReceipt)
        }

    }

    override fun uploadSlotsCodeFuncation() {
        Helper.showSuccessToast(this, "1211342342421")
    }

    override fun getUpdatedPhoneNoAfterVerify(
        contactNumber: String,
        updatedCountryCode: String
    ) {
//Not in use
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        return super.dispatchTouchEvent(ev)
    }

    private fun createZoomRoute(driverLatLong: LatLng, dropLatLong: LatLng) {
        val lstLatLngRoute: MutableList<LatLng> = java.util.ArrayList()
        lstLatLngRoute.add(driverLatLong)
        lstLatLngRoute.add(dropLatLong)
        zoomRoute(lstLatLngRoute)
    }

    private fun zoomRoute(lstLatLngRoute: MutableList<LatLng>) {
        if (googleMap == null || lstLatLngRoute == null || lstLatLngRoute.isEmpty()) return
        val boundsBuilder = LatLngBounds.Builder()
        for (latLngPoint in lstLatLngRoute) boundsBuilder.include(latLngPoint)
        val routePadding = 200
        val latLngBounds = boundsBuilder.build()
        googleMap!!.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, routePadding))
    }

    @SuppressLint("SetTextI18n")
    fun openNewJobDailog(body: Body) {
        Log.e("open", "true")
        if (dialog != null && dialog.isShowing) {
            Log.e("openNewJobDailog:", "Already showing")
        } else {
            Log.e("openNewJobDailog:", "else:  " + "Already showing")
            dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
            dialog.window!!.setGravity(Gravity.CENTER)
            dialog.window!!.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
            )
            dialog.setContentView(R.layout.res_pickup_request)
            dialog.setCancelable(false)
            btnAcceptRide = dialog.findViewById(R.id.Button_accept)
            btnRejectRide = dialog.findViewById(R.id.Button_regect)
            btnOrderDetail = dialog.findViewById(R.id.btn_orderDetails)
            /***
             * Be Card Payment View
             */
            if (body.restaurant.isBe == "0") dialog.tv_useBeCardForPayment.visibility =
                View.GONE else dialog.tv_useBeCardForPayment.visibility = View.VISIBLE
            latRestaurant = body.restaurant.latitude.toString()
            longRestaurant = body.restaurant.longitude.toString()
            dialog.tv_restaurantName.text = body.restaurant.address
            val houseNumber = body.userAddress.completeAddress
            val streetName =
                if (body.userAddress.streetName.isNotEmpty()) "/" + body.userAddress.streetName else ""
            val landmark =
                if (body.userAddress.deliveryInstructions.isNotEmpty()) "\n" + body.userAddress.deliveryInstructions else ""
            val userAddres =
                if (body.userAddress.address.isNotEmpty()) "\n" + body.userAddress.address else ""
            val finalAddress = houseNumber + streetName + landmark + userAddres
            dialog.tv_userOrderAddress.text = finalAddress
//            dialog.tv_totalAmount.text = "$" + Helper.roundOffDecimalNew(body.order.netAmount.toFloat())
            if (body.order.driverNetAmount != null)
                dialog.tv_totalAmount.text =
                    "$" + Helper.roundOffDecimalNew(body.order.driverNetAmount.toFloat())
            else
                dialog.tv_totalAmount.text =
                    "$" + Helper.roundOffDecimalNew(body.order.netAmount.toFloat())
            dialog.tv_minTimeToDeliver.text = body.restaurant.minDelivery + " mins"
            try {
                if (body.restaurant.longitude != 0.0 && body.userAddress.latitude != 0.0) {
                    val value1: String = java.lang.String.valueOf(
                        DecimalFormat("##").format(
                            distance(
                                body.restaurant.latitude,
                                body.restaurant.longitude,
                                body.userAddress.latitude,
                                body.userAddress.longitude
                            )
                        )
                    )
                    dialog.tv_minDeliverDistance.text = "$value1 mi"
                }
                if (mLatitute.isNotEmpty() && body.userAddress.latitude != 0.0) {
                    //Distance from Driver location to restaurant +and+ restaurant to user order delivery Address
                    val distancefromDriverToRestaurant = distance(
                        mLatitute.toDouble(),
                        mLongitute.toDouble(),
                        body.restaurant.latitude,
                        body.restaurant.longitude
                    )
                    val restaurantToUser = distance(
                        body.restaurant.latitude,
                        body.restaurant.longitude,
                        body.userAddress.latitude,
                        body.userAddress.longitude
                    )
                    val finalDistance = distancefromDriverToRestaurant + restaurantToUser
                    val finalDistanceNew: String =
                        java.lang.String.valueOf(DecimalFormat("##").format(finalDistance))
                    dialog.tv_totalDistance.text = "$finalDistanceNew mi"
                }
            } catch (e: Exception) {
            }
            btnAcceptRide?.setOnClickListener {
                //response :1 to accept the job
                respondRideStatusRequest(ACCEPT_RIDE_STATUS, body.id)
                val nm =
                    getSystemService(AppCompatActivity.NOTIFICATION_SERVICE) as NotificationManager
                nm.cancelAll()
            }
            btnRejectRide?.setOnClickListener {
                //response :2 to reject the job
                respondRideStatusRequest(REJECT_RIDE_STATUS, body.id)
                val nm =
                    getSystemService(AppCompatActivity.NOTIFICATION_SERVICE) as NotificationManager
                nm.cancelAll()
            }
            btnOrderDetail?.setOnClickListener {
                if (currentRideData != null && currentRideData!!.id != 0) {
                    showOrderDetailsPopUp(currentRideData!!)
                }
            }
            dateCheckMonth(
                timePrint(body.createdAt),
                getCurrentDate(),
                dialog.tv_countProgress,
                dialog.progressBarCircle,
                body.id
            )
            dialog.show()
        }

    }

    private fun timePrint(completedAt: String): String {
        val timeCountInMilliSeconds = convertToNewFormat5(completedAt)
        Log.e("rideTime", timeCountInMilliSeconds)
        val currentDateTime = getCurrentDate()
        Log.e("currentDateTime", timeCountInMilliSeconds)
        return timeCountInMilliSeconds
    }

    private fun getCurrentDate(): String {
        val c = Calendar.getInstance().time
        println("Current time => $c")
        val df = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
        return df.format(c)
    }

    private fun dateCheckMonth(
        startD: String,
        endDate: String,
        tv_Countprogress: TextView,
        progressbarCircle: ProgressBar,
        rideId: Int
    ): String {
        var status = "0"
        try {
            val formatter = DateTimeFormat.forPattern("yyyy-MM-dd hh:mm:ss")
            val d1 = formatter.parseDateTime(startD)
            val d2 = formatter.parseDateTime(endDate)
            val duration = Duration(d1, d2)
            val second = duration.standardSeconds
            val countDownTimer = object : CountDownTimer(90 * 1000 - second * 1000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    Log.e(
                        "timerStatus",
                        "inprogress" + "==" + "" + (millisUntilFinished / 1000).toInt()
                    )
                    tv_Countprogress.text = (millisUntilFinished / 1000).toInt().toString()
                    progressbarCircle.progress = (millisUntilFinished / 1000).toInt()
                }

                override fun onFinish() {
                    try {
                        Log.e("timerStatus", "stop")
                        progressbarCircle.progress = 0
                        tv_Countprogress.text = "00"
                        if (dialog != null && dialog.isShowing)
                            dialog.dismiss()
                        if (countDownTimer != null) {
                            countDownTimer!!.cancel()
                        }
                        clearAllFromMmMapSetDriverMaker()
                        showViewsWhenCurrentRideIsAvailable()
                    } catch (e: Exception) {
                    }
//                    respondRideStatusRequest(REJECT_RIDE_STATUS, rideId)
                }
            }.start()
            countDownTimer!!.start()
            Log.e("checkData", "startDate==$startD===enddate==$endDate===$second")
        } catch (ex: Exception) {
            status = "0"
            ex.printStackTrace()
        }
        return status
    }

    private fun respondRideStatusRequest(response: String, rideRequestId: Int) {
        viewModel.responseRideRequestApi(this, rideRequestId.toString(), response, true)
        viewModel.getResponseRideRequestResposne().observe(this, this)
    }

    private fun distance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val theta = lon1 - lon2
        var dist = (sin(deg2rad(lat1))
                * sin(deg2rad(lat2))
                + (cos(deg2rad(lat1))
                * cos(deg2rad(lat2))
                * cos(deg2rad(theta))))
        dist = acos(dist)
        dist = rad2deg(dist)
        dist *= 60 * 1.1515
        return dist
    }

    private fun deg2rad(deg: Double): Double {
        return deg * Math.PI / 180.0
    }

    private fun rad2deg(rad: Double): Double {
        return rad * 180.0 / Math.PI
    }

    override fun onError(event: String?, vararg args: Any?) {
        Log.d("onError: ", args.toString())
    }

    override fun onResponse(event: String?, vararg args: Any?) {
        try {
            val data = args[0] as JSONObject
            Log.d("onResponse: ", data.toString())
            val jsonData = JSONObject(data.toString())
            updateDriverTakeOrderView(jsonData.getInt("is_online"))
        } catch (e: Exception) {
            Log.d("Exception: ", e.toString())
            Log.d("is_online:--Exception:", e.toString())
        }
    }

    private fun updateDriverTakeOrderView(orderStatus: Int) {
        runOnUiThread {
            if (orderStatus == 0) {
                Log.d("updateDriverTakeOrder: ", orderStatus.toString())
                relativeOnline.visibility = View.GONE
                relativeOffline.visibility = View.VISIBLE
            } else {
                relativeOnline.visibility = View.VISIBLE
                relativeOffline.visibility = View.GONE
            }
        }

//   driverDetails.body.takeOrderStatus = orderStatus
//   savePrefObject(Constants.DRIVER_DETAILS, driverDetails)
    }

}