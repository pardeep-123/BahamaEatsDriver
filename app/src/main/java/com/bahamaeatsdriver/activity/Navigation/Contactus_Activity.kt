package com.bahamaeatsdriver.activity.Navigation

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bahamaeats.constant.Constants
import com.bahamaeatsdriver.R
import kotlinx.android.synthetic.main.activity_contactus.*


class Contactus_Activity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contactus)
        rl_callSupport.setOnClickListener(this)
        rl_chatSupport.setOnClickListener(this)
        iv_backArrow_contactus.setOnClickListener(this)
    }


    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.rl_callSupport -> {
                val intent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", Constants.HELPLINE_NO, null))
                startActivity(intent)
            }
            R.id.rl_chatSupport -> {
                val link = "http://m.me/bahamaeats?ref=2062529467381132"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
                try {
                    startActivity(intent)
                } catch (ex: android.content.ActivityNotFoundException) {
                    Toast.makeText(this, "Please Install Facebook Messenger", Toast.LENGTH_LONG).show();
                }

            }
            R.id.iv_backArrow_contactus -> {
                finish()

            }
            R.id.Button_send -> {

            }
        }
    }
}