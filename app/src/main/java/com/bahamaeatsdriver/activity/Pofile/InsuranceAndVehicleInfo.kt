package com.bahamaeatsdriver.activity.Pofile

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bahamaeatsdriver.R

class InsuranceAndVehicleInfo : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insurance_and_vehicle_info)
    }

    fun iv_backArrow_insurance(view: View?) {
        onBackPressed()
        finish()
    }
}