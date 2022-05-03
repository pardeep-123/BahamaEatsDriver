package com.bahamaeatsdriver.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bahamaeatsdriver.R
import com.bahamaeatsdriver.listeners.OnDriverAvailabilitySelection
import com.bahamaeatsdriver.model_class.slots.Body
import kotlinx.android.synthetic.main.driver_availability_row_layout.view.*

class DriverAvailabilityAdapter(
    var context: Context, val daysList: ArrayList<Body>,
    val listener: OnDriverAvailabilitySelection
) : RecyclerView.Adapter<DriverAvailabilityAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyViewHolder {
        val itemView = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.driver_availability_row_layout, viewGroup, false)
        return MyViewHolder(itemView)
    }


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(myViewHolder: MyViewHolder, pos: Int) {
        myViewHolder.itemView.tv_delivery_name.text = "Every " + daysList[pos].dayName
        val adp =
            DriverSubAvailabilityAdapter(context, daysList[pos].availableSlotList, listener, pos)
        val grid = GridLayoutManager(context, 2)
        myViewHolder.itemView.rv_availableSlots.layoutManager = grid
        myViewHolder.itemView.rv_availableSlots.adapter = adp
    }

    override fun getItemCount(): Int {
        return daysList.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}