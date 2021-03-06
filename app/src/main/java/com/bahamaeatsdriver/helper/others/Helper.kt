package com.bahamaeatsdriver.helper.others

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.ConnectivityManager
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.Toast
import com.bahamaeats.listeners.OnNoInternetConnectionListener
import com.bahamaeatsdriver.R
import com.tapadoo.alerter.Alerter
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin


object Helper {

    @JvmStatic
    fun showToast(context: Context, msg: Int) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    @JvmStatic
    fun showSuccessToast(context: Context, msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    fun removeExtraString(toString: String): String {
        return toString
                .replace("[", "") //remove the right bracket
                .replace("]", "") //remove the left bracket
                .trim { it <= ' ' }
    }

    @JvmStatic
    fun showErrorAlert(context: Activity, msg: String) {
        Alerter.create(context)
                .setTitle(context.getString(R.string.error_))
                .setTitleAppearance(R.style.AlertTextAppearanceTitle)
                .setText(msg)
                .setTextAppearance(R.style.AlertTextAppearanceText)
                .setBackgroundColorRes(R.color.colorRed)
                .show()

    }
    @JvmStatic
    fun showErrorAlertWithoutTitle(context: Activity, msg: String) {
        Alerter.create(context)
                .setText(msg)
                .setTextAppearance(R.style.AlertTextAppearanceText)
                .setBackgroundColorRes(R.color.colorRed)
                .show()

    }

    @JvmStatic
    fun showSuccessAlert(context: Activity, msg: String) {
        Alerter.create(context)
                .setText(msg)
                .setTextAppearance(R.style.AlertTextAppearanceText)
                .setBackgroundColorRes(R.color.Greenapp)
                .show()
    }


    @JvmStatic
    fun showNoInternetAlert(
            context: Activity,
            msg: String,
            listener: OnNoInternetConnectionListener
    ) {
        Alerter.create(context)
                .setTitle(context.getString(R.string.error_))
                .setTitleAppearance(R.style.AlertTextAppearanceTitle)
                .setText(msg)
                .setTextAppearance(R.style.AlertTextAppearanceText)
                .setBackgroundColorRes(R.color.Greenapp)
                .addButton(
                        context.getString(R.string.retry),
                        R.style.AlertButton,
                        View.OnClickListener {
                            listener.onRetryApi()
                        })
                .show()
    }


    @JvmStatic
    fun isNetworkConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo != null
    }

    @SuppressLint("Recycle")
    fun getAbsolutePath(activity: Context, uri: Uri): String {
        if ("content".equals(uri.scheme, ignoreCase = true)) {
            val projection = arrayOf("_data")
            val cursor: Cursor?
            try {
                cursor = activity.contentResolver.query(uri, projection, null, null, null)
                val columnIndex = cursor!!.getColumnIndexOrThrow("_data")
                if (cursor.moveToFirst()) {
                    return cursor.getString(columnIndex)
                }
            } catch (e: Exception) {
                // Eat it
                e.printStackTrace()
            }
        } else if ("file".equals(uri.scheme, ignoreCase = true)) {
            return uri.path!!
        }
        return ""
    }
    fun roundOffDecimalNew(number: Float): Double? {
        val df = DecimalFormat("##.00")
        return df.format(number).toDouble()
    }

    @JvmStatic
    fun getCurrentDate(): String {
        val c = Calendar.getInstance().time
        println("Current time => $c")

        val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        return df.format(c)
    }

    fun getCurrentTimezoneOffset(): String? {
        val tz = TimeZone.getDefault()
        val cal = GregorianCalendar.getInstance(tz)
        val offsetInMillis = tz.getOffset(cal.timeInMillis) / 1000
        var offset = String.format("%02d:%02d", abs(offsetInMillis * 1000 / 3600000), abs(offsetInMillis * 1000 / 60000 % 60))
        offset = "GMT" + (if (offsetInMillis >= 0) "+" else "-") + offset
        Log.d("offset", offset)
        return /*offset*/offsetInMillis.toString()
    }

    fun isGoogleMapsInstalled(context: Activity): Boolean {
        return try {
            val info: ApplicationInfo = context.packageManager.getApplicationInfo("com.google.android.apps.maps", 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

     fun distance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
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

   private  fun deg2rad(deg: Double): Double {
        return deg * Math.PI / 180.0
    }

    private fun rad2deg(rad: Double): Double {
        return rad * 180.0 / Math.PI
    }

}