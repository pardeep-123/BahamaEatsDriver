package com.bahamaeatsdriver.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bahamaeatsdriver.R
import com.bahamaeatsdriver.model_class.job_history_details.AddOnArray
import kotlinx.android.synthetic.main.history_page_addons_layout.view.*

class OrderDetailsAddOnAdapter(val mContext: Context, val addonsList: ArrayList<AddOnArray>) :
    RecyclerView.Adapter<OrderDetailsAddOnAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.history_page_addons_layout, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return addonsList.size
    }


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.itemView.tv_addOnName.text=addonsList.get(position).addon
        holder.itemView.tv_addOnquantity.text=addonsList.get(position).quantity.toString()+"x"
        holder.itemView.tv_addOnPrice.text="$"+addonsList.get(position).price
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


    }
}