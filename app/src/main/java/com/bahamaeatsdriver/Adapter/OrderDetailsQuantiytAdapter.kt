package com.bahamaeatsdriver.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bahamaeatsdriver.R
import com.bahamaeatsdriver.helper.others.Helper.roundOffDecimalNew
import com.bahamaeatsdriver.model_class.get_current_ride.OrderDetail
import kotlinx.android.synthetic.main.order_details_quantity_row_layout.view.*

class OrderDetailsQuantiytAdapter(var context: Context, val orderDetails: List<OrderDetail>) : RecyclerView.Adapter<OrderDetailsQuantiytAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyViewHolder {
        val itemView = LayoutInflater.from(viewGroup.context).inflate(R.layout.order_details_quantity_row_layout, viewGroup, false)
        return MyViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(myViewHolder: MyViewHolder, i: Int) {
        try {
            if (orderDetails.get(i).price.isNotEmpty())
            myViewHolder.itemView.tv_price.text = "$" + roundOffDecimalNew(orderDetails.get(i).price.toFloat()*orderDetails.get(i).quantity)
            myViewHolder.itemView.tv_quantity.text = orderDetails.get(i).quantity.toString()+"x"
            myViewHolder.itemView.tv_itemName.text = orderDetails.get(i).itemName
        } catch (e: Exception) {
        }
    }

    override fun getItemCount(): Int {
        return orderDetails.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

}