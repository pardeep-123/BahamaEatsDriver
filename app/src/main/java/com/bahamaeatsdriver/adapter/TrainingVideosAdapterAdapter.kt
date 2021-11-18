package com.bahamaeatsdriver.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView
import com.bahamaeatsdriver.R
import com.bahamaeatsdriver.activity.DriverTrainingVideoActivity
import com.bahamaeatsdriver.helper.extensions.launchActivity
import com.bahamaeatsdriver.model_class.training_video_links.Body
import kotlinx.android.synthetic.main.training_videos_row_layout.view.*


class TrainingVideosAdapterAdapter(var context: Context, var videoList: List<Body>) :
    RecyclerView.Adapter<TrainingVideosAdapterAdapter.MyViewHolder>() {

    class MyViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.training_videos_row_layout, parent, false)
        return MyViewHolder(layoutView)
    }

    override fun getItemCount(): Int {
        return videoList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.itemView.tv_title.text = videoList[position].title
        holder.itemView.setOnClickListener {
            context.launchActivity<DriverTrainingVideoActivity> {
                putExtra("videoUrl", videoList[position].link)
            }
        }
    }
}