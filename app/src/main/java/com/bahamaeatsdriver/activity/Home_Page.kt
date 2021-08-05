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
import android.provider.Telephony
import android.util.Log
import android.view.*
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bahamaeats.constant.Constants
import com.bahamaeats.constant.Constants.Companion.ACCEPT_RIDE_STATUS
import com.bahamaeats.constant.Constants.Companion.IMAGE_URL
import com.bahamaeats.constant.Constants.Companion.REJECT_RIDE_STATUS
import com.bahamaeats.network.RestApiInterface
import com.bahamaeats.network.RestObservable
import com.bahamaeats.network.Status
import com.bahamaeatsdriver.Adapter.OrderDetailsAddOnsQuantiytAdapter
import com.bahamaeatsdriver.Adapter.OrderDetailsQuantiytAdapter
import com.bahamaeatsdriver.R
import com.bahamaeatsdriver.activity.Navigation.Contactus_Activity
import com.bahamaeatsdriver.activity.Navigation.Settings_Activity
import com.bahamaeatsdriver.activity.Navigation.TermAnd_Conditions
import com.bahamaeatsdriver.activity.Pofile.My_Profile_Activity
import com.bahamaeatsdriver.activity.login_register.Login_Activity
import com.bahamaeatsdriver.helper.extensions.clearPrefrences
import com.bahamaeatsdriver.helper.extensions.getprefObject
import com.bahamaeatsdriver.helper.extensions.launchActivity
import com.bahamaeatsdriver.helper.extensions.savePrefObject
import com.bahamaeatsdriver.helper.others.CommonMethods
import com.bahamaeatsdriver.helper.others.CommonMethods.convertToNewFormat5
import com.bahamaeatsdriver.helper.others.Helper
import com.bahamaeatsdriver.location.CheckLocationActivity
import com.bahamaeatsdriver.model_class.accept_reject_ride.AcceptRejectRideRequest
import com.bahamaeatsdriver.model_class.add_on_list.AddOnsCustomModel
import com.bahamaeatsdriver.model_class.change_ride_status.ChangeRideStatusResponse
import com.bahamaeatsdriver.model_class.get_current_ride.Body
import com.bahamaeatsdriver.model_class.get_current_ride.GetCurrentRideResponse
import com.bahamaeatsdriver.model_class.get_take_driver_orderstatus.GetTakeDriverOrderStatus
import com.bahamaeatsdriver.model_class.i_am_here.IAmHereResponse
import com.bahamaeatsdriver.model_class.login.LoginResponse
import com.bahamaeatsdriver.model_class.logout.LogoutResponse
import com.bahamaeatsdriver.model_class.map_poliline.Route
import com.bahamaeatsdriver.model_class.update_driver_online_status.UpdateDriverTakeOrderStatus
import com.bahamaeatsdriver.model_class.update_latitudeLongitude.UpdateDriverLatLongResponse
import com.bahamaeatsdriver.model_class.upload_receipt.UploadReceiptResponse
import com.bahamaeatsdriver.repository.BaseViewModel
import com.bahamaeatsdriver.services.LocationMonitoringService
import com.bahamaeatsdriver.services.SensorService
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_home_page.*
import kotlinx.android.synthetic.main.current_job_layout.*
import kotlinx.android.synthetic.main.fragment_navigation_drawl.*
import kotlinx.android.synthetic.main.messenger_popup_layout.*
import kotlinx.android.synthetic.main.notification_on_alert.*
import kotlinx.android.synthetic.main.order_details_dailog.*
import kotlinx.android.synthetic.main.order_details_dailog.tv_paymentMode
import kotlinx.android.synthetic.main.res_pickup_request.*
import org.joda.time.Duration
import org.joda.time.format.DateTimeFormat
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList


class Home_Page : CheckLocationActivity(), OnMapReadyCallback, View.OnClickListener/*, SocketManager.SocketInterface*/, Observer<RestObservable> {

    private var Button_accept: TextView? = null
    private var Button_regect: TextView? = null
    private var btn_orderDetails: TextView? = null
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
    private var checkDriverTakeOrderStatus = 0

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
        if (!mLatitute.isEmpty()) {
            /***
             * Update Location on server
             */
            viewModel.updateDriverLatLongApi(this, mLatitute, mLongitute, false)
            viewModel.getUpdateDriverLatLongResponse().observe(this, this)
            if (googleMap != null) {
                if (!mLatitute.isEmpty()) {
                    if (driverMarker == null)
                        driverMarker = setMarkerdate(mLatitute.toDouble(), mLongitute.toDouble(), R.drawable.car_marker, "You")
                } else {
                    checkPermissionLocation(this)
                }
            }
        }
        if (finishlat != null && finishlong != null && finishlat != 0.0 && finishlong != 0.0 && startlat != 0.0 && startlong != 0.0) {
            drawpoliline(LatLng(startlat!!, startlong!!), LatLng(finishlat!!, finishlong!!), "")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        checkPermissionLocation(this)
        dialog = Dialog(this)
        currentJobOptiondialog = Dialog(this)
        notificationOnDialog = Dialog(this)
        builder = AlertDialog.Builder(this)
        iv_drawable.setOnClickListener(this)
        Relative_Online.setOnClickListener(this)
        Relativ_moveToMapApp.setOnClickListener(this)
        Relative_offline.setOnClickListener(this)
        relativ_livlocation.setOnClickListener(this)
        Relativ_currentloc.setOnClickListener(this)
        Relative_offlineN.setOnClickListener(this)
        Relative_OnlineN.setOnClickListener(this)
        LL_deliveries.setOnClickListener(this)
        LL_paymentstatus.setOnClickListener(this)
        homelayout.setOnClickListener(this)
        LL_support.setOnClickListener(this)
        LL_TandC.setOnClickListener(this)
        LL_settings.setOnClickListener(this)
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
        mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment!!.getMapAsync {
            mapFragment!!.getMapAsync(this);
        }
        val timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                isCall = "true"
            }
        }, 0, 30000)

        mBroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val action = intent.action
                when (action) {
                    "msg" -> if (Helper.isNetworkConnected(this@Home_Page)) {
                        if (!(context as Activity).isFinishing) {
                            if (intent.getStringExtra("type").equals("3")) {
                                currentRideApiCall()
                            }
                        }

                    } else {
                        Toast.makeText(context, "Please check your internet connection", Toast.LENGTH_LONG).show()
                    }
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
        val locationMonitoringService: LocationMonitoringService
        val i: Intent
        val mServiceIntent: Intent
        locationMonitoringService = LocationMonitoringService(activity)
        i = Intent(activity, locationMonitoringService::class.java)
        //And it will be keep running until you close the entire application from task manager.
        //This method will executed only once.
        mSensorService = SensorService(activity)
        mServiceIntent = Intent(activity, SensorService::class.java)
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


    override fun onResume() {
        super.onResume()
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            checkPermissionLocation(this)
            return
        } else {
            //Get driver details
            currentRideApiCall()
            driverDetails = getprefObject(Constants.DRIVER_DETAILS)
            loginDiverId = driverDetails.body.id.toString()
            /***
             * Show Driver available information over views
             */
           /* if (driverDetails.body.takeOrderStatus == 0) {
                Relative_offline.visibility = View.VISIBLE
                Relative_Online.visibility = View.GONE
            } else {
                Relative_Online.visibility = View.VISIBLE
                Relative_offline.visibility = View.GONE
            }*/
            if (driverDetails.body.image.contains("http")) {
                Glide.with(this).load(driverDetails.body.image).placeholder(R.drawable.profileimage).into(iv_Profile_image)
            } else {
                Glide.with(this).load(IMAGE_URL + driverDetails.body.image).placeholder(R.drawable.profileimage).into(iv_Profile_image)
            }
            if (!driverDetails.body.fullName.isEmpty())
                tv_driverName.text = driverDetails.body.fullName
            else
                tv_driverName.text = driverDetails.body.firstName + " " + driverDetails.body.lastName
            getDriverTakeStatusApicall()
            checkNotificationsPermissionIsEnable()
        }
    }

    private fun getDriverTakeStatusApicall() {
        viewModel.getDriverTakeStatusApi(this, false)
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
                notificationOnDialog.window!!.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
                notificationOnDialog.setCancelable(false)
                notificationOnDialog.setCanceledOnTouchOutside(false)
                notificationOnDialog.window!!.setGravity(Gravity.CENTER)
                notificationOnDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                notificationOnDialog.tv_allow.setOnClickListener {
                    notificationOnDialog.dismiss()
                    launchActivity<Settings_Activity>()
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
                if (mLatitute.isNotEmpty()) {
                    driverMarker = setMarkerdate(mLatitute.toDouble(), mLongitute.toDouble(), R.drawable.car_marker, "You")
                }
            }
        } else {
            if (googleMap != null) {
                googleMap!!.clear()
                if (!latRestaurant.isEmpty() && !longRestaurant.isEmpty()) {
                    val sydney = LatLng(latRestaurant.toDouble(), longRestaurant.toDouble())
                    val cameraPosition = CameraPosition.Builder().target(sydney).zoom(12f).build()
                    googleMap!!.addMarker(MarkerOptions().position(sydney).title("Restaurant").icon(BitmapDescriptorFactory.defaultMarker()))
                    googleMap!!.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
                    loadMapOf = 0
                }
            }
        }
        googleMap!!.setTrafficEnabled(false)
        googleMap!!.setIndoorEnabled(false)
        googleMap!!.setBuildingsEnabled(true)
        googleMap!!.getUiSettings().setZoomControlsEnabled(true)
    }

    protected fun createDestinationMarker(latitude: Double, longitude: Double, iconResID: Int, title: String?): Marker? {
        var resizeBitmap = BitmapFactory.decodeResource(getResources(), iconResID)
        resizeBitmap = Bitmap.createScaledBitmap(resizeBitmap!!, 60, 50, true)
        return googleMap!!.addMarker(MarkerOptions().position(LatLng(latitude, longitude)).anchor(0.5f, 0.5f).title(title).icon(BitmapDescriptorFactory.fromBitmap(resizeBitmap)))
    }

    protected fun createMarker(latitude: Double, longitude: Double, iconResID: Int, title: String?): Marker? {
        var resizeBitmap = BitmapFactory.decodeResource(getResources(), iconResID)
        resizeBitmap = Bitmap.createScaledBitmap(resizeBitmap!!, 50, 40, true)
        return googleMap!!.addMarker(MarkerOptions().position(LatLng(latitude, longitude)).anchor(0.5f, 0.5f).title(title).icon(BitmapDescriptorFactory.fromBitmap(resizeBitmap)))
    }

    private fun setMarkerdate(LATITUDE: Double, LONGITUDE: Double, markar: Int, title: String): Marker? {
        val mMarker: Marker?
        mMarker = createMarker(LATITUDE, LONGITUDE, markar, title)
        //first maker camera focuse
        val latLng = LatLng(LATITUDE, LONGITUDE)
        val cameraPosition = CameraPosition.fromLatLngZoom(latLng, 15.0f)
        val cu = CameraUpdateFactory.newCameraPosition(cameraPosition)
        googleMap!!.animateCamera(cu)
        return mMarker
    }

    // todo ------------------------------- Polyline --------------------------------- //
    private var polyLineList: List<LatLng>? = null
    var polylineOptions: PolylineOptions? = null
    var blackPolyline: Polyline? = null
    var greyPolyLine: Polyline? = null
    var blackPolylineOptions: PolylineOptions? = null

    @SuppressLint("CheckResult")
    private fun drawpoliline(pickuplatlng: LatLng, dropofflatlng: LatLng, dropTitle: String) {
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
            val routeList: List<Route> = it.getRoutes()
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

    fun setDropMaker(dropTitle: String) {
        if (dropMarker != null) {
            dropMarker!!.remove()
            dropMarker = null
        }
        dropMarker = googleMap!!.addMarker(MarkerOptions().position(LatLng(finishlat!!.toDouble(), finishlong!!.toDouble())).title(dropTitle).icon(BitmapDescriptorFactory.fromResource(R.drawable.source)))
    }

    private fun drawPolyLineAndAnimateCar(pickuplatlng: LatLng, dropofflatlng: LatLng) {
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
            polylineOptions!!.addAll(polyLineList)
            greyPolyLine = googleMap!!.addPolyline(polylineOptions)
            blackPolylineOptions = PolylineOptions()
            blackPolylineOptions!!.width(9f)
            blackPolylineOptions!!.color(Color.BLACK)
            blackPolylineOptions!!.startCap(SquareCap())
            blackPolylineOptions!!.endCap(SquareCap())
            blackPolylineOptions!!.jointType(JointType.ROUND)
            blackPolyline = googleMap!!.addPolyline(blackPolylineOptions)
            runningdriver = true
        }
    }

    private fun decodePoly(encoded: String): List<LatLng>? {
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

    override fun onClick(view: View?) {
        val itemid = view!!.id
        when (itemid) {
            /* R.id.rl_rootLayoutNew -> {

             }*/
            R.id.Button_Starttrip -> {
                /****
                 * cancel the ride here with change ride status
                 */
                if (!rideRequestId.isEmpty()) {
                    if (isResuarantBePaymentAvailable.isNotEmpty() && isResuarantBePaymentAvailable.equals("1")) {
                        if (isReceiptUpload == 1) changeRideStatusMethod(rideRequestId, Constants.START_RIDE)
                        else
                            Helper.showErrorAlert(this, "Please upload order receipt before start ride")
                    } else
                        changeRideStatusMethod(rideRequestId, Constants.START_RIDE)
                }
            }
            R.id.iv_whatsApp -> {
                val number = currentRideData!!.user.countryCode + currentRideData!!.user.countryCodePhone
                val url = "https://api.whatsapp.com/send?phone=$number"
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(url)
                startActivity(i)
            }
            R.id.tv_uploadReceipt -> {
                checkPermissionCamera(false, "2", "")
            }
            R.id.iv_message -> {
                val number = currentRideData!!.user.countryCodePhone
                /* val number = currentRideData!!.user.countryCodePhone
                 startActivity(Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", number, null)))*/
                val openMessengerDailog = Dialog(this)
                openMessengerDailog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                openMessengerDailog.setContentView(R.layout.messenger_popup_layout)
                openMessengerDailog.window!!.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
                openMessengerDailog.setCancelable(true)
                openMessengerDailog.setCanceledOnTouchOutside(true)
                openMessengerDailog.window!!.setGravity(Gravity.BOTTOM)
                openMessengerDailog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                openMessengerDailog.iv_messageUser.setOnClickListener {
                    openMessengerDailog.dismiss()
                    val uri = Uri.parse("smsto:" + number)
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
                if (!rideRequestId.isEmpty()) {
                    viewModel.changeRideStatusApi(this, rideRequestId, Constants.CANCEL_RIDE, true)
                    viewModel.getChangeRideStatusResposne().observe(this, this)
                }
            }
            R.id.Button_COMPLETEtrip -> {
                /****
                 * cancel the ride here with change ride status
                 */
                if (!rideRequestId.isEmpty()) {
                    viewModel.changeRideStatusApi(this, rideRequestId, Constants.COMPLETE_RIDE, true)
                    viewModel.getChangeRideStatusResposne().observe(this, this)
                }
            }
            R.id.Button_imhear -> {
                /****
                 * I am here API call
                 */
                if (!rideRequestId.isEmpty()) {
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
            R.id.Relative_offline -> {
                viewModel.updateDriverOnlineStatusResposneApi(this, "1", true)
                viewModel.updateDriverOnlineStatusResposne().observe(this, this)
            }
            R.id.Relative_Online -> {
                viewModel.updateDriverOnlineStatusResposneApi(this, "0", true)
                viewModel.updateDriverOnlineStatusResposne().observe(this, this)
            }
            R.id.relativ_livlocation -> {
                if (!mLatitute.isEmpty() && !mLatitute.equals("0.0")) {
                    if (googleMap != null) {
                        val sydney = LatLng(mLatitute.toDouble(), mLongitute.toDouble())
                        val cameraPosition = CameraPosition.Builder().target(sydney).zoom(12f).build()
                        googleMap!!.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
                    }
                } else {
                    checkPermissionLocation(this)
                }

            }
            R.id.Relativ_currentloc -> {
                if (!mLatitute.isEmpty() && !mLatitute.equals("0.0")) {
                    val location = CameraUpdateFactory.newLatLngZoom(LatLng(mLatitute.toDouble(), mLongitute.toDouble()), 15f)
                    googleMap!!.animateCamera(location)
                } else {
                    checkPermissionLocation(this)
                }
            }
            R.id.Relative_offlineN -> {
                Relative_OnlineN!!.setVisibility(View.VISIBLE)
                Relative_offlineN!!.setVisibility(View.GONE)
            }
            R.id.Relativ_moveToMapApp -> {
                if (isGoogleMapsInstalled()) {
                    if (finishlat != null && finishlong != null && finishlat != 0.0 && finishlong != 0.0) {
                        val uri = "http://maps.google.com/maps?saddr=" + mLatitute + "," + mLongitute + "&daddr=" + finishlat + "," + finishlong
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
                        startActivity(intent)
                    }
                } else
                    Helper.showErrorAlert(this, "Unable to find google map. Please intsall the google map")
            }
            R.id.Relative_OnlineN -> {
                Relative_offlineN!!.setVisibility(View.VISIBLE)
                Relative_OnlineN!!.setVisibility(View.GONE)
            }
            R.id.LL_deliveries -> {
                temp = 1
                startActivity(Intent(this, Deliveries_jobhistory::class.java))
            }

            R.id.LL_paymentstatus -> {
                temp = 1
                startActivity(Intent(this, Payment_Status::class.java))
            }
            R.id.homelayout -> {
                temp = 0
                openCloseDrawer()
            }
            R.id.LL_support -> {
                temp = 1
                startActivity(Intent(this, Contactus_Activity::class.java))
            }
            R.id.LL_TandC -> {
                temp = 1
                startActivity(Intent(this, TermAnd_Conditions::class.java))
            }
            R.id.LL_settings -> {
                temp = 1
                startActivity(Intent(this, Settings_Activity::class.java))
            }
            R.id.Relativ_profile -> {
                temp = 1
                launchActivity<My_Profile_Activity>()
            }
            R.id.LL_logout -> {
                temp = 1
                builder!!.setMessage(getString(R.string.logout)).setTitle(getString(R.string.logout))
                builder!!.setMessage(getString(R.string.logout_alert))
                        .setCancelable(false)
                        .setPositiveButton(getString(R.string.yes)) { dialog, id ->
                            viewModel.logoutApi(this, true)
                            viewModel.getlogoutResposne().observe(this, this)
                        }
                        .setNegativeButton(getString(R.string.no)) { dialog, id ->
                            dialog.cancel()
                        }
                val alert = builder!!.create()
                alert.show()
            }
        }
    }

    fun getDefaultSmsAppPackageName(@NonNull context: Context): String? {
        val defaultSmsPackageName: String
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            defaultSmsPackageName = Telephony.Sms.getDefaultSmsPackage(context)
            return defaultSmsPackageName
        } else {
            val intent = Intent(Intent.ACTION_VIEW)
                    .addCategory(Intent.CATEGORY_DEFAULT).setType("vnd.android-dir/mms-sms")
            val resolveInfos = context.packageManager.queryIntentActivities(intent, 0)
            if (resolveInfos != null && !resolveInfos.isEmpty()) return resolveInfos[0].activityInfo.packageName
        }
        return null
    }

    private fun changeRideStatusMethod(rideRequestId: String, rideStatus: String) {
        viewModel.changeRideStatusApi(this, rideRequestId, rideStatus, true)
        viewModel.getChangeRideStatusResposne().observe(this, this)
    }

    private fun isGoogleMapsInstalled(): Boolean {
        return try {
            val info: ApplicationInfo = packageManager.getApplicationInfo("com.google.android.apps.maps", 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    /***
     * Dialog to show order details of current job
     */
    @SuppressLint("SetTextI18n")
    fun showOrderDetailsPopUp(currentRideData: Body) {
        dialogOrderDeatail = Dialog(this@Home_Page)
        dialogOrderDeatail.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialogOrderDeatail.setContentView(R.layout.order_details_dailog)
        dialogOrderDeatail.setCancelable(false)
        /***
         * Set data over Views
         */
        val adapterItemQuantity = OrderDetailsQuantiytAdapter(this, currentRideData.order.orderDetails)
        dialogOrderDeatail.rv_orderItems.setAdapter(adapterItemQuantity)
        dialogOrderDeatail.tv_orderPlaceDate.text = CommonMethods.parseDateToddMMyyyy(currentRideData.order.createdAt, Constants.ORDER_DATE_FORMAT)
        dialogOrderDeatail.tv_user_Name.text = currentRideData.user.firstName + " " + currentRideData.user.lastName
        dialogOrderDeatail.tv_taxLabel.text = "Tax(" + currentRideData.order.taxPercentage + "%)"
        val houseNumber = currentRideData.userAddress.completeAddress
        val streetName = if (!currentRideData.userAddress.streetName.isEmpty()) "/" + currentRideData.userAddress.streetName else ""
        val landmark = if (!currentRideData.userAddress.deliveryInstructions.isEmpty()) "\n" + currentRideData.userAddress.deliveryInstructions else ""
        val userAddres = if (!currentRideData.userAddress.address.isEmpty()) "\n" + currentRideData.userAddress.address else ""
        val finalAddress = houseNumber + streetName + landmark + userAddres
        dialogOrderDeatail.tv_userAddress.text = finalAddress
        dialogOrderDeatail.tv_ContactNumber.text = "P: " + currentRideData.user.countryCodePhone
        dialogOrderDeatail.tv_userEmail.text = "Email: " + currentRideData.user.email
        if (currentRideData.order.preparationTime.isNotEmpty()){
        if (currentRideData.rideStatus == 2) {
            dialogOrderDeatail.tv_preprationTime.visibility=View.GONE
        }
        else if (currentRideData.rideStatus == 1 && currentRideData.response == 0||currentRideData.response == 1) {
            dialogOrderDeatail.tv_preprationTime.visibility=View.VISIBLE
        }else if (currentRideData.rideStatus == 1 && currentRideData.response == 1) {
            dialogOrderDeatail.tv_preprationTime.visibility=View.VISIBLE
        }
        else{
            dialogOrderDeatail.tv_preprationTime.visibility=View.GONE
        }
        dialogOrderDeatail.tv_preprationTime.text = "Prepration time: " + currentRideData.order.preparationTime+" mins"

        }
        dialogOrderDeatail.tv_orderId.text = "#" + currentRideData.order.id

        if (currentRideData.order.tax.isNotEmpty() && currentRideData.order.tax != "0") {
            dialogOrderDeatail.ll_taxRoot.visibility = View.VISIBLE
            dialogOrderDeatail.ll_taxRootView.visibility = View.VISIBLE
        } else {
            dialogOrderDeatail.ll_taxRoot.visibility = View.GONE
            dialogOrderDeatail.ll_taxRootView.visibility = View.GONE
        }
        dialogOrderDeatail.tv_tax.text = "$ " + currentRideData.order.tax

        if (currentRideData.order.serviceFee != 0.0) {
            dialogOrderDeatail.ll_serviceRoot.visibility = View.VISIBLE
            dialogOrderDeatail.ll_serviceRootView.visibility = View.VISIBLE
        } else {
            dialogOrderDeatail.ll_serviceRoot.visibility = View.GONE
            dialogOrderDeatail.ll_serviceRootView.visibility = View.GONE
        }
        dialogOrderDeatail.tv_serviceFee.text = "$ " + currentRideData.order.serviceFee

        dialogOrderDeatail.tv_orderPrice.text = "$ " + Helper.roundOffDecimalNew(currentRideData.order.totalAmount.toFloat())

        dialogOrderDeatail.tv_totalAmountWithAll.text = "$ " + Helper.roundOffDecimalNew(currentRideData.order.netAmount.toFloat())

        if (currentRideData.order.promoDiscount.isNotEmpty() && currentRideData.order.promoDiscount != "0.0") {
            dialogOrderDeatail.ll_promoRoot.visibility = View.VISIBLE
            dialogOrderDeatail.ll_promoRootView.visibility = View.VISIBLE
        } else {
            dialogOrderDeatail.ll_promoRoot.visibility = View.GONE
            dialogOrderDeatail.ll_promoRootView.visibility = View.GONE
        }
        dialogOrderDeatail.tv_promoDiscount.text = "- " + currentRideData.order.promoDiscount

        if (currentRideData.order.cartFee.isNotEmpty() && currentRideData.order.cartFee != "0") {
            dialogOrderDeatail.ll_cartFeeRoot.visibility = View.VISIBLE
            dialogOrderDeatail.ll_cartFeeRootView.visibility = View.VISIBLE
        } else {
            dialogOrderDeatail.ll_cartFeeRoot.visibility = View.GONE
            dialogOrderDeatail.ll_cartFeeRootView.visibility = View.GONE
        }
        dialogOrderDeatail.tv_cartFee.text = "- " + currentRideData.order.cartFee

        if (currentRideData.order.tip.isNotEmpty() && currentRideData.order.tip != "0.0" && currentRideData.order.tip != "0") {
            dialogOrderDeatail.ll_tipRoot.visibility = View.VISIBLE
            dialogOrderDeatail.ll_tipRootView.visibility = View.VISIBLE
        } else {
            dialogOrderDeatail.ll_tipRoot.visibility = View.GONE
            dialogOrderDeatail.ll_tipRootView.visibility = View.GONE
        }
        dialogOrderDeatail.tv_tip.text = "$ " + currentRideData.order.tip
        /** paymentMethod-1 for suncash
         * paymentMethod-2 for paypal
         * paymentMethod-4 for kanoo
         * paymentMethod-5 for Atlantic
         * paymentMethod-6 for IsLand Pay
         * paymentMethod-7 for BE Wallet
         * */
        if (currentRideData.order.paymentMethod.equals("1")) {
            dialogOrderDeatail.tv_paymentMode.text = "Payment Mode: Suncash"
        } else if (currentRideData.order.paymentMethod.equals("2")) {
            dialogOrderDeatail.tv_paymentMode.text = "Payment Mode: Paypal"
        } else if (currentRideData.order.paymentMethod.equals("4")) {
            dialogOrderDeatail.tv_paymentMode.text = "Payment Mode: Kanoo"
        } else if (currentRideData.order.paymentMethod.equals("5")) {
            dialogOrderDeatail.tv_paymentMode.text = "Payment Mode: Atlantic"
        } else if (currentRideData.order.paymentMethod.equals("7")) {
            dialogOrderDeatail.tv_paymentMode.text = "Payment Mode: "+getString(R.string.be_wallet)
        }

        val listAddOnList = ArrayList<AddOnsCustomModel>()
        var count = 0
        for (i in 0 until currentRideData.order.orderDetails.size) {
            for (j in 0 until currentRideData.order.orderDetails.get(i).categories.size) {
                for (k in 0 until currentRideData.order.orderDetails.get(i).categories.get(j).addOnArray.size) {
                    val addOnMOdel = AddOnsCustomModel(currentRideData.order.orderDetails.get(i).categories.get(j).addOnArray.get(k).addon, currentRideData.order.orderDetails.get(i).categories.get(j).addOnArray.get(k).price, currentRideData.order.orderDetails.get(i).quantity)
                    listAddOnList.add(count, addOnMOdel)
                    count++
                }
            }
        }

        if (listAddOnList != null && !listAddOnList.isEmpty()) {
            dialogOrderDeatail.ll_addOnsLabel.visibility = View.VISIBLE
            val adapterItemAddOnsQuantity1 = OrderDetailsAddOnsQuantiytAdapter(this, listAddOnList)
            dialogOrderDeatail.rv_orderAddOnsItems.adapter = adapterItemAddOnsQuantity1
        } else {
            dialogOrderDeatail.ll_addOnsLabel.visibility = View.GONE
        }

        dialogOrderDeatail.btn_ok.setOnClickListener({
            dialogOrderDeatail.dismiss()
        })
        dialogOrderDeatail.show()
    }

    @SuppressLint("RtlHardcoded")
    fun openCloseDrawer() {
        if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
            drawerLayout.closeDrawer(Gravity.LEFT)
        } else {
            drawerLayout.openDrawer(Gravity.LEFT)
        }
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
        /*Relative_Online.visibility = View.VISIBLE
        Relative_offline.visibility = View.GONE*/
        if (checkDriverTakeOrderStatus == 0) {
            Relative_Online.visibility = View.GONE
            Relative_offline.visibility = View.VISIBLE
        } else {
            Relative_Online.visibility = View.VISIBLE
            Relative_offline.visibility = View.GONE
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showViewsWhenRideIsStared() {
        card_pikup.visibility = View.VISIBLE
        rl_bottomOptionsRoot.visibility = View.GONE
        layoutCurrentJob.visibility = View.VISIBLE
        /* Relative_offline.visibility = View.GONE
         Relative_Online.visibility = View.VISIBLE*/
        /***
         * Show Driver available information
         */
        if (checkDriverTakeOrderStatus== 0) {
            Relative_Online.visibility = View.GONE
            Relative_offline.visibility = View.VISIBLE
        } else {
            Relative_Online.visibility = View.VISIBLE
            Relative_offline.visibility = View.GONE
        }
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
        /***
         * Show Driver available information
         */
        if (checkDriverTakeOrderStatus== 0) {
            Relative_Online.visibility = View.GONE
            Relative_offline.visibility = View.VISIBLE
        } else {
            Relative_Online.visibility = View.VISIBLE
            Relative_offline.visibility = View.GONE
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onChanged(liveData: RestObservable?) {
        when (liveData!!.status) {
            Status.SUCCESS -> {
                if (liveData.data is GetTakeDriverOrderStatus) {
                    checkDriverTakeOrderStatus=liveData.data.body.takeOrderStatus
                    if (checkDriverTakeOrderStatus == 0) {
                        Relative_offline.visibility = View.VISIBLE
                        Relative_Online.visibility = View.GONE
                    } else {
                        Relative_Online.visibility = View.VISIBLE
                        Relative_offline.visibility = View.GONE
                    }
                }
                if (liveData.data is UploadReceiptResponse) {
                    isReceiptUpload = 1
                    Helper.showSuccessToast(this, liveData.data.message)
                    tv_uploadReceipt.text = getString(R.string.uploaded_receipt)
                }
                if (liveData.data is UpdateDriverLatLongResponse) {
                    Log.e("updateDriverLatLng", "success" + liveData.data.message)
                    if (finishlat != null && finishlong != null && finishlat != 0.0 && finishlong != 0.0) {
                        drawpoliline(LatLng(startlat!!, startlong!!), LatLng(finishlat!!, finishlong!!), "Restaurant")
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
                            latRestaurantAcceptJob = liveData.data.body.restaurant.latitude.toString()
                            longRestaurantAcceptJob = liveData.data.body.restaurant.longitude.toString()
                            Log.e("AcceptJob:..mLatitute: ", mLatitute + "mLongitute: " + mLongitute + "restaurant.latitude: " + liveData.data.body.restaurant.latitude + "restaurant.longitude" + liveData.data.body.restaurant.longitude)
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
                        Glide.with(this).load(currentRideData!!.user.photo).placeholder(R.drawable.placeholder_rectangle).into(Image_profile)
                        tv_currentOrderUsename.text = currentRideData!!.user.username
                        tv_currentOrderId.text = "Order ID: #" + currentRideData!!.order.id

                        /** paymentMethod-1 for suncash
                         * paymentMethod-2 for paypal
                         * paymentMethod-4 for kanoo
                         * paymentMethod-5 for Atlantic*/
                        if (currentRideData!!.order.paymentMethod.equals("1")) {
                            tv_currentOrderPaymentMode.text = "Payment Mode: Suncash"
                        } else if (currentRideData!!.order.paymentMethod.equals("2")) {
                            tv_currentOrderPaymentMode.text = "Payment Mode: Paypal"
                        } else if (currentRideData!!.order.paymentMethod.equals("4")) {
                            tv_currentOrderPaymentMode.text = "Payment Mode: Kanoo"
                        } else if (currentRideData!!.order.paymentMethod.equals("5")) {
                            tv_currentOrderPaymentMode.text = "Payment Mode: Atlantic"
                        } else if (currentRideData!!.order.paymentMethod.equals("6")) {
                            tv_currentOrderPaymentMode.text = "Payment Mode: IsLand Pay"
                        } else if (currentRideData!!.order.paymentMethod.equals("7")) {
                            tv_currentOrderPaymentMode.text = "Payment Mode: "+getString(R.string.be_wallet)
                        }
                        tv_currentOrderTotal.text = "$" + Helper.roundOffDecimalNew(currentRideData!!.order.netAmount.toFloat())
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
                                drawpoliline(LatLng(startlat!!, startlong!!), LatLng(finishlat!!, finishlong!!), "Restaurant")
                                setDropMaker("Restaurant")
                            }
                            tv_adress.text = currentRideData!!.restaurant.address
                            showViewsWhenRideIsAccepted()
                            showHildeUploadReceiptView(currentRideData!!.order.receiptUpload, currentRideData!!.restaurant.isBe, "")
                        }
                        //when ride is accepted by the driver
                        else if (currentRideData!!.rideStatus == 2) {
                            finishlat = currentRideData!!.userAddress.latitude
                            finishlong = currentRideData!!.userAddress.longitude

                            val houseNumber = currentRideData!!.userAddress.completeAddress
                            val streetName = if (!currentRideData!!.userAddress.streetName.isEmpty()) "/" + currentRideData!!.userAddress.streetName else ""
                            val landmark = if (!currentRideData!!.userAddress.deliveryInstructions.isEmpty()) "\n" + currentRideData!!.userAddress.deliveryInstructions else ""
                            val userAddres = if (!currentRideData!!.userAddress.address.isEmpty()) "\n" + currentRideData!!.userAddress.address else ""
                            val finalAddress = houseNumber + streetName + landmark + userAddres
                            tv_adress.text = finalAddress
                            showViewsWhenRideIsStared()
                            tv_adress.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                            Iv_search.visibility = View.GONE
                            if (dropMarker != null)
                                dropMarker!!.remove()
                            drawpoliline(LatLng(startlat!!, startlong!!), LatLng(finishlat!!, finishlong!!), "Deliver to")
                            setDropMaker("Deliver to")
                            Log.e("GetCur:..mLatitute: ", currentRideData!!.fromLat + "mLongitute: " + currentRideData!!.fromLong + "restaurant.latitude: " + liveData.data.body.restaurant.latitude + "restaurant.longitude" + liveData.data.body.restaurant.longitude)
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
                    val NM = getSystemService(AppCompatActivity.NOTIFICATION_SERVICE) as NotificationManager
                    NM.cancelAll()
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
                    currentRideData!!.response=changeRideStatus!!.response
                    currentRideData!!.rideStatus=changeRideStatus!!.rideStatus

                    Glide.with(this).load(changeRideStatus!!.user.photo).placeholder(R.drawable.placeholder_rectangle).into(Image_profile)
                    tv_currentOrderUsename.text = changeRideStatus!!.user.username
                    tv_currentOrderId.text = "Order ID: #" + changeRideStatus!!.order.id
//                    tv_currentOrderPaymentMode.text = "Online"
                    /**
                     * paymentMethod-1 for suncash
                     * paymentMethod-2 for paypal
                     * paymentMethod-4 for kanoo
                     * paymentMethod-5 for Atlantic
                     * paymentMethod-6 for IsLand Pay
                     * paymentMethod-7 for be_wallet
                     */
                    if (changeRideStatus!!.order.paymentMethod == 1) {
                        tv_currentOrderPaymentMode.text = "Payment Mode: Suncash"
                    } else if (changeRideStatus!!.order.paymentMethod == 2) {
                        tv_currentOrderPaymentMode.text = "Payment Mode: Paypal"
                    } else if (changeRideStatus!!.order.paymentMethod == 4) {
                        tv_currentOrderPaymentMode.text = "Payment Mode: Kanoo"
                    } else if (changeRideStatus!!.order.paymentMethod == 5) {
                        tv_currentOrderPaymentMode.text = "Payment Mode: Atlantic"
                    } else if (changeRideStatus!!.order.paymentMethod == 6) {
                        tv_currentOrderPaymentMode.text = "Payment Mode: IsLand Pay"
                    } else if (changeRideStatus!!.order.paymentMethod == 7) {
                        tv_currentOrderPaymentMode.text ="Payment Mode: "+ getString(R.string.be_wallet)
                    }
                    tv_currentOrderTotal.text = "$" + Helper.roundOffDecimalNew(changeRideStatus!!.order.netAmount.toFloat())
                    val houseNumber = changeRideStatus!!.userAddress.completeAddress
                    val streetName = if (!changeRideStatus!!.userAddress.streetName.isEmpty()) "/" + changeRideStatus!!.userAddress.streetName else ""
                    val landmark = if (!changeRideStatus!!.userAddress.deliveryInstructions.isEmpty()) "\n" + changeRideStatus!!.userAddress.deliveryInstructions else ""
                    val userAddres = if (!changeRideStatus!!.userAddress.address.isEmpty()) "\n" + changeRideStatus!!.userAddress.address else ""
                    val finalAddress = houseNumber + streetName + landmark + userAddres
                    tv_adress.text = finalAddress
                    //rideStatus=2 when  driver click to start ride
                    if (changeRideStatus!!.rideStatus == 2) {
                        Helper.showSuccessAlert(this, liveData.data.message)
                        anyActiveJobAvailable = 1
                        finishlat = changeRideStatus!!.userAddress.latitude
                        finishlong = changeRideStatus!!.userAddress.longitude
                        val houseNumber = changeRideStatus!!.userAddress.completeAddress
                        val streetName = if (!changeRideStatus!!.userAddress.streetName.isEmpty()) "/" + changeRideStatus!!.userAddress.streetName else ""
                        val landmark = if (!changeRideStatus!!.userAddress.deliveryInstructions.isEmpty()) "\n" + changeRideStatus!!.userAddress.deliveryInstructions else ""
                        val userAddres = if (!changeRideStatus!!.userAddress.address.isEmpty()) "\n" + changeRideStatus!!.userAddress.address else ""
                        val finalAddress = houseNumber + streetName + landmark + userAddres
                        tv_adress.text = finalAddress
                        showViewsWhenRideIsStared()
                        tv_adress.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                        Iv_search.visibility = View.GONE
                        if (dropMarker != null) {
                            dropMarker!!.remove()
                            dropMarker = null
                        }
                        drawpoliline(LatLng(startlat!!, startlong!!), LatLng(finishlat!!, finishlong!!), "Deliver to")
                        setDropMaker("Deliver to")
                        Log.e("GetCur:..mLatitute: ", changeRideStatus!!.fromLat + "mLongitute: " + changeRideStatus!!.fromLong + "restaurant.latitude: " + liveData.data.body.restaurant.latitude + "restaurant.longitude" + liveData.data.body.restaurant.longitude)
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
                    checkDriverTakeOrderStatus=liveData.data.body.takeOrderStatus
                    driverDetails.body.takeOrderStatus = liveData.data.body.takeOrderStatus
                    savePrefObject(Constants.DRIVER_DETAILS, driverDetails)
                    if (checkDriverTakeOrderStatus== 0) {
                        Relative_Online.visibility = View.GONE
                        Relative_offline.visibility = View.VISIBLE
                    } else {
                        Relative_Online.visibility = View.VISIBLE
                        Relative_offline.visibility = View.GONE
                    }
                }
            }

            Status.ERROR -> {

            }
            else -> {

            }
        }
    }

    private fun showHildeUploadReceiptView(receiptUpload: String, bePaymentAvailable: String, from: String) {
        /**
         * from isEmpty=== use method called when ride is accepted
         */
        if (from.isEmpty()) {
            if (bePaymentAvailable == "1") {
                tv_uploadReceipt.visibility = View.VISIBLE
                if (receiptUpload.isEmpty()) {
                    tv_uploadReceipt.text = getString(R.string.upload_receipt)
                } else {
                    tv_uploadReceipt.text = getString(R.string.uploaded_receipt)
                }
            } else tv_uploadReceipt.visibility = View.GONE
        } else {
            if (bePaymentAvailable == "1") {
                if (receiptUpload.isEmpty()) {
                    tv_uploadReceipt.visibility = View.VISIBLE
                } else {
                    tv_uploadReceipt.visibility = View.GONE
                }
            }
        }
    }

    private fun clearAllFromMmMapSetDriverMaker() {
        finishlat = 0.0
        finishlong = 0.0
        otherUserId = ""
        if (googleMap != null) {
            googleMap!!.clear()
            driverMarker = setMarkerdate(mLatitute.toDouble(), mLongitute.toDouble(), R.drawable.car_marker, "You")
        }
    }

    override fun selectedImage(imagePath: String?, thumbnailVideoPath: String) {
        image_path = imagePath!!
        viewModel.uploadReceiptApi(this, image_path, currentRideData!!.orderId.toString(), true)
        viewModel.getUploadReceiptResponse().observe(this, this)
    }

    override fun getUpdatedPhoneNoAfterVerify(contactNumber: String, updatedCountryCode: String) {
//Not in use
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        return super.dispatchTouchEvent(ev)
    }

    fun createZoomRoute(driverLatLong: LatLng, dropLatLong: LatLng) {
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
            dialog.window!!.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
            dialog.setContentView(R.layout.res_pickup_request)
            dialog.setCancelable(false)
            Button_accept = dialog.findViewById(R.id.Button_accept)
            Button_regect = dialog.findViewById(R.id.Button_regect)
            btn_orderDetails = dialog.findViewById(R.id.btn_orderDetails)
            /***
             * Be Card Payment View
             */
            if (body.restaurant.isBe == "0") dialog.tv_useBeCardForPayment.visibility = View.GONE else dialog.tv_useBeCardForPayment.visibility = View.VISIBLE
            latRestaurant = body.restaurant.latitude.toString()
            longRestaurant = body.restaurant.longitude.toString()
            dialog.tv_restaurantName.text = body.restaurant.address
            val houseNumber = body.userAddress.completeAddress
            val streetName = if (!body.userAddress.streetName.isEmpty()) "/" + body.userAddress.streetName else ""
            val landmark = if (!body.userAddress.deliveryInstructions.isEmpty()) "\n" + body.userAddress.deliveryInstructions else ""
            val userAddres = if (!body.userAddress.address.isEmpty()) "\n" + body.userAddress.address else ""
            val finalAddress = houseNumber + streetName + landmark + userAddres
            dialog.tv_userOrderAddress.text = finalAddress
            dialog.tv_totalAmount.text = "$" + Helper.roundOffDecimalNew(body.order.netAmount.toFloat())
            dialog.tv_minTimeToDeliver.text = body.restaurant.minDelivery + " mins"
            try {
                if (body.restaurant.longitude != 0.0 && body.userAddress.latitude != 0.0) {
                    val value1: String = java.lang.String.valueOf(DecimalFormat("##").format(distance(body.restaurant.latitude, body.restaurant.longitude, body.userAddress.latitude, body.userAddress.longitude)))
                    dialog.tv_minDeliverDistance.text = value1 + " mi"
                }
                if (!mLatitute.isEmpty() && body.userAddress.latitude != 0.0) {
                    //Distance from Driver location to restaurant +and+ restaurant to user order delivery Address
                    val distancefromDriverToRestaurant = distance(mLatitute.toDouble(), mLongitute.toDouble(), body.restaurant.latitude, body.restaurant.longitude)
                    val restaurantToUser = distance(body.restaurant.latitude, body.restaurant.longitude, body.userAddress.latitude, body.userAddress.longitude)
                    val finalDistance = distancefromDriverToRestaurant + restaurantToUser
                    val finalDistanceNew: String = java.lang.String.valueOf(DecimalFormat("##").format(finalDistance))
                    dialog.tv_totalDistance.text = finalDistanceNew + " mi"
                }
            } catch (e: Exception) {
            }
            Button_accept?.setOnClickListener {
                //response :1 to accept the job
                respondRideStatusRequest(ACCEPT_RIDE_STATUS, body.id)
                val NM = getSystemService(AppCompatActivity.NOTIFICATION_SERVICE) as NotificationManager
                NM.cancelAll()
            }
            Button_regect?.setOnClickListener {
                //response :2 to reject the job
                respondRideStatusRequest(REJECT_RIDE_STATUS, body.id)
                val NM = getSystemService(AppCompatActivity.NOTIFICATION_SERVICE) as NotificationManager
                NM.cancelAll()
            }
            btn_orderDetails?.setOnClickListener {
                if (currentRideData != null && currentRideData!!.id != 0) {
                    showOrderDetailsPopUp(currentRideData!!)
                }
            }
            dateCheckMonth(timePrint(body.createdAt), getCurrentDate(), dialog.tv_countProgress, dialog.progressBarCircle, body.id)
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

    fun getCurrentDate(): String {
        val c = Calendar.getInstance().time
        println("Current time => $c")
        val df = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
        return df.format(c)
    }

    fun dateCheckMonth(startD: String, endDate: String, tv_Countprogress: TextView, progressbarCircle: ProgressBar, rideId: Int): String {
        var status = "0"
        try {
            val formatter = DateTimeFormat.forPattern("yyyy-MM-dd hh:mm:ss")
            val d1 = formatter.parseDateTime(startD)
            val d2 = formatter.parseDateTime(endDate)
            val duration = Duration(d1, d2)
            val second = duration.standardSeconds
            var countDownTimer = object : CountDownTimer(90 * 1000 - second * 1000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    Log.e("timerStatus", "inprogress" + "==" + "" + (millisUntilFinished / 1000).toInt())
                    tv_Countprogress.setText((millisUntilFinished / 1000).toInt().toString())
                    progressbarCircle.setProgress((millisUntilFinished / 1000).toInt())
                }

                override fun onFinish() {
                    Log.e("timerStatus", "stop")
                    progressbarCircle.setProgress(0)
                    tv_Countprogress.setText("00")
                    if (dialog != null && dialog.isShowing)
                        dialog.dismiss()
                    if (countDownTimer != null) {
                        countDownTimer!!.cancel();
                    }
                    respondRideStatusRequest(REJECT_RIDE_STATUS, rideId)
                }
            }.start()
            countDownTimer!!.start()
            Log.e("checkData", "startDate==" + startD + "===" + "enddate==" + endDate + "===" + second)
        } catch (ex: Exception) {
            status = "0"
            ex.printStackTrace()
        }
        return status
    }

    private fun hmsTimeFormatter(milliSeconds: Long): String? {
        /* return String.format(
                 "%02d:%02d:%02d",
                 TimeUnit.MILLISECONDS.toHours(milliSeconds),
                 TimeUnit.MILLISECONDS.toMinutes(milliSeconds) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(milliSeconds)),
                 TimeUnit.MILLISECONDS.toSeconds(milliSeconds) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliSeconds))
         )*/
//        return  String.format("%02d", TimeUnit.MILLISECONDS.toSeconds(milliSeconds) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliSeconds))
        return String.format("%02d", TimeUnit.MILLISECONDS.toSeconds(milliSeconds) % 60)
    }

    private fun respondRideStatusRequest(response: String, rideRequestId: Int) {
        viewModel.responseRideRequestApi(this, rideRequestId.toString(), response, true)
        viewModel.getResponseRideRequestResposne().observe(this, this)
    }

    private fun distance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val theta = lon1 - lon2
        var dist = (Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + (Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta))))
        dist = Math.acos(dist)
        dist = rad2deg(dist)
        dist = dist * 60 * 1.1515
        return dist
    }

    private fun deg2rad(deg: Double): Double {
        return deg * Math.PI / 180.0
    }

    private fun rad2deg(rad: Double): Double {
        return rad * 180.0 / Math.PI
    }

    /*  override fun onDestroy() {
          if (::mSensorService.isInitialized) {
              stopService(mServiceIntent)
          }
          super.onDestroy()
      }*/

}