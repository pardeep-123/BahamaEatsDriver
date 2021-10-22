package com.bahamaeatsdriver.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bahamaeatsdriver.R
import java.util.*

class ScheduleAvailableAdapter(var context: Context, var start: ArrayList<String>, var end: ArrayList<String>) : RecyclerView.Adapter<ScheduleAvailableAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyViewHolder {
        val inflater = LayoutInflater.from(viewGroup.context).inflate(R.layout.res_schedule, viewGroup, false)
        return MyViewHolder(inflater)
    }

    override fun onBindViewHolder(myViewHolder: MyViewHolder, i: Int) {
        myViewHolder.tv_starttime.text = start[i]
        myViewHolder.tv_endtime.text = end[i]
        if (i == 1) {
            myViewHolder.Button_select.setBackgroundResource(R.drawable.back_togal_button)
        }
    }

    override fun getItemCount(): Int {
        return start.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tv_starttime: TextView
        var tv_endtime: TextView
        var Button_select: Button

        init {
            tv_starttime = itemView.findViewById(R.id.tv_starttime)
            tv_endtime = itemView.findViewById(R.id.tv_endtime)
            Button_select = itemView.findViewById(R.id.Button_select)
        }
    }

}