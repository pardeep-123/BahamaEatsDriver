package com.bahamaeatsdriver.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bahamaeats.network.RestObservable
import com.bahamaeats.network.Status
import com.bahamaeatsdriver.adapter.ItemsAdapter
import com.bahamaeatsdriver.R
import com.bahamaeatsdriver.activity.job_history_details.JobHistoryDetails
import com.bahamaeatsdriver.helper.extensions.launchActivity
import com.bahamaeatsdriver.listeners.OnJobHistoryClick
import com.bahamaeatsdriver.model_class.job_history.JobHistoryResponse
import com.bahamaeatsdriver.model_class.job_history.PastJobHistory
import com.bahamaeatsdriver.model_class.job_history.UpComingJobHistory
import com.bahamaeatsdriver.repository.BaseViewModel
import kotlinx.android.synthetic.main.activity_deliveries_jobhistory.*

class Deliveries_jobhistory : AppCompatActivity(), View.OnClickListener, OnJobHistoryClick, Observer<RestObservable> {

    private val viewModel: BaseViewModel by lazy { ViewModelProvider(this).get(BaseViewModel::class.java) }
    private var pastJobHistory: List<PastJobHistory>? = null
    private var upComingJobHistory: UpComingJobHistory? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deliveries_jobhistory)
        iv_backArrow_jobhistory.setOnClickListener(this)
        viewModel.jobHistoryApi(this, true)
        viewModel.getJobHistoryResposne().observe(this, this)
    }

    fun tv_upcomming(view: View?) {
        tv_upcomming!!.setTextColor(ContextCompat.getColor(this,R.color.Black))
        tv_past!!.setTextColor(ContextCompat.getColor(this,R.color.colorTextView_new))
        View_upcoming!!.visibility = View.VISIBLE
        View_past!!.visibility = View.INVISIBLE

        if (upComingJobHistory != null && upComingJobHistory!!.id != 0) {
            Rv_jobhistory.visibility = View.VISIBLE
            no_dataAvailable.visibility = View.GONE
            setAdapter("upcoming", pastJobHistory!!, upComingJobHistory!!)
        } else {
            Rv_jobhistory.visibility = View.GONE
            no_dataAvailable.visibility = View.VISIBLE
        }


    }

    fun tv_past(view: View?) {
        tv_upcomming!!.setTextColor(ContextCompat.getColor(this,R.color.colorTextView_new))
        tv_past!!.setTextColor(ContextCompat.getColor(this,R.color.Black))
        View_upcoming!!.visibility = View.INVISIBLE
        View_past!!.visibility = View.VISIBLE
        if (pastJobHistory != null && pastJobHistory!!.isNotEmpty()) {
            Rv_jobhistory.visibility = View.VISIBLE
            no_dataAvailable.visibility = View.GONE
            setAdapter("past", pastJobHistory!!, upComingJobHistory!!)
        } else {
            Rv_jobhistory.visibility = View.GONE
            no_dataAvailable.visibility = View.VISIBLE
        }
    }


    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.iv_backArrow_jobhistory -> {
                finish()
            }
        }
    }


    override fun onPastJobHistoryClick(position: Int, data: PastJobHistory) {
        launchActivity<JobHistoryDetails>
        {
            putExtra("jobId", data.id.toString())
            putExtra("from", "PastJobHistory")
        }
    }

    override fun onUpComingJobHistoryClick(position: Int, data: UpComingJobHistory) {
      if(data.id!=0){
       launchActivity<JobHistoryDetails>
        {
            putExtra("jobId", data.id.toString())
            putExtra("from", "UpComingJobHistory")
        }
      }

    }

    override fun onChanged(liveData: RestObservable?) {
        when (liveData!!.status) {
            Status.SUCCESS -> {
                if (liveData.data is JobHistoryResponse) {
                    pastJobHistory = liveData.data.body.pastJobHistory
                    upComingJobHistory = liveData.data.body.upComingJobHistory
                    if (liveData.data.body.upComingJobHistory.id != 0) {
                        Rv_jobhistory.visibility = View.VISIBLE
                        no_dataAvailable.visibility = View.GONE
                        setAdapter("upcoming", pastJobHistory!!, upComingJobHistory!!)
                    } else {
                        Rv_jobhistory.visibility = View.GONE
                        no_dataAvailable.visibility = View.VISIBLE
                    }
                    tv_upcomming!!.setTextColor(ContextCompat.getColor(this,R.color.Black))
                    tv_past!!.setTextColor(ContextCompat.getColor(this,R.color.colorTextView_new))
                    View_upcoming!!.visibility = View.VISIBLE
                    View_past!!.visibility = View.INVISIBLE
                }
            }
            Status.ERROR -> {

            }
            else -> {


            }
        }
    }

    private fun setAdapter(type: String, pastJobHistory: List<PastJobHistory>, upComingJobHistory: UpComingJobHistory) {
        val linearLayoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
        Rv_jobhistory!!.layoutManager = linearLayoutManager
        val adapterItems = ItemsAdapter(this, type, pastJobHistory, upComingJobHistory, this)
        Rv_jobhistory!!.adapter = adapterItems
    }


}
