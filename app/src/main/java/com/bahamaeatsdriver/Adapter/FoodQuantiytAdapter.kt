package com.bahamaeatsdriver.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bahamaeatsdriver.R
import com.bahamaeatsdriver.model_class.job_history_details.AddOnArray
import com.bahamaeatsdriver.model_class.job_history_details.OrderDetail
import kotlinx.android.synthetic.main.food_quantity_layout.view.*
import java.text.FieldPosition

class FoodQuantiytAdapter(var context: Context, val orderDetails: List<OrderDetail>) : RecyclerView.Adapter<FoodQuantiytAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyViewHolder {
        val itemView = LayoutInflater.from(viewGroup.context).inflate(R.layout.food_quantity_layout, viewGroup, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(myViewHolder: MyViewHolder, position: Int) {
        var addOnName=""
        var addOnPrice=""

        myViewHolder.itemView.tv_price.text = "$"+orderDetails.get(position).price
        myViewHolder.itemView.tv_quantity.text = orderDetails.get(position).quantity.toString()+"x"
        myViewHolder.itemView.tv_itemName.text = orderDetails.get(position).itemName
        myViewHolder.itemView.tv_desciption.text = orderDetails.get(position).itemDescription
        val listAddOnList=ArrayList<AddOnArray>()
        var count = 0
        for (i in 0 until orderDetails.get(position).categories.size){
            for (j in 0 until orderDetails.get(position).categories.get(i).addOnArray.size) {
                  val addOnMOdel = AddOnArray(orderDetails.get(position).categories.get(i).addOnArray.get(j).addon,
                      orderDetails.get(position).categories.get(position).addOnArray.get(j).price,
                      orderDetails.get(position).quantity)
//                      orderDetails.get(position).categories.get(position).addOnArray.get(j).quantity)
                     listAddOnList.add(count, addOnMOdel)
                      count++
                if (addOnName.isEmpty()) {
                    addOnName = orderDetails.get(position).categories.get(i).addOnArray.get(j).addon
                    addOnPrice =orderDetails.get(position).categories.get(i).addOnArray.get(j).price
                } else {
                    addOnPrice =addOnPrice+ ","+orderDetails.get(position).categories.get(i).addOnArray.get(j).price
                    addOnName = addOnName + "," + orderDetails.get(position).categories.get(i).addOnArray.get(j).addon
                }
            }
            }
        if (listAddOnList.isEmpty())
        {
            myViewHolder.itemView.rv_addon.visibility=View.GONE
            myViewHolder.itemView.tv_addOnLabel.visibility=View.GONE
        }else{
            myViewHolder.itemView.rv_addon.adapter= OrderDetailsAddOnAdapter(context,listAddOnList)
            myViewHolder.itemView.tv_addOnLabel.visibility=View.VISIBLE
            myViewHolder.itemView.rv_addon.visibility=View.VISIBLE
        }
    }

    override fun getItemCount(): Int {
        return orderDetails.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

}