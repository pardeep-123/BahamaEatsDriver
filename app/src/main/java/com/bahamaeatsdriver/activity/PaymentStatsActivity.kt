package com.bahamaeatsdriver.activity

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bahamaeats.network.RestObservable
import com.bahamaeats.network.Status
import com.bahamaeatsdriver.R
import com.bahamaeatsdriver.adapter.DriverEarnRewardPaymentAdapter
import com.bahamaeatsdriver.adapter.FilterPaymentAdapter
import com.bahamaeatsdriver.adapter.PaymentAdapter
import com.bahamaeatsdriver.helper.others.Helper
import com.bahamaeatsdriver.model_class.driver_payments.DriverPaymentsResposne
import com.bahamaeatsdriver.model_class.driver_tips_earning.DriverTipsAndEarning
import com.bahamaeatsdriver.repository.BaseViewModel
import kotlinx.android.synthetic.main.activity_payment__status.*
import kotlinx.android.synthetic.main.dialog_filter.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class PaymentStatsActivity : AppCompatActivity(), Observer<RestObservable> {

    private var Recycler_view: RecyclerView? = null
    private var rv_rewardEarnings: RecyclerView? = null
    private var LL_Day: LinearLayout? = null
    private var LL_week: LinearLayout? = null
    private var LL_months: LinearLayout? = null
    private var tv_day: TextView? = null
    private var tv_week: TextView? = null
    private var tv_months: TextView? = null
    private var isIssueDateSelected: Boolean = false
    private var isFilterApplied = 0
    private var type = "0"
    private val viewModel: BaseViewModel by lazy { ViewModelProvider(this).get(BaseViewModel::class.java) }

    private lateinit var activityReachDialog: Dialog
    private var date_startTimestamp = ""
    private var date_endDateTimestamp = ""
    private var filterStartDate = ""
    private var filterEndDate = ""
    private var dayStart = ""
    private var monthStart = ""
    private var yearStart = ""

    private var dayEnd = ""
    private var monthEnd = ""
    private var yearEnd = ""
    private var select_start_date = ""
    private var select_end_date = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment__status)
        Recycler_view = findViewById(R.id.Recycler_view)
        rv_rewardEarnings = findViewById(R.id.rv_rewardEarnings)
        getDateMonth()
        LL_Day = findViewById(R.id.LL_Day)
        LL_week = findViewById(R.id.LL_week)
        LL_months = findViewById(R.id.LL_months)
        tv_day = findViewById(R.id.tv_day)
        tv_week = findViewById(R.id.tv_week)
        tv_months = findViewById(R.id.tv_months)
        callFilterByDate("", "", type)
        updateViewAsPerType(type)
        LL_Day?.setOnClickListener {

            try {
                type = "0"
                callFilterByDate("", "", type)
                updateViewAsPerType(type)
            } catch (e: Exception) {
            }
        }
        LL_week?.setOnClickListener {
            try {
                type = "1"
                callFilterByDate("", "", type)
                updateViewAsPerType(type)
            } catch (e: Exception) {
            }
        }
        LL_months?.setOnClickListener {
            try {
                type = "2"
                callFilterByDate("", "", type)
                updateViewAsPerType(type)
            } catch (e: Exception) {
            }
        }
        iv_filter.setOnClickListener {
            popupFilter()
        }
        tv_filterClear.setOnClickListener {
            date_startTimestamp = ""
            date_endDateTimestamp = ""
            filterStartDate = ""
            filterEndDate = ""
            activityReachDialog.tv_start_date.text = ""
            activityReachDialog.tv_end_date.text = ""
            activityReachDialog.tv_start_date.hint = "Select start date"
            activityReachDialog.tv_end_date.hint = "Select end date"
            type = "0"
            callFilterByDate("", "", type)
            updateViewAsPerType(type)
            isFilterApplied = 0
            tv_filterClear.visibility = View.GONE
            LL_days.visibility = View.VISIBLE
            ll_rootDates.visibility = View.VISIBLE
        }
        activityReachDialog = Dialog(this, R.style.Theme_Dialog)
        activityReachDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        activityReachDialog.setContentView(R.layout.dialog_filter)
        activityReachDialog.window!!.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)
        activityReachDialog.setCancelable(true)
        activityReachDialog.setCanceledOnTouchOutside(false)
        activityReachDialog.window!!.setGravity(Gravity.CENTER)
        activityReachDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    private fun updateViewAsPerType(type: String) {
        if (type == "0"){
            tv_day?.setTextColor(ContextCompat.getColor(this,R.color.White))
            tv_week?.setTextColor(ContextCompat.getColor(this,R.color.Black))
            tv_months?.setTextColor(ContextCompat.getColor(this,R.color.Black))
            LL_Day!!.setBackgroundResource(R.drawable.green_bg)
            LL_week!!.setBackgroundResource(R.drawable.white_bg)
            LL_months!!.setBackgroundResource(R.drawable.white_bg)
        }else  if (type == "1"){

            tv_day?.setTextColor(ContextCompat.getColor(this,R.color.Black))
            tv_week?.setTextColor(ContextCompat.getColor(this,R.color.White))
            tv_months?.setTextColor(ContextCompat.getColor(this,R.color.Black))
            LL_Day!!.setBackgroundResource(R.drawable.white_bg)
            LL_week!!.setBackgroundResource(R.drawable.green_bg)
            LL_months!!.setBackgroundResource(R.drawable.white_bg)
        }else  if (type == "2"){

            tv_day?.setTextColor(ContextCompat.getColor(this,R.color.Black))
            tv_week?.setTextColor(ContextCompat.getColor(this,R.color.Black))
            tv_months?.setTextColor(ContextCompat.getColor(this,R.color.White))
            LL_Day!!.setBackgroundResource(R.drawable.white_bg)
            LL_week!!.setBackgroundResource(R.drawable.white_bg)
            LL_months!!.setBackgroundResource(R.drawable.green_bg)
        }

    }

    private fun getDateMonth() {
        val c1 = Calendar.getInstance()
        //first day of week
        c1[Calendar.DAY_OF_WEEK] = 1
        //last day of week
        c1[Calendar.DAY_OF_WEEK] = 7
        val sdf = SimpleDateFormat("EEEE")
        val d = Date()
        val dayOfTheWeek = sdf.format(d)
        val dateFormat: DateFormat = SimpleDateFormat("MMMM")
        val date = Date()
        /**/
       val calendar = Calendar.getInstance()
        Log.v("Current Week", java.lang.String.valueOf(calendar.get(Calendar.WEEK_OF_YEAR)))
        val current_week: Int = calendar.get(Calendar.WEEK_OF_YEAR)
        val week_start_day: Int = calendar.firstDayOfWeek // this will get the starting day os week in integer format i-e 1 if monday
        // get the starting and ending date
        // Set the calendar to sunday of the current week
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        println("Current week = " + Calendar.DAY_OF_WEEK)
        // Print dates of the current week starting on Sunday
//        val df: DateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val df: DateFormat = SimpleDateFormat("dd MMM", Locale.getDefault())
        val startDate1: String
        var endDate1 = ""
        startDate1 = df.format(calendar.time)
        calendar.add(Calendar.DATE, 6)
        endDate1 = df.format(calendar.time)
        println("Start Date = $startDate1")
        println("End Date = $endDate1")
        tv_currentWeek.text = "$startDate1-$endDate1"
        tv_currentMonth.text = dateFormat.format(date)
        tv_currentDate.text  = dayOfTheWeek
    }

    fun calenderDateToTimeStamp(
        str_date: String?,
        date_formate: String?
    ): Long {
        var time_stamp = java.lang.Long.valueOf(0)
        try {
            val formatter = SimpleDateFormat(date_formate)
            val date: Date = formatter.parse(str_date)
            time_stamp = date.time
            time_stamp = time_stamp / 1000
        } catch (ex: java.lang.Exception) {
            ex.printStackTrace()
        }
        return time_stamp
    }

    private fun selectStartDate(tv_startDate: TextView) {
        val mYear: Int
        val mMonth: Int
        val mDay: Int
        val mcurrentDate = Calendar.getInstance()
        mYear = mcurrentDate[Calendar.YEAR]
        mMonth = mcurrentDate[Calendar.MONTH]
        mDay = mcurrentDate[Calendar.DAY_OF_MONTH]


        val mDatePicker = DatePickerDialog(
            this,
            { datepicker, selectedyear, selectedmonth, selectedday ->
                if (selectedday.toString().length == 1)
                    dayStart = "0$selectedday"
                else
                    dayStart = selectedday.toString()

                if ((selectedmonth + 1).toString().length == 1)
                    monthStart = "0" + (selectedmonth + 1).toString()
                else monthStart = (selectedmonth + 1).toString()

                yearStart = selectedyear.toString()

                date_startTimestamp = calenderDateToTimeStamp(
                    "$dayStart-$monthStart-$yearStart",
                    "dd-MM-yyyy"
                ).toString()
                //date_filter=2021-03-25
//                    filterStartDate = yearStart + "/" + monthStart + "/" + dayStart
                filterStartDate = dayStart + "/" + monthStart + "/" + yearStart
                Log.e("day", dayStart)
                Log.e("month", monthStart)
                Log.e("year", selectedyear.toString())
                Log.e("date_timestamp", date_startTimestamp)
                select_start_date = "$dayStart/$monthStart/$selectedyear"

                tv_startDate.text = "$dayStart/$monthStart/$selectedyear"
                isIssueDateSelected = true

            },
            mYear,
            mMonth,
            mDay
        )

        mDatePicker.datePicker.maxDate = System.currentTimeMillis()

// mDatePicker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

        //mDatePicker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        if (!mDatePicker.isShowing) {
            mDatePicker.show()
        }

    }

    private fun selectEndDate(tv_EndDate: TextView) {
        val mYear: Int
        val mMonth: Int
        val mDay: Int
        val mcurrentDate = Calendar.getInstance()
        mYear = mcurrentDate[Calendar.YEAR]
        mMonth = mcurrentDate[Calendar.MONTH]
        mDay = mcurrentDate[Calendar.DAY_OF_MONTH]


        val mDatePicker = DatePickerDialog(
            this,
            { datepicker, selectedyear, selectedmonth, selectedday ->
                if (selectedday.toString().length == 1)
                    dayEnd = "0$selectedday"
                else
                    dayEnd = selectedday.toString()

                if ((selectedmonth + 1).toString().length == 1)
                    monthEnd = "0" + (selectedmonth + 1).toString()
                else monthEnd = (selectedmonth + 1).toString()

                yearEnd = selectedyear.toString()

                date_endDateTimestamp = calenderDateToTimeStamp(
                    "$dayEnd-$monthEnd-$yearEnd",
                    "dd-MM-yyyy"
                ).toString()
                //date_filter=2021-03-25
//            filterEndDate = yearEnd + "-" + monthEnd + "-" + dayEnd
                filterEndDate = "$dayEnd/$monthEnd/$yearEnd"
                Log.e("day", dayEnd)
                Log.e("month", monthEnd)
                Log.e("year", selectedyear.toString())
                Log.e("date_timestamp", date_endDateTimestamp)
                select_end_date = dayEnd + "/" + monthEnd + "/" + selectedyear

                tv_EndDate.text = "$dayEnd/$monthEnd/$selectedyear"
            },
            mYear,
            mMonth,
            mDay
        )
        mcurrentDate[yearStart.toInt(), monthStart.toInt() - 1] = dayStart.toInt()
        val value = mcurrentDate.timeInMillis
//        mDatePicker.datePicker.maxDate = value
        mDatePicker.datePicker.maxDate = System.currentTimeMillis()
        if (!mDatePicker.isShowing) {
            mDatePicker.show()
        }

    }

    private fun popupFilter() {

        if (!activityReachDialog.isShowing) {

            activityReachDialog.tv_start_date.setOnClickListener {
                selectStartDate(activityReachDialog.tv_start_date)
            }
            activityReachDialog.tv_end_date.setOnClickListener {
                if (isIssueDateSelected) {
                    selectEndDate(activityReachDialog.tv_end_date)
                } else {
                    Helper.showSuccessToast(this, "Please select start date")
                }
            }

            activityReachDialog.iv_cross_new.setOnClickListener {
                activityReachDialog.dismiss()
            }
            activityReachDialog.tv_clearFilter.setOnClickListener {
                if (filterStartDate.isEmpty()) {
                    Helper.showSuccessToast(this, "No filter applied")
                } else {
                    activityReachDialog.dismiss()
                    date_startTimestamp = ""
                    date_endDateTimestamp = ""
                    filterStartDate = ""
                    filterEndDate = ""
                    activityReachDialog.tv_start_date.text = ""
                    activityReachDialog.tv_end_date.text = ""
                    activityReachDialog.tv_start_date.hint = "Select start date"
                    activityReachDialog.tv_end_date.hint = "Select end date"
                    type = "0"
                    callFilterByDate("", "", type)
                    updateViewAsPerType(type)
                    LL_days.visibility = View.VISIBLE
                    ll_rootDates.visibility = View.VISIBLE
                    tv_filterClear.visibility = View.GONE
                    isFilterApplied = 0
                }

            }
            activityReachDialog.setOnDismissListener {
                if (isFilterApplied != 0)
                    tv_filterClear.visibility = View.VISIBLE
                else
                    tv_filterClear.visibility = View.GONE

            }


            activityReachDialog.tv_submit.setOnClickListener {
                if (activityReachDialog.tv_start_date.text.isNullOrEmpty()) {
                    Helper.showSuccessToast(this, "Please select start date")
                } else if (activityReachDialog.tv_end_date.text.isNullOrEmpty()) {
                    Helper.showSuccessToast(this, "Please select end date")
                } else if (!activityReachDialog.tv_start_date.text.isNullOrEmpty() && !activityReachDialog.tv_end_date.text.isNullOrEmpty()) {
                    type = ""
                    callFilterByDate(filterStartDate, filterEndDate, type)
                    activityReachDialog.dismiss()
//                    if (date_endDateTimestamp<date_startTimestamp)
//                        Helper.showSuccessToast(this, "Please select valid date")
//                    else {
//                        //date_timestamp
////                        callNearByUpdateMethod(filterDate)
//                        activityReachDialog.dismiss()
//
//                    }


                }/* else {
                    //date_timestamp
//                        callNearByUpdateMethod(filterDate)
                    activityReachDialog.dismiss()

                }*/

            }
            activityReachDialog.show()
        }


    }

    /***
     * type==0/1/2 day week month
     */
    private fun callFilterByDate(filterStartDate: String, filterEndDate: String, type: String) {

        if (filterEndDate.isEmpty() && filterEndDate.isEmpty()) {
            /***
             * Get payment status
             */
            viewModel.paymentStatusListApi(this, type, true)
            viewModel.getPaymentStatusListResposne().observe(this, this)
            /***
             * Get total earnings API call
             */
//            viewModel.driverEarningsApi(this, false)
//            viewModel.getEarningResposneResposne().observe(this, this)
        } else {
            viewModel.driverFilterEarningsApi(this, filterStartDate, filterEndDate, true)
            viewModel.getfilterEarningResposneResposne().observe(this, this)
        }

    }


    fun iv_backArrow_payment(view: View) {
        finish()
    }

    @SuppressLint("SetTextI18n")
    override fun onChanged(liveData: RestObservable?) {
        when (liveData!!.status) {
            Status.SUCCESS -> {
                if (liveData.data is DriverPaymentsResposne) {
                    tv_tipDelivery.text = "$ " + changeDoubleFormat(liveData.data.body.earning.totalTip)
                    tv_totalDelivery.text = "$ " +changeDoubleFormat(liveData.data.body.earning.totalDeliveryFee)
                    tv_refferBenefit.text = "$ " +if (!liveData.data.body.earning.referalBonus.isNullOrEmpty())changeDoubleFormat(liveData.data.body.earning.referalBonus.toDouble()) else 0.0
                    if (liveData.data.body.order.isEmpty()) {
                        tv_noDataAvailable.visibility = View.VISIBLE
                        Recycler_view!!.visibility = View.GONE
                    } else {
                        tv_noDataAvailable.visibility = View.GONE
                        Recycler_view!!.visibility = View.VISIBLE
                        val linearLayoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
                        Recycler_view?.layoutManager = linearLayoutManager
                        val adapterItems = PaymentAdapter(this, liveData.data.body.order)
                        Recycler_view?.adapter = adapterItems
                    }
                    if (liveData.data.body.refferalData.isNotEmpty()){
                        tv_noRewardAvailable.visibility=View.GONE
                        rv_rewardEarnings?.visibility=View.VISIBLE
                        rl_earnRewardRoot.visibility=View.VISIBLE
                        val driverEarnsRewardsAdapter = DriverEarnRewardPaymentAdapter(this,liveData.data.body.refferalData)
                        rv_rewardEarnings?.adapter = driverEarnsRewardsAdapter
                    }else{
                        rl_earnRewardRoot.visibility=View.GONE
                        tv_noRewardAvailable.visibility=View.VISIBLE
                        rv_rewardEarnings?.visibility=View.GONE
                    }
                }

                if (liveData.data is DriverTipsAndEarning) {
                    LL_days.visibility = View.GONE
                    ll_rootDates.visibility = View.GONE
                    isFilterApplied = 1
                    if (isFilterApplied != 0)
                        tv_filterClear.visibility = View.VISIBLE
                    else
                        tv_filterClear.visibility = View.GONE

                        tv_totalDelivery.text ="$ " +if (liveData.data.body.earnings.totalDeliveryFee.isNotEmpty()) changeDoubleFormat(liveData.data.body.earnings.totalDeliveryFee.toDouble())
                    else
                        tv_totalDelivery.text ="$ " + liveData.data.body.earnings.totalDeliveryFee

                    if (liveData.data.body.earnings.totalTip.isNotEmpty())
                        tv_tipDelivery.text = "$ " + changeDoubleFormat(liveData.data.body.earnings.totalTip.toDouble())
                    else
                        tv_tipDelivery.text = "$ " + liveData.data.body.earnings.totalTip

                    if (liveData.data.body.earnings.referalBonus.isNotEmpty())
                        tv_refferBenefit.text = "$ " + changeDoubleFormat(liveData.data.body.earnings.referalBonus.toDouble())
                    else
                        tv_refferBenefit.text = "$ " + 0.0

                    if (liveData.data.body.rideRequest.isEmpty()) {
                        tv_noDataAvailable.visibility = View.VISIBLE
                        Recycler_view!!.visibility = View.GONE
                    } else {
                        tv_noDataAvailable.visibility = View.GONE
                        Recycler_view!!.visibility = View.VISIBLE
                        val linearLayoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
                        Recycler_view?.layoutManager = linearLayoutManager
                        val adapterItems = FilterPaymentAdapter(this, liveData.data.body.rideRequest)
                        Recycler_view?.adapter = adapterItems
                    }
                    if (liveData.data.body.refferalData.isNotEmpty()){
                        tv_noRewardAvailable.visibility=View.GONE
                        rl_earnRewardRoot.visibility=View.VISIBLE
                        rv_rewardEarnings?.visibility=View.VISIBLE
                        val driverEarnsRewardsAdapter = DriverEarnRewardPaymentAdapter(this,liveData.data.body.refferalData)
                        rv_rewardEarnings?.adapter = driverEarnsRewardsAdapter
                    }else{
                        rv_rewardEarnings?.visibility=View.GONE
                        rl_earnRewardRoot.visibility=View.GONE
                        tv_noRewardAvailable.visibility=View.VISIBLE
                    }
                }
            }
            Status.ERROR -> {

            }
            else -> {


            }
        }
    }

    private fun changeDoubleFormat(number: Double): String {
        try {
            val df = String.format("%.2f", number)
            return df.format(number)
        } catch (e: Exception) {
            return ""
        }
    }
}