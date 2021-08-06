package com.bahamaeatsdriver.activity.driver_availability

import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bahamaeats.network.RestObservable
import com.bahamaeats.network.Status
import com.bahamaeatsdriver.Adapter.DriverAvailabilityAdapter
import com.bahamaeatsdriver.R
import com.bahamaeatsdriver.listeners.OnDriverAvailabilitySelection
import com.bahamaeatsdriver.model_class.add_driver_availability_slots.AddDriverAvailabilitySlots
import com.bahamaeatsdriver.model_class.driver_added_slots.DriverAddedSlotList
import com.bahamaeatsdriver.model_class.slots.AvailableSlot
import com.bahamaeatsdriver.model_class.slots.Body
import com.bahamaeatsdriver.model_class.slots.DriverSlots
import com.bahamaeatsdriver.repository.BaseViewModel
import kotlinx.android.synthetic.main.activity_driver_availablity.*
import org.json.JSONArray
import org.json.JSONObject
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class DriverAvailability : AppCompatActivity(), View.OnClickListener, OnDriverAvailabilitySelection, Observer<RestObservable> {

    private val viewModel: BaseViewModel by lazy { ViewModelProvider(this).get(BaseViewModel::class.java) }
    private var daysList = ArrayList<Body>()
    private var adapterSlots: DriverAvailabilityAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_driver_availablity)
        setOnClickListners()
        getDriverAvailableSlots()
    }

    private fun getDriverAddedAvailableSlots() {
        viewModel.getDriverAddedAvailableSlotsApi(this, true)
        viewModel.getDriverAddedAvailableSlotsResposne().observe(this, this)
    }

    private fun getDriverAvailableSlots() {
        viewModel.getdriverAvailableSlotsApi(this, true)
        viewModel.getDriverAvailableSlotsResposne().observe(this, this)
    }

    private fun selectStartDate(tv_startDate: TextView) {
        val mTimePicker: TimePickerDialog
        val c = Calendar.getInstance()
        val hour = c[Calendar.HOUR_OF_DAY]
        val minute = c[Calendar.MINUTE]

        mTimePicker = TimePickerDialog(this, { timePicker, selectedHour, selectedMinute ->
            val time = "$selectedHour:$selectedMinute"
            val fmt = SimpleDateFormat("HH:mm")
            var date: Date? = null
            try {
                date = fmt.parse(time)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            val fmtOut = SimpleDateFormat("hh:mm aa")
            val formattedTime: String = fmtOut.format(date)
            tv_startDate.setText(formattedTime)
        }, hour, minute, false) //No 24 hour time

        mTimePicker.setTitle("Select Time")
        mTimePicker.show()

    }

    private fun setOnClickListners() {
        tv_save.setOnClickListener(this)
        iv_backArrow_jobhistory.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.tv_save -> {
                var isSlotSelected = false
                val jsonArray = JSONArray()
                var slotIdMultiple = ""
                var slotObject: JSONObject
                for (i in 0 until daysList.size) {
                     slotObject = JSONObject()
                    for (j in 0 until daysList[i].availableSlotList.size) {
                        if (daysList[i].availableSlotList[j].isSelected) {
                            isSlotSelected=true
                            if (slotIdMultiple.isEmpty()) {
                                slotIdMultiple = daysList[i].availableSlotList[j].id.toString()
                            } else {
                                slotIdMultiple = slotIdMultiple + "," + daysList[i].availableSlotList[j].id.toString()
                            }
                        }
                    }
                    if (slotIdMultiple.isNotEmpty()){
                        slotObject.put("dayId", daysList[i].id)
                        slotObject.put("slotId", slotIdMultiple)
                        jsonArray.put(slotObject)
                        slotIdMultiple=""
                    }
                }
                if (isSlotSelected) {
                    Log.d("addDriverAvailablity: ",jsonArray.toString() )
                    viewModel.addDriverAvailablitySlotsApi(this, jsonArray.toString(), true)
                    viewModel.addDriverAvailablitySlotsResponse().observe(this, this)
                } else {
                    com.bahamaeatsdriver.helper.others.Helper.showErrorAlert(this, "Please select any slot.")
                }
                Log.d("", "onClick: ")
            }
            R.id.iv_backArrow_jobhistory -> {
                finish()
            }
        }
    }

    override fun OnDriverAvailabilitySelection(item: AvailableSlot, slotpos: Int, dayPos: Int) {
        if (!daysList[dayPos].availableSlotList[slotpos].isSelected) {
            daysList[dayPos].availableSlotList[slotpos].isSelected = true
            println(dayPos.toString() + "  dayPos" + slotpos + "  slotpos")
        } else {
            daysList[dayPos].availableSlotList[slotpos].isSelected = false
        }
        adapterSlots!!.notifyDataSetChanged()
        Log.d("OnDriverAvailab", "OnDriverAvailabilitySelection: ")
    }

    override fun onChanged(liveData: RestObservable?) {
        when (liveData!!.status) {
            Status.SUCCESS -> {
                if (liveData.data is DriverAddedSlotList) {
                    val driverAddedSlotsData=liveData.data.body
                    try {
                        if (driverAddedSlotsData.isNotEmpty()){
                            for (i in 0 until daysList.size){
                                for (j in 0 until daysList[i].availableSlotList.size){
                                    for (k in 0 until driverAddedSlotsData.size){
                                        if (driverAddedSlotsData[k].day==daysList[i].id.toInt()){
                                            for (l in 0 until driverAddedSlotsData[k].data.size){
                                                if (driverAddedSlotsData[k].data[l].id==daysList[i].availableSlotList[j].id){
                                                    daysList[i].availableSlotList[j].isSelected=true
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            adapterSlots!!.notifyDataSetChanged()
                        }
                    } catch (e: Exception) {
                    }
                }
                if (liveData.data is AddDriverAvailabilitySlots) {
                    com.bahamaeatsdriver.helper.others.Helper.showSuccessToast(this, liveData.data.message)
                    finish()
                }
                if (liveData.data is DriverSlots) {
                    daysList = liveData.data.body
                    adapterSlots = DriverAvailabilityAdapter(this, daysList, this)
                    rv_days.adapter = adapterSlots
                    if (daysList.isNotEmpty())
                        tv_noDataAvailable.visibility=View.GONE
                    else
                        tv_noDataAvailable.visibility=View.VISIBLE
                    /**
                     * GetAdriveAddessSlots list
                     */
                      getDriverAddedAvailableSlots()
                }
            }
        }
    }

}