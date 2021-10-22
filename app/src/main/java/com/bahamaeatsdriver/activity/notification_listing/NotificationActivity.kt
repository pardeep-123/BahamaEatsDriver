package com.bahamaeatsdriver.activity.notification_listing

import android.app.Activity
import android.app.Dialog
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bahamaeats.network.RestObservable
import com.bahamaeats.network.Status
import com.bahamaeatsdriver.adapter.NotificationAdapter
import com.bahamaeatsdriver.R
import com.bahamaeatsdriver.helper.others.Helper
import com.bahamaeatsdriver.listeners.DeleteUserNotificationOnClickEvent
import com.bahamaeatsdriver.model_class.delete_notifications.DeleteNotificationResponse
import com.bahamaeatsdriver.model_class.notification_listing.Body
import com.bahamaeatsdriver.model_class.notification_listing.NotificationListingResponse
import com.bahamaeatsdriver.repository.BaseViewModel
import kotlinx.android.synthetic.main.activity_notification2.*
import kotlinx.android.synthetic.main.clear_notification_alert.*

class NotificationActivity : AppCompatActivity(), Observer<RestObservable>,
    DeleteUserNotificationOnClickEvent {

    private var notificationList = ArrayList<Body>()
    private lateinit var clearNotiticationsDialog: Dialog
    private val viewModel: BaseViewModel by lazy { ViewModelProvider(this).get(BaseViewModel::class.java) }
    private var deleteNotificationPos = -1
    private lateinit var notificationAdapter: NotificationAdapter
    private var mBroadcastReceiver: BroadcastReceiver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification2)
        iv_backBtn.setOnClickListener {
            finish()
        }
        tv_clear.setOnClickListener {
            if (notificationList != null && notificationList.size > 0)
                clearNotificationsDailogMethod()
            else
                Helper.showSuccessToast(this, getString(R.string.no_notification_available))
        }
        mBroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                when (intent.action) {
                    "msg" -> if (Helper.isNetworkConnected(this@NotificationActivity)) {
                        if (!(context as Activity).isFinishing) {
                            if (intent.getStringExtra("type")!! == "10") {
                                val notificationManager =
                                    getSystemService(NOTIFICATION_SERVICE) as NotificationManager
                                notificationManager.cancelAll()
                                viewModel.getNotificationsListApi(this@NotificationActivity, true)
                                viewModel.getNotificationsResposne().observe(this@NotificationActivity, this@NotificationActivity)
                            }
                        }

                    } else
                        Toast.makeText(context, getString(R.string.internet_error), Toast.LENGTH_LONG).show()

                }
            }
        }
        val filter = IntentFilter("msg")
        registerReceiver(mBroadcastReceiver, filter)

        /***
         * Get Notification listing
         */
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancelAll()
        viewModel.getNotificationsListApi(this, true)
        viewModel.getNotificationsResposne().observe(this, this)
    }


    private fun clearNotificationsDailogMethod() {
        clearNotiticationsDialog = Dialog(this, R.style.Theme_Dialog)
        clearNotiticationsDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        clearNotiticationsDialog.setContentView(R.layout.clear_notification_alert)
        clearNotiticationsDialog.window!!.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
        clearNotiticationsDialog.setCancelable(true)
        clearNotiticationsDialog.setCanceledOnTouchOutside(false)
        clearNotiticationsDialog.window!!.setGravity(Gravity.CENTER)
        clearNotiticationsDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        clearNotiticationsDialog.tv_yes.setOnClickListener {
            clearNotiticationsDialog.dismiss()
            viewModel.deleteUserNotificationApi(this, "", true)
            viewModel.deleteUserNotificationResponse().observe(this, this)
        }
        clearNotiticationsDialog.tv_no.setOnClickListener { clearNotiticationsDialog.dismiss() }
        clearNotiticationsDialog.show()
    }

    override fun onChanged(liveData: RestObservable?) {
        when (liveData!!.status) {
            Status.SUCCESS -> {
                if (liveData.data is NotificationListingResponse) {
                    if (liveData.data.body.isEmpty()) {
                        rv_notification.visibility = View.GONE
                        tv_no_notification.visibility = View.VISIBLE
                    } else {
                        rv_notification.visibility = View.VISIBLE
                        tv_no_notification.visibility = View.GONE
                        notificationList = liveData.data.body
                        notificationAdapter = NotificationAdapter(this, notificationList, this)
                        rv_notification.adapter = notificationAdapter
                    }
                }
                /*if  if (liveData.data is DeleteAllNotificationsResponse) {
                      notificationList.clear()
                      rv_notification.visibility = View.GONE
                      tv_no_notification.visibility = View.VISIBLE
                      Helper.showSuccessAlert(this, liveData.data.message)
                  }
                  if (liveData.data is ReadNotification) {
                      notificationList[pos].isRead = 1
                      notificationAdapter.notifyDataSetChanged()
                      if (notificationList[pos].code == 12) {
                          launchActivity<WalletActivity>()
                      } else {
                          if (notificationDetails.restaurantId != 0) {
                              launchActivity<MainActivity>()
                              {
                                  putExtra("from", "NotificationActivity")
                                  putExtra("notifcationData", notificationDetails)
                                  putExtra("notifcationCode", notificationDetails.code)
                              }
                          }

                      }

                  }
                  */
                if (liveData.data is DeleteNotificationResponse) {
                    Helper.showSuccessAlert(this, liveData.data.message)
                    /****
                     * deleteNotificationPos =0 means all notifications are deleted
                     * deleteNotificationPos =1 particular notification is deleted
                     */
                    if (deleteNotificationPos != -1) {
                        notificationList.removeAt(deleteNotificationPos)
                        notificationAdapter.notifyDataSetChanged()
                        if (notificationList.isEmpty()) {
                            rv_notification.visibility = View.GONE
                            tv_no_notification.visibility = View.VISIBLE
                        } else {
                            rv_notification.visibility = View.VISIBLE
                            tv_no_notification.visibility = View.GONE
                        }
                        deleteNotificationPos = -1
                    } else {
                        notificationList.clear()
                        rv_notification.visibility = View.GONE
                        tv_no_notification.visibility = View.VISIBLE
                    }

                }
            }
            Status.ERROR -> {

            }
            else -> {

            }
        }
    }

    override fun onClickDeleteUserNotifcation(postition: Int, notificationId: Int) {
        deleteNotificationPos = postition
        val deleteNotiticationsDialog = Dialog(this, R.style.Theme_Dialog)
        deleteNotiticationsDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        deleteNotiticationsDialog.setContentView(R.layout.clear_notification_alert)
        deleteNotiticationsDialog.window!!.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
        deleteNotiticationsDialog.setCancelable(true)
        deleteNotiticationsDialog.setCanceledOnTouchOutside(false)
        deleteNotiticationsDialog.window!!.setGravity(Gravity.CENTER)
        deleteNotiticationsDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        deleteNotiticationsDialog.tv_message.text = getString(R.string.clear_notification_alert)
        deleteNotiticationsDialog.tv_yes.setOnClickListener {
            deleteNotiticationsDialog.dismiss()
            viewModel.deleteUserNotificationApi(this, notificationId.toString(), true)
            viewModel.deleteUserNotificationResponse().observe(this, this)
        }
        deleteNotiticationsDialog.tv_no.setOnClickListener {
            deleteNotiticationsDialog.dismiss()
        }
        deleteNotiticationsDialog.show()
    }

    override fun onClickReadUserNotifcation(postition: Int, notificationId: Int, notificationData: Body) {
    }

}