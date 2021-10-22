package com.bahamaeatsdriver.activity

import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bahamaeatsdriver.adapter.ScheduleAvailableAdapter
import com.bahamaeatsdriver.adapter.Schedule_Sch_Adapter
import com.bahamaeatsdriver.adapter.ScheduletimeAdapter
import com.bahamaeatsdriver.R
import java.util.*

class Schedule_time : AppCompatActivity() {
    var Recycler_schedule_time: RecyclerView? = null
    var Recycler_Avaliable_schedule: RecyclerView? = null
    var Recycler_schedule_Sch: RecyclerView? = null
    var endtime = ArrayList<String>()
    var starttime = ArrayList<String>()
    var date = ArrayList<String>()
    var week = ArrayList<String>()
    var Tv_schedule: TextView? = null
    var Tv_Available: TextView? = null
    var Relativ_Avaliable: RelativeLayout? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule_time)
        Recycler_schedule_Sch = findViewById(R.id.Recycler_schedule_Sch)
        Tv_schedule = findViewById(R.id.Tv_schedule)
        Tv_Available = findViewById(R.id.Tv_Available)
        Relativ_Avaliable = findViewById(R.id.Relativ_Avaliable)
        Recycler_Avaliable_schedule = findViewById(R.id.Recycler_Avaliable_schedule)
        Recycler_schedule_time = findViewById(R.id.Recycler_schedule_time)
        starttime.add("9:30")
        starttime.add("8:10")
        starttime.add("2:30")
        starttime.add("6:20")
        starttime.add("7:30")
        starttime.add("5:50")
        endtime.add("9:30")
        endtime.add("8:10")
        endtime.add("2:30")
        endtime.add("6:20")
        endtime.add("7:30")
        endtime.add("5:50")
        date.add("8")
        date.add("9")
        date.add("10")
        date.add("11")
        date.add("12")
        date.add("13")
        date.add("14")
        week.add("Mon")
        week.add("Tue")
        week.add("Wed")
        week.add("Thur")
        week.add("Fri")
        week.add("Sat")
        week.add("Sun")
        Recycler_schedule_Sch?.setVisibility(View.GONE)
        val linearLayoutManager2 = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
        Recycler_Avaliable_schedule?.setLayoutManager(linearLayoutManager2)
        val adapterItems2 = ScheduleAvailableAdapter(this, starttime, endtime)
        Recycler_Avaliable_schedule?.setAdapter(adapterItems2)
        val linearLayoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)
        Recycler_schedule_time?.setLayoutManager(linearLayoutManager)
        val adapterItems = ScheduletimeAdapter(this, date, week)
        Recycler_schedule_time?.setAdapter(adapterItems)
    }

    fun Tv_Available(view: View?) {
        Relativ_Avaliable!!.visibility = View.VISIBLE
        Recycler_schedule_Sch!!.visibility = View.GONE
        Tv_schedule!!.setBackgroundColor(resources.getColor(R.color.Black))
        Tv_Available!!.setBackgroundColor(resources.getColor(R.color.Greenapp))
        val linearLayoutManager2 = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
        Recycler_Avaliable_schedule!!.layoutManager = linearLayoutManager2
        val adapterItems2 = ScheduleAvailableAdapter(this, starttime, endtime)
        Recycler_Avaliable_schedule!!.adapter = adapterItems2
    }

    fun Tv_schedule(view: View?) {
        Tv_schedule!!.setBackgroundColor(resources.getColor(R.color.Greenapp))
        Tv_Available!!.setBackgroundColor(resources.getColor(R.color.Black))
        val linearLayoutManager2 = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
        Recycler_schedule_Sch!!.layoutManager = linearLayoutManager2
        val adapterItems2 = Schedule_Sch_Adapter(this, starttime, endtime)
        Recycler_schedule_Sch!!.adapter = adapterItems2
        Relativ_Avaliable!!.visibility = View.GONE
        Recycler_schedule_Sch!!.visibility = View.VISIBLE
    }
}