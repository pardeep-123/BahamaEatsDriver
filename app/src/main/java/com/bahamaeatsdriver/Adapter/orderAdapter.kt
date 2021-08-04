package com.bahamaeatsdriver.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bahamaeatsdriver.R
import java.util.*

class orderAdapter(var context: Context, var name: ArrayList<String>) : RecyclerView.Adapter<orderAdapter.MyViewHolder>() {
    private var inflater: LayoutInflater? = null
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyViewHolder {
       val inflater = LayoutInflater.from(viewGroup.context).inflate(R.layout.res_orderitem, viewGroup, false)
        return MyViewHolder(inflater)
    }

    override fun onBindViewHolder(myViewHolder: MyViewHolder, i: Int) {
        myViewHolder.tv_order_name.text = name[i]
        if (i == 0) {
            myViewHolder.imageView_order.setImageResource(R.drawable.ring_greenwith_white)
            myViewHolder.tv_ingrednt.visibility = View.VISIBLE

        }
    }

    override fun getItemCount(): Int {
        return name.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tv_order_name: TextView
        var tv_ingrednt: TextView
        var imageView_order: ImageView

        init {
            tv_order_name = itemView.findViewById(R.id.tv_order_name)
            tv_ingrednt = itemView.findViewById(R.id.tv_ingrednt)
            imageView_order = itemView.findViewById(R.id.imageView_order)
        }
    }

}