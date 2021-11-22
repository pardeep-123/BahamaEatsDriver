package com.bahamaeatsdriver.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bahamaeats.constant.Constants
import com.bahamaeatsdriver.R
import com.bahamaeatsdriver.activity.PaymentStatsActivity
import com.bahamaeatsdriver.activity.job_history_details.JobHistoryDetails
import com.bahamaeatsdriver.helper.extensions.launchActivity
import com.bahamaeatsdriver.helper.others.CommonMethods
import com.bahamaeatsdriver.model_class.driver_tips_earning.RideRequest
import kotlinx.android.synthetic.main.res_payment.view.*

class FilterPaymentAdapter(
    var context: PaymentStatsActivity,
    val paymentsList: ArrayList<RideRequest>
) :
    RecyclerView.Adapter<FilterPaymentAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyViewHolder {
        val inflater = LayoutInflater.from(viewGroup.context).inflate(R.layout.res_payment, viewGroup, false)
        return MyViewHolder(inflater)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(myViewHolder: MyViewHolder, i: Int) {
        if (paymentsList[i].user != null)
            myViewHolder.itemView.tv_userName.text =
                paymentsList[i].user.firstName + " " + paymentsList[i].user.lastName
        if (paymentsList[i].order != null) {
            myViewHolder.itemView.tv_orderNumber.text = "Order Number-" + paymentsList[i].order.id
            if (paymentsList[i].order.tip != 0.0) myViewHolder.itemView.tv_tip.visibility else myViewHolder.itemView.tv_tip.visibility =
                View.GONE
            if (paymentsList[i].order.deliveryFee != 0.0) myViewHolder.itemView.tv_deliveryFee.visibility else myViewHolder.itemView.tv_deliveryFee.visibility =
                View.GONE
            myViewHolder.itemView.tv_tip.text = "Tip : $" + paymentsList[i].order.tip
            myViewHolder.itemView.tv_deliveryFee.text =
                "Delivery Fee : $" + paymentsList[i].order.deliveryFee
        }

        myViewHolder.itemView.tv_date.text = CommonMethods.parseDateToddMMyyyy(
            paymentsList[i].createdAt,
            Constants.ORDER_DATE_FORMAT
        )
        /**
         * orderType => 1.New,2.Accepted,3.Completed,4.Cancelled,5.In-progress,6.Ready For Pickup
        status: 1.New,2.Accepted,3.Completed,4.Cancelled,5.In-progress,6.Ready For Pickup
        //1=>New 2=>Started 3=>Completed 4=>Cancelled
        let arrayRideSatus = ["","New","Started","Completed","Cancelled"]
         */
        when (paymentsList[i].rideStatus) {
            1 -> {
                myViewHolder.itemView.tv_status.setTextColor(Color.parseColor("#D7EF2323"))
                myViewHolder.itemView.tv_status.text = "New"
            }
            2 -> {
                myViewHolder.itemView.tv_status.setTextColor(Color.parseColor("#5fb709"))
                myViewHolder.itemView.tv_status.text = "Started"
            }
            3 -> {
                myViewHolder.itemView.tv_status.text = "Completed"
                myViewHolder.itemView.tv_status.setTextColor(Color.parseColor("#78B071"))
            }
            4 -> {
                myViewHolder.itemView.tv_status.text = "Cancelled"
                myViewHolder.itemView.tv_status.setTextColor(Color.parseColor("#808080"))
            }
        }

        myViewHolder.itemView.setOnClickListener {
            context.launchActivity<JobHistoryDetails>
            {
                putExtra("jobId", paymentsList[i].id.toString())
            }

        }
    }

    override fun getItemCount(): Int {
        return paymentsList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}