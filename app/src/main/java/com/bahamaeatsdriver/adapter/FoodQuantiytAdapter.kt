package com.bahamaeatsdriver.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bahamaeatsdriver.R
import com.bahamaeatsdriver.helper.others.Helper.roundOffDecimalNew
import com.bahamaeatsdriver.model_class.job_history_details.AddOnArray
import com.bahamaeatsdriver.model_class.job_history_details.OrderDetail
import kotlinx.android.synthetic.main.food_quantity_layout.view.*

class FoodQuantiytAdapter(var context: Context, val orderDetails: List<OrderDetail>) : RecyclerView.Adapter<FoodQuantiytAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyViewHolder {
        val itemView = LayoutInflater.from(viewGroup.context).inflate(R.layout.food_quantity_layout, viewGroup, false)
        return MyViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(myViewHolder: MyViewHolder, position: Int) {
        var addOnName=""
        var addOnPrice=""
        myViewHolder.itemView.tv_price.text = "$"+roundOffDecimalNew(orderDetails[position].price.toFloat()*orderDetails[position].quantity)
        myViewHolder.itemView.tv_quantity.text = orderDetails[position].quantity.toString()+"x"
        myViewHolder.itemView.tv_itemName.text = orderDetails[position].itemName
        myViewHolder.itemView.tv_desciption.text = orderDetails[position].itemDescription
        val listAddOnList=ArrayList<AddOnArray>()
        var count = 0
        for (i in orderDetails[position].categories.indices){
            for (j in orderDetails[position].categories[i].addOnArray.indices) {
                  val addOnMOdel = AddOnArray(
                      orderDetails[position].categories[i].addOnArray[j].addon,
                      orderDetails[position].categories[i].addOnArray[j].price,
                      orderDetails[position].quantity)
                     listAddOnList.add(count, addOnMOdel)
                      count++
                if (addOnName.isEmpty()) {
                    addOnName = orderDetails[position].categories[i].addOnArray[j].addon
                    addOnPrice = orderDetails[position].categories[i].addOnArray[j].price
                } else {
                    addOnPrice =addOnPrice+ ","+ orderDetails[position].categories[i].addOnArray[j].price
                    addOnName = addOnName + "," + orderDetails[position].categories[i].addOnArray[j].addon
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

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}