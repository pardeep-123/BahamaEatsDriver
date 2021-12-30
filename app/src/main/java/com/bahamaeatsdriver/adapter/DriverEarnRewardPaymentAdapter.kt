package com.bahamaeatsdriver.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bahamaeats.constant.Constants
import com.bahamaeatsdriver.R
import com.bahamaeatsdriver.activity.PaymentStatsActivity
import com.bahamaeatsdriver.helper.others.CommonMethods
import com.bahamaeatsdriver.model_class.driver_tips_earning.RefferalData
import kotlinx.android.synthetic.main.driver_reward_payment_row_layout.view.*

class DriverEarnRewardPaymentAdapter(
    var context: PaymentStatsActivity,
    val referralList: ArrayList<RefferalData>
) :
    RecyclerView.Adapter<DriverEarnRewardPaymentAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyViewHolder {
        val inflater = LayoutInflater.from(viewGroup.context).inflate(R.layout.driver_reward_payment_row_layout, viewGroup, false)
        return MyViewHolder(inflater)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(myViewHolder: MyViewHolder, i: Int) {
        myViewHolder.itemView.tv_userName.text=referralList[i].driverdetails.fullName
        myViewHolder.itemView.tv_date.text= CommonMethods.parseDateToddMMyyyy(referralList[i].createdAt, Constants.ORDER_DATE_FORMAT)
        myViewHolder.itemView.tv_completedRides.text="Completed Rides- "+referralList[i].completedRide
        myViewHolder.itemView.tv_earnReward.text="Received Reward- $"+referralList[i].referralsAmount
    }

    override fun getItemCount(): Int {
        return referralList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}