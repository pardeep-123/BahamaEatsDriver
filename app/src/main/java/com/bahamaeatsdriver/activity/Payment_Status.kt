package com.bahamaeatsdriver.activity

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.Typeface
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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bahamaeats.network.RestObservable
import com.bahamaeats.network.Status
import com.bahamaeatsdriver.Adapter.FilterPaymentAdapter
import com.bahamaeatsdriver.Adapter.PaymentAdapter
import com.bahamaeatsdriver.R
import com.bahamaeatsdriver.helper.others.Helper
import com.bahamaeatsdriver.model_class.driver_earnings.Day
import com.bahamaeatsdriver.model_class.driver_earnings.Month
import com.bahamaeatsdriver.model_class.driver_earnings.Week
import com.bahamaeatsdriver.model_class.driver_payments.DriverPaymentsResposne
import com.bahamaeatsdriver.model_class.driver_tips_earning.DriverTipsAndEarning
import com.bahamaeatsdriver.repository.BaseViewModel
import kotlinx.android.synthetic.main.activity_payment__status.*
import kotlinx.android.synthetic.main.dialog_filter.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class Payment_Status : AppCompatActivity(), Observer<RestObservable> {

    private var Recycler_view: RecyclerView? = null
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
//    private lateinit var dayEarning: Day
//    private lateinit var monthEarning: Month
//    private lateinit var weeklyEarning: Week

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
        getDateMonth()
        LL_Day = findViewById(R.id.LL_Day)
        LL_week = findViewById(R.id.LL_week)
        LL_months = findViewById(R.id.LL_months)
        tv_day = findViewById(R.id.tv_day)
        tv_week = findViewById(R.id.tv_week)
        tv_months = findViewById(R.id.tv_months)
        callFilterByDate("", "", type)
        LL_Day?.setOnClickListener {

            try {
                tv_day!!.setTypeface(Typeface.DEFAULT_BOLD)
                tv_week!!.setTypeface(null, Typeface.NORMAL)
                tv_months!!.setTypeface(null, Typeface.NORMAL)
                tv_day?.setTextColor(resources.getColor(R.color.Black))
                tv_week?.setTextColor(resources.getColor(R.color.colorTextView))
                tv_months?.setTextColor(resources.getColor(R.color.colorTextView))
                type = "0"
                callFilterByDate("", "", type)
                /*  if (dayEarning != null) {
                      updateDeliveryAmount(1, dayEarning, monthEarning, weeklyEarning)
                  }*/
            } catch (e: Exception) {
            }
        }
        LL_week?.setOnClickListener {
            try {
                tv_day!!.setTypeface(null, Typeface.NORMAL)
                tv_week!!.setTypeface(Typeface.DEFAULT_BOLD)
                tv_months!!.setTypeface(null, Typeface.NORMAL)
                tv_day?.setTextColor(resources.getColor(R.color.colorTextView))
                tv_week?.setTextColor(resources.getColor(R.color.Black))
                tv_months?.setTextColor(resources.getColor(R.color.colorTextView))
                /*  if (weeklyEarning != null) {
                      updateDeliveryAmount(3, dayEarning, monthEarning, weeklyEarning)
                  }*/
                type = "1"
                callFilterByDate("", "", type)
//                updateViewAsPerType(type)
            } catch (e: Exception) {
            }
        }
        LL_months?.setOnClickListener {
            try {
                tv_day!!.setTypeface(null, Typeface.NORMAL)
                tv_week!!.setTypeface(null, Typeface.NORMAL)
                tv_months!!.setTypeface(Typeface.DEFAULT_BOLD)
                tv_day?.setTextColor(resources.getColor(R.color.colorTextView))
                tv_week?.setTextColor(resources.getColor(R.color.colorTextView))
                tv_months?.setTextColor(resources.getColor(R.color.Black))
                /* if (monthEarning != null) {
                     updateDeliveryAmount(2, dayEarning, monthEarning, weeklyEarning)
                 }*/
                type = "2"
                callFilterByDate("", "", type)
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
            isFilterApplied = 0
            tv_filterClear.visibility = View.GONE
            LL_days.visibility = View.VISIBLE
            ll_rootDates.visibility = View.VISIBLE
        }
        activityReachDialog = Dialog(this, R.style.Theme_Dialog)
        activityReachDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        activityReachDialog.setContentView(R.layout.dialog_filter)
        activityReachDialog.getWindow()!!.setLayout(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        activityReachDialog.setCancelable(true)
        activityReachDialog.setCanceledOnTouchOutside(false)
        activityReachDialog.getWindow()!!.setGravity(Gravity.CENTER)
        activityReachDialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    private fun getDateMonth() {
        val c1 = Calendar.getInstance()
        //first day of week
        c1[Calendar.DAY_OF_WEEK] = 1
        val year1 = c1[Calendar.YEAR]
        val month1 = c1[Calendar.MONTH] + 1
        val day1 = c1[Calendar.DAY_OF_MONTH]
        val dateFormat1: DateFormat = SimpleDateFormat("MMM")
        val date1 = Date()
//        val startDate = day1 + "/" + month1 + "/" + year1
//        val startDate =day1.toString()+"/" + month1 + "/" + year1
        val startDate =day1.toString()+" " +dateFormat1.format(date1)
        //last day of week
        c1[Calendar.DAY_OF_WEEK] = 7

        val year7 = c1[Calendar.YEAR]
        val month7 = c1[Calendar.MONTH] + 1
        val day7 = c1[Calendar.DAY_OF_MONTH]
//        val endDate = day7.toString() + "/" + month7 + "/" + year7
        val endDate = day7.toString()+" " +dateFormat1.format(date1)
        val weekDates =startDate+" - "+endDate

        val sdf = SimpleDateFormat("EEEE")
        val d = Date()
        val dayOfTheWeek = sdf.format(d)
        tv_currentWeek.text =weekDates
        val dateFormat: DateFormat = SimpleDateFormat("MMMM")
        val date = Date()
        tv_currentMonth.text = dateFormat.format(date)
//        tv_currentDate.text  = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        tv_currentDate.text  = dayOfTheWeek
    }

    /*private fun updateViewAsPerType(type: String) {
        if (type.equals("0")){

        }else  if (type.equals("1")){

        }else  if (type.equals("2")){

        }

    }*/


    fun calenderDateToTimeStamp(
        str_date: String?,
        date_formate: String?
    ): Long {
        var time_stamp = java.lang.Long.valueOf(0)
        try {
            val formatter = SimpleDateFormat(date_formate)
            //SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
            val date: Date = formatter.parse(str_date)
            time_stamp = date.time
            time_stamp = time_stamp / 1000
        } catch (ex: java.lang.Exception) {
            ex.printStackTrace()
        }

        //
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
            DatePickerDialog.OnDateSetListener { datepicker, selectedyear, selectedmonth, selectedday ->
                if (selectedday.toString().length == 1)
                    dayStart = "0$selectedday"
                else
                    dayStart = selectedday.toString()

                if ((selectedmonth + 1).toString().length == 1)
                    monthStart = "0" + (selectedmonth + 1).toString()
                else monthStart = (selectedmonth + 1).toString()

                yearStart = selectedyear.toString()

                date_startTimestamp = calenderDateToTimeStamp(
                    dayStart + "-" + monthStart + "-" + yearStart,
                    "dd-MM-yyyy"
                ).toString()
                //date_filter=2021-03-25
//                    filterStartDate = yearStart + "/" + monthStart + "/" + dayStart
                filterStartDate = dayStart + "/" + monthStart + "/" + yearStart
                Log.e("day", dayStart)
                Log.e("month", monthStart)
                Log.e("year", selectedyear.toString())
                Log.e("date_timestamp", date_startTimestamp)
                select_start_date = dayStart + "/" + monthStart + "/" + selectedyear

                tv_startDate.setText(dayStart + "/" + monthStart + "/" + selectedyear)
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
            DatePickerDialog.OnDateSetListener { datepicker, selectedyear, selectedmonth, selectedday ->
                if (selectedday.toString().length == 1)
                    dayEnd = "0$selectedday"
                else
                    dayEnd = selectedday.toString()

                if ((selectedmonth + 1).toString().length == 1)
                    monthEnd = "0" + (selectedmonth + 1).toString()
                else monthEnd = (selectedmonth + 1).toString()

                yearEnd = selectedyear.toString()

                date_endDateTimestamp = calenderDateToTimeStamp(
                    dayEnd + "-" + monthEnd + "-" + yearEnd,
                    "dd-MM-yyyy"
                ).toString()
                //date_filter=2021-03-25
//            filterEndDate = yearEnd + "-" + monthEnd + "-" + dayEnd
                filterEndDate = dayEnd + "/" + monthEnd + "/" + yearEnd
                Log.e("day", dayEnd)
                Log.e("month", monthEnd)
                Log.e("year", selectedyear.toString())
                Log.e("date_timestamp", date_endDateTimestamp)
                select_end_date = dayEnd + "/" + monthEnd + "/" + selectedyear

                tv_EndDate.setText(dayEnd + "/" + monthEnd + "/" + selectedyear)
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
    fun updateDeliveryAmount(type: Int, dayEarning: Day, monthEarning: Month, weeklyEarning: Week) {
        /***
         * Type=1 for dayEarning
         * Type=2 for monthEarning
         * Type=3 for weeklyEarning
         */
        if (type == 1) {
            if (dayEarning.total_delivery_fee != null && !dayEarning.total_delivery_fee.isEmpty())
                tv_totalDelivery.text =
                    "$ " + changeDoubleFormat(dayEarning.total_delivery_fee.toDouble())
            else
                tv_totalDelivery.text = "$ " + dayEarning.total_delivery_fee

            if (dayEarning.total_tip != null && !dayEarning.total_tip.isEmpty())
                tv_tipDelivery.text = "$ " + changeDoubleFormat(dayEarning.total_tip.toDouble())
            else
                tv_tipDelivery.text = "$ " + dayEarning.total_tip


        } else if (type == 2) {
            if (monthEarning.total_delivery_fee != null && !monthEarning.total_delivery_fee.isEmpty())
                tv_totalDelivery.text =
                    "$ " + changeDoubleFormat(monthEarning.total_delivery_fee.toDouble())
            else
                tv_totalDelivery.text = "$ " + monthEarning.total_delivery_fee.toDouble()

            if (monthEarning.total_tip != null && !monthEarning.total_tip.isEmpty())
                tv_tipDelivery.text = "$ " + changeDoubleFormat(monthEarning.total_tip.toDouble())
            else
                tv_tipDelivery.text = "$ " + changeDoubleFormat(monthEarning.total_tip.toDouble())
        } else if (type == 3) {
            if (weeklyEarning.total_delivery_fee != null && !weeklyEarning.total_delivery_fee.isEmpty())
                tv_totalDelivery.text =
                    "$ " + changeDoubleFormat(weeklyEarning.total_delivery_fee.toDouble())
            else
                tv_totalDelivery.text = "$ " + weeklyEarning.total_delivery_fee

            if (weeklyEarning.total_tip != null && !weeklyEarning.total_tip.isEmpty())
                tv_tipDelivery.text = "$ " + changeDoubleFormat(weeklyEarning.total_tip.toDouble())
            else
                tv_tipDelivery.text = "$ " + weeklyEarning.total_tip
        }

    }

    @SuppressLint("SetTextI18n")
    override fun onChanged(liveData: RestObservable?) {
        when (liveData!!.status) {
            Status.SUCCESS -> {
                if (liveData.data is DriverPaymentsResposne) {
                    tv_tipDelivery.text = "$ " + liveData.data.body.earning.total_tip
                    tv_totalDelivery.text = "$ " + liveData.data.body.earning.total_delivery_fee
//                    if (isFilterApplied != 0)
//                        tv_filterClear.visibility = View.VISIBLE
//                    else
//                        tv_filterClear.visibility = View.GONE

                    if (liveData.data.body.order.isEmpty()) {
                        tv_noDataAvailable.visibility = View.VISIBLE
                        Recycler_view!!.visibility = View.GONE
                    } else {
                        tv_noDataAvailable.visibility = View.GONE
                        Recycler_view!!.visibility = View.VISIBLE
                        val linearLayoutManager = LinearLayoutManager(
                            applicationContext,
                            LinearLayoutManager.VERTICAL,
                            false
                        )
                        Recycler_view?.setLayoutManager(linearLayoutManager)
                        val adapterItems = PaymentAdapter(this, liveData.data.body.order)
                        Recycler_view?.setAdapter(adapterItems)
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

                    if (liveData.data.body.earnings.total_delivery_fee != null && !liveData.data.body.earnings.total_delivery_fee.isEmpty())
                        tv_totalDelivery.text =
                            "$ " + changeDoubleFormat(liveData.data.body.earnings.total_delivery_fee.toDouble())
                    else
                        tv_totalDelivery.text =
                            "$ " + liveData.data.body.earnings.total_delivery_fee

                    if (liveData.data.body.earnings.total_tip != null && !liveData.data.body.earnings.total_tip.isEmpty())
                        tv_tipDelivery.text =
                            "$ " + changeDoubleFormat(liveData.data.body.earnings.total_tip.toDouble())
                    else
                        tv_tipDelivery.text = "$ " + liveData.data.body.earnings.total_tip

                    if (liveData.data.body.rideRequest.isEmpty()) {
                        tv_noDataAvailable.visibility = View.VISIBLE
                        Recycler_view!!.visibility = View.GONE
                    } else {
                        tv_noDataAvailable.visibility = View.GONE
                        Recycler_view!!.visibility = View.VISIBLE
                        val linearLayoutManager = LinearLayoutManager(
                            applicationContext,
                            LinearLayoutManager.VERTICAL,
                            false
                        )
                        Recycler_view?.setLayoutManager(linearLayoutManager)
                        val adapterItems =
                            FilterPaymentAdapter(this, liveData.data.body.rideRequest)
                        Recycler_view?.setAdapter(adapterItems)
                    }
                }
                /* if (liveData.data is DriverEarningResposne) {
                     LL_days.visibility = View.VISIBLE
                     dayEarning = liveData.data.body.day
                     monthEarning = liveData.data.body.month
                     weeklyEarning = liveData.data.body.week
                     updateDeliveryAmount(1, dayEarning, monthEarning, weeklyEarning)
                 }*/
            }
            Status.ERROR -> {

            }
            else -> {


            }
        }
    }

    fun changeDoubleFormat(number: Double): String {
        try {
            val df = String.format("%.2f", number)
            return df.format(number)
        } catch (e: Exception) {
            return ""
        }

    }
}