package com.bahamaeatsdriver.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bahamaeatsdriver.R
import com.bahamaeatsdriver.listeners.OnDriverAvailabilitySelection
import com.bahamaeatsdriver.model_class.slots.AvailableSlot
import kotlinx.android.synthetic.main.driver_sub_availability_row_layout.view.*
import java.text.SimpleDateFormat
import java.util.*


class DriverSubAvailabilityAdapter(var context: Context, val availableSlotsList: ArrayList<AvailableSlot>, val listener: OnDriverAvailabilitySelection,  val daysPostion: Int) : RecyclerView.Adapter<DriverSubAvailabilityAdapter.MyViewHolder>() {
    @SuppressLint("SimpleDateFormat")
    private val sdf = SimpleDateFormat("hh:mm")
    @SuppressLint("SimpleDateFormat")
    private val sdfs = SimpleDateFormat("hh:mm a")

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyViewHolder {
        val itemView = LayoutInflater.from(viewGroup.context).inflate(R.layout.driver_sub_availability_row_layout, viewGroup, false)
        return MyViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(myViewHolder: MyViewHolder, i: Int) {
        myViewHolder.itemView.tv_start_date.text = sdfs.format(sdf.parse(availableSlotsList[i].openTime)!!).toUpperCase(Locale.ROOT)+" - "+sdfs.format(sdf.parse(availableSlotsList[i].closeTime)!!).toUpperCase(Locale.ROOT)
        myViewHolder.itemView.tv_end_date.text = sdfs.format(sdf.parse(availableSlotsList[i].closeTime)!!).toUpperCase(Locale.ROOT)
        myViewHolder.itemView.ll_root1.setOnClickListener{
            listener.OnDriverAvailabilitySelection(availableSlotsList[i],i,daysPostion)
        }
        myViewHolder.itemView.ll_root2.setOnClickListener{
            listener.OnDriverAvailabilitySelection(availableSlotsList[i],i,daysPostion)
        }
        if (availableSlotsList[i].isSelected) {
//            myViewHolder.itemView.cb_select.setImageResource(R.drawable.black_checked)
            myViewHolder.itemView.tv_start_date.setBackgroundResource(R.drawable.green_bg_selected_slot)
            myViewHolder.itemView.tv_end_date.setBackgroundResource(R.drawable.green_bg_selected_slot)

        } else {
//            myViewHolder.itemView.cb_select.setImageResource(R.drawable.black_unchecked)
            myViewHolder.itemView.tv_start_date.setBackgroundResource(R.drawable.green_border)
            myViewHolder.itemView.tv_end_date.setBackgroundResource(R.drawable.green_border)
        }
    }

    override fun getItemCount(): Int {
        return availableSlotsList.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}