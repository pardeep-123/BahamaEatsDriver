package com.bahamaeatsdriver.fcm

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.bahamaeats.constant.Constants
import com.bahamaeatsdriver.R
import com.bahamaeatsdriver.activity.Home_Page
import com.bahamaeatsdriver.helper.extensions.saveTokenPrefrence
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {
    private val TAG = "MyFirebaseMsgService"
    private var i = 0
    private var CHANNEL_ID = "" // The id of the channel.
    private var CHANNEL_ONE_NAME = "Channel One"
    private var notificationManager: NotificationManager? = null
    private var notificationChannel: NotificationChannel? = null
    private var notification: Notification? = null

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "Refreshed token: $token")
        val tokenDevice = FirebaseInstanceId.getInstance().token
        if (tokenDevice != null && !tokenDevice.isEmpty()) {
            sendRegistrationToServer(tokenDevice)
        }
    }


    private fun sendRegistrationToServer(deviceToken: String) {
        saveTokenPrefrence(Constants.DEVICE_TOKEN, deviceToken)
        Log.e("device_token", deviceToken)
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        getManager()
        CHANNEL_ID = applicationContext.packageName
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_ONE_NAME,
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationChannel!!.description = ""
            notificationChannel!!.enableLights(true)
            notificationChannel!!.lightColor = Color.RED
            notificationChannel!!.setShowBadge(true)
            notificationChannel!!.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            notificationChannel!!.shouldVibrate()
            notificationChannel!!.vibrationPattern = longArrayOf(100, 200, 300, 400, 500)
            notificationChannel!!.enableVibration(true)
        }
        Log.e("data", "==" + remoteMessage.data)
//        val json = JSONObject(remoteMessage.data.get("body"))
        if (remoteMessage.data.get("notification_code") != null) {

            val notification_code = remoteMessage.data.get("notification_code")
            val message = remoteMessage.data.get("message")
            val title = remoteMessage.data.get("title")
            if (notification_code.equals("3")) {
                sendMessagePush(notification_code!!, message!!, title!!)
            } else if (notification_code.equals("10")) {
                sendMessagePush(notification_code!!, message!!, title!!)
            }
        } /*else if (json.getString("code") != null) {
            val code = json.getString("code")
            val message = json.getString("message")
            sendMessagePush(code, message, getString(R.string.app_name))
        }*/
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private fun sendMessagePush(type: String, message: String, title: String) {
        var notificationHeader = ""
        /***
         * notification_code == 8
        Then show simle notification message
        notification_code == 3. the getRideDetails api hit krni ride id notification ch ayegi(Accpet reject vali screen)
         */
        if (type == "10")
            notificationHeader = title
        else
            notificationHeader = getString(R.string.app_name)


        val intent = Intent(this, Home_Page::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        val pendingIntent = PendingIntent.getActivity(this, i, intent, 0)
        val icon1 = BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(getNotificationIcon()).setLargeIcon(icon1)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setColor(ContextCompat.getColor(applicationContext, R.color.White))
            .setContentTitle(notificationHeader)
            .setDefaults(Notification.DEFAULT_ALL)
            .setContentText(message)
            .setTicker("Notification test")
            .setAutoCancel(true)
        notificationBuilder.setVibrate(longArrayOf(100, 200, 300, 400, 500))
        notificationBuilder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
        notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH).setSound(defaultSoundUri).setContentIntent(pendingIntent)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager!!.createNotificationChannel(notificationChannel!!)
        }
        val openIntent = Intent(this, Home_Page::class.java)
        openIntent.action = "View"
        openIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        val openPendingIntent = PendingIntent.getActivity(this, 0, openIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val openDismissAction = NotificationCompat.Action(0, "View", openPendingIntent)
        notificationBuilder.addAction(openDismissAction)
        notification = notificationBuilder.build()
        notificationManager!!.notify(i++, notification)
        val i = Intent("msg") //action: "msg"
        i.setPackage(packageName)
        i.putExtra("type", type)
        applicationContext.sendBroadcast(i)
    }


    private fun getManager(): NotificationManager? {
        if (notificationManager == null) {
            notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        }
        return notificationManager
    }

    @SuppressLint("ObsoleteSdkInt")
    private fun getNotificationIcon(): Int {
        val useWhiteIcon = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
        return if (useWhiteIcon) R.mipmap.ic_launcher else R.mipmap.ic_launcher
    }
}