package com.bahamaeatsdriver.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bahamaeatsdriver.R
import com.bahamaeatsdriver.listeners.OnCitySelection
import com.bahamaeatsdriver.model_class.get_city.Body
import kotlinx.android.synthetic.main.city_row_layout.view.*

class CityAdapter(var context: Context, val cityList: List<Body>,val listener: OnCitySelection) : RecyclerView.Adapter<CityAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyViewHolder {
        val itemView = LayoutInflater.from(viewGroup.context).inflate(R.layout.city_row_layout, viewGroup, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(myViewHolder: MyViewHolder, i: Int) {
        myViewHolder.itemView.tv_cityName.text = cityList[i].name

        myViewHolder.itemView.tv_cityName.setOnClickListener{
            listener.onCitySelect(cityList[i].name,i)
        }
    }

    override fun getItemCount(): Int {
        return cityList.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}