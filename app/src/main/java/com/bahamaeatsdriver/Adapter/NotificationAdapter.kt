package com.bahamaeatsdriver.Adapter

import android.view.*
import androidx.recyclerview.widget.RecyclerView
import com.bahamaeats.constant.Constants
import com.bahamaeatsdriver.R
import com.bahamaeatsdriver.activity.notification_listing.NotificationActivity
import com.bahamaeatsdriver.helper.others.CommonMethods
import com.bahamaeatsdriver.listeners.DeleteUserNotificationOnClickEvent
import com.bahamaeatsdriver.model_class.notification_listing.Body
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.row_notification.view.*
import java.util.*


class NotificationAdapter(
    val mContext: NotificationActivity,
    val notificationsList: ArrayList<Body>,
    val onClickEvent: DeleteUserNotificationOnClickEvent
) : RecyclerView.Adapter<NotificationAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.row_notification, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return notificationsList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

            holder.itemView.tv_title.visibility = View.VISIBLE
            holder.itemView.iv_notificationImage.visibility = View.VISIBLE
            Glide.with(mContext).load(notificationsList.get(position).restaurantImage).placeholder(R.drawable.placeholder_circle).into(holder.itemView.iv_notificationImage)
            holder.itemView.tv_title.text = notificationsList.get(position).title
            holder.itemView.tv_descriptions.text = notificationsList.get(position).message
            holder.itemView.tv_dateTime.text = CommonMethods.convertToNewFormat3(notificationsList.get(position).createdAt)
        //Long Press
        holder.itemView.setOnLongClickListener(object : View.OnLongClickListener {
            override fun onLongClick(v: View): Boolean {
                onClickEvent.onClickDeleteUserNotifcation(position, notificationsList.get(position).id)
                return false
            }
        })
       /* holder.itemView.setOnClickListener {
            if (notificationsList.get(position).code != 10) {
                onClickEvent.onClickReadUserNotifcation(
                    position,
                    notificationsList.get(position).id,
                    notificationsList.get(position)
                )
            } else {
                onClickEvent.onClickReadUserNotifcation(
                    position,
                    notificationsList.get(position).id,
                    notificationsList.get(position)
                )
            }
        }*/

       /* if (notificationsList.get(position).isRead == 0) {
            holder.itemView.tv_notification_count.visibility = View.VISIBLE
        } else {
            holder.itemView.tv_notification_count.visibility = View.GONE
        }*/
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    fun getNotificationTime2(time_stamp: Long): String? {
        var date: Date? = null
        try {
            date = Date(time_stamp * 1000)
        } catch (ex: java.lang.Exception) {
            ex.printStackTrace()
        }
        System.out.println("dateeee" + date.toString())
        var string_date: String?
        val current = Calendar.getInstance().time
        var diffInSeconds = (current.time - date!!.time) / 1000
        val sec = if (diffInSeconds >= 60) diffInSeconds % 60 else diffInSeconds
        val min = if ((diffInSeconds / 60).also {
                diffInSeconds = it
            } >= 60) diffInSeconds % 60 else diffInSeconds
        val hrs = if ((diffInSeconds / 60).also {
                diffInSeconds = it
            } >= 24) diffInSeconds % 24 else diffInSeconds
        val days = if ((diffInSeconds / 24).also {
                diffInSeconds = it
            } >= 30) diffInSeconds % 30 else diffInSeconds
        val weeks = days / 7
        val months = if ((diffInSeconds / 30).also {
                diffInSeconds = it
            } >= 12) diffInSeconds % 12 else diffInSeconds
        val years = (diffInSeconds / 12).also { diffInSeconds = it }
        var getYeardata = ""
        if (years != 0L) {
            getYeardata = if (years == 1L) {
                "1 year"
            } else {
                "$years years"
            }
        }
        var getMonthdata = ""
        if (months != 0L) {
            getMonthdata = if (months == 1L) {
                //sb.append("a month");
                "1 m"
            } else {
                //sb.append(months + " months");
                "$months m"
            }
        }
        var getWeekData = ""
        if (weeks != 0L) {
            getWeekData = if (weeks == 1L) {
                //sb.append("a month");
                "1 w"
            } else {
                //sb.append(months + " months");
                "$weeks w"
            }
        }
        var getDaydata = ""
        if (days != 0L) {
            getDaydata = if (days == 1L) {
                //sb.append("a day");
                "1 d"
            } else {
                // sb.append(days + " days");
                "$days d"
            }
        }
        var getHourData = ""
        if (hrs != 0L) {
            getHourData = if (hrs == 1L) {
                //sb.append("an hour");
                "1 h"
            } else {
                //sb.append(hrs + " hours");
                "$hrs h"
            }
        }
        var getMintData = ""
        if (min != 0L) {
            getMintData = if (min == 1L) {
                //sb.append("a minute");
                "1 m"
            } else {
                //sb.append(min + " minutes");
                "$min m"
            }
        }
        //final result
        var finalResult = ""
        if (!getYeardata.isEmpty()) {
            finalResult = getYeardata
        } else {
            if (!getMonthdata.isEmpty()) {
                finalResult = "$getMonthdata"
            } else {
                if (!getWeekData.isEmpty()) {
                    finalResult = "$getWeekData"
                } else {
                    if (!getDaydata.isEmpty()) {
                        finalResult = "$getDaydata"
                    } else {
                        if (!getHourData.isEmpty()) {
                            finalResult = "$getHourData"
                        } else {
                            if (!getMintData.isEmpty()) {
                                finalResult = "$getMintData"
                            }
                        }
                    }
                }
            }
        }

        if (getYeardata.isEmpty() && getMonthdata.isEmpty() && getWeekData.isEmpty() && getDaydata.isEmpty() && getHourData.isEmpty()
            && getMintData.isEmpty()
        ) {
            finalResult = "just now"
        }
        string_date = finalResult
        return string_date
    }


}