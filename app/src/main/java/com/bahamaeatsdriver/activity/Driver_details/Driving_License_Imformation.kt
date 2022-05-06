package com.bahamaeatsdriver.activity.Driver_details

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bahamaeatsdriver.R
import com.google.android.material.textfield.TextInputEditText

class Driving_License_Imformation : AppCompatActivity() {
    private var iv_licence: ImageView? = null
    private var iv_edit: ImageView? = null
    private var Relativ_camera: RelativeLayout? = null
    private var ButtonsLayout: LinearLayout? = null
    private var te_licence_type: TextInputEditText? = null
    private var Te_birthdate: TextInputEditText? = null
    private var Te_Licence_no: TextInputEditText? = null
    private var te_nationality: TextInputEditText? = null
    private var te_issueon: TextInputEditText? = null
    private var te_expired: TextInputEditText? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_driving_license_imformation)

    }

    fun iv_edit(view: View?) {
        te_expired!!.isEnabled = true
        te_issueon!!.isEnabled = true
        te_nationality!!.isEnabled = true
        Te_Licence_no!!.isEnabled = true
        Te_birthdate!!.isEnabled = true
        te_licence_type!!.isEnabled = true
        Relativ_camera!!.visibility = View.VISIBLE
        iv_edit!!.visibility = View.GONE
        ButtonsLayout!!.visibility = View.VISIBLE
        iv_licence!!.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.licence))
    }

    fun iv_backArrow_driving(view: View?) {
        onBackPressed()
    }

    fun Button_cancle(view: View?) {
        onBackPressed()
    }

    fun Button_update(view: View?) {
        onBackPressed()
    }
}