package com.bahamaeatsdriver.activity.Navigation

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bahamaeatsdriver.R

class My_Wallet_Activity : AppCompatActivity() {
    private var Tv_Depost: TextView? = null
    private var Tv_statistics: TextView? = null
    private var LL_mainlayout_statistic: LinearLayout? = null
    private var LL_mainlayout_deposit: LinearLayout? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_wallet)
        Tv_Depost = findViewById(R.id.Tv_Depost)
        Tv_statistics = findViewById(R.id.Tv_statistics)
        LL_mainlayout_statistic = findViewById(R.id.LL_mainlayout_statistic)
        LL_mainlayout_deposit = findViewById(R.id.LL_mainlayout_deposit)
    }

    fun iv_backArrow_waller(view: View?) {
        onBackPressed()
    }

    fun Tv_statistics(view: View?) {
        Tv_statistics!!.setBackgroundColor(ContextCompat.getColor(this,R.color.Greenapp))
        Tv_Depost!!.setBackgroundColor(ContextCompat.getColor(this,R.color.Black))
        LL_mainlayout_statistic!!.visibility = View.VISIBLE
        LL_mainlayout_deposit!!.visibility = View.GONE
    }

    fun Tv_Depost(view: View?) {
        Tv_statistics!!.setBackgroundColor(ContextCompat.getColor(this,R.color.Black))
        Tv_Depost!!.setBackgroundColor(ContextCompat.getColor(this,R.color.Greenapp))
        LL_mainlayout_statistic!!.visibility = View.GONE
        LL_mainlayout_deposit!!.visibility = View.VISIBLE
    }
}