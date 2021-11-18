package com.bahamaeatsdriver.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bahamaeats.network.RestObservable
import com.bahamaeats.network.Status
import com.bahamaeatsdriver.R
import com.bahamaeatsdriver.adapter.TrainingVideosAdapterAdapter
import com.bahamaeatsdriver.model_class.training_video_links.TrainingVideoLinksResponse
import com.bahamaeatsdriver.repository.BaseViewModel
import kotlinx.android.synthetic.main.activity_driver_training_videos_list.*

class DriverTrainingVideosListActivity : AppCompatActivity(), Observer<RestObservable> {
    private val viewModel: BaseViewModel by lazy { ViewModelProvider(this).get(BaseViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_driver_training_videos_list)
        iv_backArrow.setOnClickListener { finish() }
        viewModel.getTrainingVideoLinksApi(this, true)
        viewModel.getTrainingVideoLinksResponse().observe(this, this)
    }

    override fun onChanged(liveData: RestObservable?) {
        when (liveData!!.status) {
            Status.SUCCESS -> {
                if (liveData.data is TrainingVideoLinksResponse) {
                    rv_trainingVideos.adapter = TrainingVideosAdapterAdapter(this,liveData.data.body)
                }
            }
        }
    }
}