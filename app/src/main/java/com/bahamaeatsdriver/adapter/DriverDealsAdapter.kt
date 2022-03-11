package com.bahamaeatsdriver.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bahamaeatsdriver.R
import com.bahamaeatsdriver.activity.Home_Page.Companion.mLatitute
import com.bahamaeatsdriver.activity.Home_Page.Companion.mLongitute
import com.bahamaeatsdriver.activity.driver_deals.DriverDealsActivity
import com.bahamaeatsdriver.helper.others.Helper
import com.bahamaeatsdriver.helper.others.Helper.distance
import com.bahamaeatsdriver.listeners.OnDealSelection
import com.bahamaeatsdriver.model_class.driver_deals.All
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.driver_deals_row_layout.view.*
import java.text.DecimalFormat

class DriverDealsAdapter(
    var mContext: DriverDealsActivity, var  dealsList: ArrayList<All>,
    val listener: OnDealSelection
) : RecyclerView.Adapter<DriverDealsAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyViewHolder {
        val itemView = LayoutInflater.from(viewGroup.context).inflate(R.layout.driver_deals_row_layout, viewGroup, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(myViewHolder: MyViewHolder, i: Int) {
        val countToTargetDeal = Math.abs(dealsList[i].complete_rides - dealsList[i].number_of_order)
        if (countToTargetDeal == 0) {
            myViewHolder.itemView.rl_closeRootLayout.visibility = View.GONE
            myViewHolder.itemView.tv_openCloseStatus.text = ""
        } else {
            myViewHolder.itemView.tv_openCloseStatus.text =
                "Complete $countToTargetDeal rides to unlock the deal"
            myViewHolder.itemView.rl_closeRootLayout.visibility = View.VISIBLE
        }
        /***
         * favouritecount= 1 means added as favourite
         * favouritecount= 0 means not added as favourite
         */
        if (dealsList[i].favouritecount == 1)
            myViewHolder.itemView.iv_favIcon.setImageDrawable(
                ContextCompat.getDrawable(
                    mContext,
                    R.drawable.ic_like
                )
            )
        else
            myViewHolder.itemView.iv_favIcon.setImageDrawable(
                ContextCompat.getDrawable(
                    mContext,
                    R.drawable.ic_unlike
                )
            )
        myViewHolder.itemView.tv_discount.text = dealsList[i].discount_title
        myViewHolder.itemView.tv_description.text = dealsList[i].discount_description
        myViewHolder.itemView.tv_restaurantName.text = dealsList[i].name
        myViewHolder.itemView.tv_categories.text = dealsList[i].description
        if (!mLatitute.isNullOrEmpty() && !mLongitute.isNullOrEmpty() && !dealsList[i].latitude.isNullOrEmpty() && !dealsList[i].longitude.isNullOrEmpty()) {
            val value1: String = java.lang.String.valueOf(DecimalFormat("##").format(distance(mLatitute.toDouble(),mLongitute.toDouble(), dealsList[i].latitude.toDouble(), dealsList[i].longitude.toDouble())))
            myViewHolder.itemView.tv_distance.text = "$value1 miles away"
        }
        Glide.with(mContext).load(dealsList[i].image).placeholder(R.drawable.placeholder_rectangle)
            .into(myViewHolder.itemView.iv_imageThumbail)

        myViewHolder.itemView.iv_location.setOnClickListener {
            if (Helper.isGoogleMapsInstalled(mContext)) {
                if (!mLatitute.isNullOrEmpty() && !mLongitute.isNullOrEmpty() && !dealsList[i].latitude.isNullOrEmpty() && !dealsList[i].longitude.isNullOrEmpty()) {
                    val merchantLatitude = dealsList[i].latitude
                    val merchantLongitude = dealsList[i].longitude
                    val uri = "http://maps.google.com/maps?saddr=$mLatitute,$mLongitute&daddr=$merchantLatitude,$merchantLongitude"
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
                    mContext.startActivity(intent)
                }else
                    Helper.showErrorAlert(mContext, "Merchant location is not available")
            } else
                Helper.showErrorAlert(mContext, "Unable to find google map. Please intsall the google map")
        }
        myViewHolder.itemView.iv_favIcon.setOnClickListener {
            listener.OnDealSelection(dealsList[i],i,dealsList[i].id.toString())
        }

    }

    fun updateList( dealsList: ArrayList<All>){
      this.dealsList=dealsList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return dealsList.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}