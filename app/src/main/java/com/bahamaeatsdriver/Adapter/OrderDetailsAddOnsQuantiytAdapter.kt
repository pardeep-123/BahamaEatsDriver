package com.bahamaeatsdriver.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bahamaeatsdriver.R
import com.bahamaeatsdriver.helper.others.Helper.roundOffDecimalNew
import com.bahamaeatsdriver.model_class.add_on_list.AddOnsCustomModel
import kotlinx.android.synthetic.main.order_details_quantity_row_layout.view.*

class OrderDetailsAddOnsQuantiytAdapter(var context: Context, val listAddOnList: ArrayList<AddOnsCustomModel>) : RecyclerView.Adapter<OrderDetailsAddOnsQuantiytAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyViewHolder {
        val itemView = LayoutInflater.from(viewGroup.context).inflate(R.layout.order_details_quantity_row_layout, viewGroup, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(myViewHolder: MyViewHolder, i: Int) {
        if(listAddOnList.get(i).price.isNotEmpty())
        myViewHolder.itemView.tv_price.text = "$"+roundOffDecimalNew(listAddOnList.get(i).price.toFloat()*listAddOnList.get(i).quantity)
            myViewHolder.itemView.tv_quantity.text = listAddOnList.get(i).quantity.toString()+"x"
            myViewHolder.itemView.tv_itemName.text = listAddOnList.get(i).addon

    }

    override fun getItemCount(): Int {
        return listAddOnList.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}