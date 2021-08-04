package com.bahamaeatsdriver.activity.Navigation

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bahamaeatsdriver.R

class Refer_And_Earn : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_refer_and_earn)
    }

    fun iv_backArrow_refer(view: View?) {
        onBackPressed()
    }
}