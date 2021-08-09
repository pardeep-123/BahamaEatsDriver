package com.bahamaeatsdriver.Adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bahamaeats.constant.Constants
import com.bahamaeatsdriver.R
import com.bahamaeatsdriver.activity.Payment_Status
import com.bahamaeatsdriver.activity.job_history_details.JobHistoryDetails
import com.bahamaeatsdriver.helper.extensions.launchActivity
import com.bahamaeatsdriver.helper.others.CommonMethods
import com.bahamaeatsdriver.model_class.driver_tips_earning.RideRequest
import kotlinx.android.synthetic.main.res_payment.view.*
import java.text.DecimalFormat

class FilterPaymentAdapter(var context: Payment_Status, val paymentsList: ArrayList<RideRequest>) :
    RecyclerView.Adapter<FilterPaymentAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyViewHolder {
        val inflater =
            LayoutInflater.from(viewGroup.context).inflate(R.layout.res_payment, viewGroup, false)
        return MyViewHolder(inflater)
    }

    override fun onBindViewHolder(myViewHolder: MyViewHolder, i: Int) {
        if (paymentsList.get(i).user != null)
            myViewHolder.itemView.tv_userName.text = paymentsList.get(i).user.firstName + " " + paymentsList.get(i).user.lastName

        if (paymentsList.get(i).order != null) {
            myViewHolder.itemView.tv_orderNumber.text = "Order Number-" + paymentsList.get(i).order.id
            if (paymentsList.get(i).order.tip!=0.0) myViewHolder.itemView.tv_tip.visibility else myViewHolder.itemView.tv_tip.visibility=View.GONE
            if (paymentsList.get(i).order.deliveryFee!=0.0) myViewHolder.itemView.tv_deliveryFee.visibility else myViewHolder.itemView.tv_deliveryFee.visibility=View.GONE
            myViewHolder.itemView.tv_tip.text = "Tip : $" + paymentsList.get(i).order.tip
            myViewHolder.itemView.tv_deliveryFee.text = "Delivery Fee : $" + paymentsList.get(i).order.deliveryFee
        }

        myViewHolder.itemView.tv_date.text = CommonMethods.parseDateToddMMyyyy(paymentsList.get(i).createdAt, Constants.ORDER_DATE_FORMAT)
        /**
         * orderType => 1.New,2.Accepted,3.Completed,4.Cancelled,5.In-progress,6.Ready For Pickup
        status: 1.New,2.Accepted,3.Completed,4.Cancelled,5.In-progress,6.Ready For Pickup
        //1=>New 2=>Started 3=>Completed 4=>Cancelled
        let arrayRideSatus = ["","New","Started","Completed","Cancelled"]
         */

        if (paymentsList.get(i).rideStatus == 1) {
            myViewHolder.tv_status.setTextColor(Color.parseColor("#D7EF2323"))
            myViewHolder.itemView.tv_status.text = "New"
        } else if (paymentsList.get(i).rideStatus == 2) {
            myViewHolder.tv_status.setTextColor(Color.parseColor("#5fb709"))
            myViewHolder.itemView.tv_status.text = "Started"
        } else if (paymentsList.get(i).rideStatus == 3) {
            myViewHolder.itemView.tv_status.text = "Completed"
            myViewHolder.tv_status.setTextColor(Color.parseColor("#78B071"))
        } else if (paymentsList.get(i).rideStatus == 4) {
            myViewHolder.itemView.tv_status.text = "Cancelled"
            myViewHolder.tv_status.setTextColor(Color.parseColor("#808080"))
        }

        myViewHolder.itemView.setOnClickListener {
                context.launchActivity<JobHistoryDetails>
                {
                    putExtra("jobId", paymentsList.get(i).id.toString())
                }

        }
    }

    override fun getItemCount(): Int {
        return paymentsList.size
    }

    fun roundOffDecimal(number: Double): kotlin.String {
//        val df = DecimalFormat("#.##")
        val df = DecimalFormat(".00")
//        df.roundingMode = RoundingMode.FLOOR
        return df.format(number).toString()
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tv_status: TextView

        init {
            tv_status = itemView.findViewById(R.id.tv_status)
        }
    }

}