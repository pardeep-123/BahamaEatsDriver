package com.bahamaeatsdriver.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bahamaeatsdriver.R
import com.bahamaeatsdriver.model_class.change_ride_status.OrderDetail
import kotlinx.android.synthetic.main.order_details_after_complete_quantity_row_layout.view.*
import java.text.DecimalFormat

class OrderDetailsQuantiytAfterOrderCompleteAdapter(var context: Context, val orderDetails: List<OrderDetail>) : RecyclerView.Adapter<OrderDetailsQuantiytAfterOrderCompleteAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyViewHolder {
        val itemView = LayoutInflater.from(viewGroup.context).inflate(R.layout.order_details_after_complete_quantity_row_layout, viewGroup, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(myViewHolder: MyViewHolder, i: Int) {
        try {
            myViewHolder.itemView.tv_price.text = "$" + roundOffDecimalNew(orderDetails.get(i).price.toFloat()*orderDetails.get(i).quantity)
            myViewHolder.itemView.tv_price.text = "$" + orderDetails.get(i).price*orderDetails.get(i).quantity
            myViewHolder.itemView.tv_quantity.text = orderDetails.get(i).quantity.toString()
            myViewHolder.itemView.tv_itemName.text = orderDetails.get(i).itemName
        } catch (e: Exception) {
        }
    }

    fun roundOffDecimalNew(number: Float): Double? {
        val df = DecimalFormat("##.00")
        return df.format(number).toDouble()
    }
    override fun getItemCount(): Int {
        return orderDetails.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

}