package com.bahamaeatsdriver.activity.Navigation

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bahamaeatsdriver.R

class AboutUs : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_us)
    }

    fun iv_backArrowabout(view: View?) {
        onBackPressed()
        finish()
    }
}