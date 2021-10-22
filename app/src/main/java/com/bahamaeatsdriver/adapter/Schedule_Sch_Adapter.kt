package com.bahamaeatsdriver.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bahamaeatsdriver.R
import java.util.*

class Schedule_Sch_Adapter(var context: Context, var start: ArrayList<String>, var end: ArrayList<String>) : RecyclerView.Adapter<Schedule_Sch_Adapter.MyViewHolder>() {
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyViewHolder {
        val  inflater = LayoutInflater.from(viewGroup.context).inflate(R.layout.res_scheduled, viewGroup, false)

        return MyViewHolder(inflater)
    }

    override fun onBindViewHolder(myViewHolder: MyViewHolder, i: Int) {
        myViewHolder.tv_starttime.text = start[i]
        myViewHolder.tv_endtime.text = end[i]
        if (i == 1) {
            myViewHolder.Button_select.setBackgroundResource(R.drawable.back_togal_button)
            myViewHolder.Button_aviliable.setBackgroundResource(R.drawable.btn_green_avaliable)
            myViewHolder.Button_aviliable.setTextColor(Color.parseColor("#ffffff"))
            myViewHolder.Button_aviliable.text = "AVALILED"
            myViewHolder.card_back.setCardBackgroundColor(Color.parseColor("#FAFAFA"))
            myViewHolder.Relativ_sidecard.visibility = View.VISIBLE
        }
    }

    override fun getItemCount(): Int {
        return start.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tv_starttime: TextView
        var tv_endtime: TextView
        var Button_select: Button
        var Button_aviliable: Button
        var Relativ_sidecard: RelativeLayout
        var card_back: CardView

        init {
            tv_starttime = itemView.findViewById(R.id.tv_starttime)
            tv_endtime = itemView.findViewById(R.id.tv_endtime)
            Button_select = itemView.findViewById(R.id.Button_select)
            Button_aviliable = itemView.findViewById(R.id.Button_aviliable)
            Relativ_sidecard = itemView.findViewById(R.id.Relativ_sidecard)
            card_back = itemView.findViewById(R.id.card_back)
        }
    }

}