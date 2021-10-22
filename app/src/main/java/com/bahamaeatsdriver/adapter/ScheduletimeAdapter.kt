package com.bahamaeatsdriver.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bahamaeatsdriver.R
import java.util.*

class ScheduletimeAdapter(var context: Context, var date: ArrayList<String>, var week: ArrayList<String>) : RecyclerView.Adapter<ScheduletimeAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyViewHolder {
     val inflater = LayoutInflater.from(viewGroup.context).inflate(R.layout.res_schedule_time, viewGroup, false)
        return MyViewHolder(inflater)
    }

    override fun onBindViewHolder(myViewHolder: MyViewHolder, i: Int) {
        myViewHolder.tv_day.text = date[i]
        myViewHolder.tv_week.text = week[i]
        myViewHolder.itemView.setOnClickListener { myViewHolder.relativ_background.setBackgroundResource(R.drawable.cuv_green) }
    }

    override fun getItemCount(): Int {
        return date.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tv_week: TextView
        var tv_day: TextView
        var relativ_background: RelativeLayout

        init {
            tv_day = itemView.findViewById(R.id.tv_day)
            tv_week = itemView.findViewById(R.id.tv_week)
            relativ_background = itemView.findViewById(R.id.relativ_background)
        }
    }

}