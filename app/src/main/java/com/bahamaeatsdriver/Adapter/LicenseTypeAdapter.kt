package com.bahamaeatsdriver.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bahamaeatsdriver.R
import com.bahamaeatsdriver.listeners.OnLicenseTypeSelection
import com.bahamaeatsdriver.model_class.license_type.LicenseTypeModel
import kotlinx.android.synthetic.main.license_type_row_layout.view.*

class LicenseTypeAdapter(var context: Context, val licenseType: ArrayList<LicenseTypeModel>, val listener: OnLicenseTypeSelection) : RecyclerView.Adapter<LicenseTypeAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyViewHolder {
        val itemView = LayoutInflater.from(viewGroup.context).inflate(R.layout.license_type_row_layout, viewGroup, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(myViewHolder: MyViewHolder, i: Int) {
        myViewHolder.itemView.tv_type.text = licenseType.get(i).licenseType

        myViewHolder.itemView.setOnClickListener {
            listener.OnTypeSelection(licenseType.get(i).licenseType, i)
        }
    }

    override fun getItemCount(): Int {
        return licenseType.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}