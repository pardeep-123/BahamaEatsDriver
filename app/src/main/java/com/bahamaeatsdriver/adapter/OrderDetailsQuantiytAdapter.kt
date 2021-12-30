package com.bahamaeatsdriver.adapter

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
        var addOnName = ""
        for (m in 0 until orderDetails[i].categories.size) {
        for (n in 0 until orderDetails[i].categories[m].addOnArray.size) {
            if (addOnName.isEmpty())
                addOnName += orderDetails[i].categories[m].addOnArray[n].addon
            else
                addOnName = addOnName + "," + orderDetails[i].categories[m].addOnArray[n].addon
        }
          /*  if (itemList.items!![position].localAddOnArray!![i].isSelected) {
                if (itemList.items!![position].localAddOnArray!![i].price.isNotEmpty())
                    if (itemList.items!![position].localAddOnArray!![i].price.isEmpty()) {
                        addOnPrice = addOnPrice
                    } else {
                        addOnPrice += itemList.items!![position].localAddOnArray!![i].price.toDouble()
                    }
                if (addOnName.isEmpty())
                    addOnName += itemList.items!![position].localAddOnArray!![i].addon
                else
                    addOnName =
                        addOnName + "," + itemList.items!![position].localAddOnArray!![i].addon

            }*/
        }
        if (addOnName.isEmpty()) {
            myViewHolder.itemView.tv_addOns.visibility = View.GONE
        } else {
            myViewHolder.itemView.tv_addOns.visibility = View.VISIBLE
            myViewHolder.itemView.tv_addOns.text = addOnName
        }
        try {
            if (orderDetails[i].price.isNotEmpty())
            myViewHolder.itemView.tv_price.text = "$" + roundOffDecimalNew(orderDetails[i].price.toFloat()* orderDetails[i].quantity)
            myViewHolder.itemView.tv_quantity.text = orderDetails[i].quantity.toString()+"x"
            myViewHolder.itemView.tv_itemName.text = orderDetails[i].itemName
            myViewHolder.itemView.tv_menuName.text = orderDetails[i].menu_name
            if (orderDetails[i].itemSpecialRequest.isNotEmpty()) myViewHolder.itemView.tv_specialRequest.visibility=View.VISIBLE else myViewHolder.itemView.tv_specialRequest.visibility=View.GONE
            myViewHolder.itemView.tv_specialRequest.text = "Special Request: "+orderDetails[i].itemSpecialRequest
        } catch (e: Exception) {
        }
    }

    override fun getItemCount(): Int {
        return orderDetails.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

}