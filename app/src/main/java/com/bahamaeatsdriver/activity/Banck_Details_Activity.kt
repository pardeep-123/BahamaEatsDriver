package com.bahamaeatsdriver.activity

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bahamaeatsdriver.R

class Banck_Details_Activity : AppCompatActivity() {
    var button_banck: Button? = null
    var iv_edit_banck: ImageView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_banck_details)
        button_banck = findViewById(R.id.button_banck)
        iv_edit_banck = findViewById(R.id.iv_edit_banck)
    }

    fun iv_edit_banck(view: View?) {
        button_banck!!.text = "CONFIRM"
        iv_edit_banck!!.visibility = View.GONE
    }
}