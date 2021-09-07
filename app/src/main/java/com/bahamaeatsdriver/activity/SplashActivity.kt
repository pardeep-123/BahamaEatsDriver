package com.bahamaeatsdriver.activity

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.bahamaeats.constant.Constants
import com.bahamaeatsdriver.R
import com.bahamaeatsdriver.activity.login_register.Login_Activity
import com.bahamaeatsdriver.helper.CheckPlayStoreVersion
import com.bahamaeatsdriver.helper.extensions.getPrefrence
import com.bahamaeatsdriver.helper.extensions.launchActivity


class SplashActivity : CheckPlayStoreVersion() {
//class SplashActivity : AppCompatActivity() {
    var handler: Handler? = null
    val a: String? = null
    var token = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splesh__screen)
        // Initialize the Update Manager with the Activity and the Update Mode
        /*if (getPrefrence(Constants.TOKEN, "") != null && !getPrefrence(Constants.TOKEN, "").isEmpty()) {
            token = getPrefrence(Constants.TOKEN, "")
        }
        Handler().postDelayed({
            if (!token.isEmpty()) {
                launchActivity<Home_Page>()
                finish()
            } else {
                launchActivity<Login_Activity>()
                finish()
            }
        }, 2000)*/

        GetVersionCode().execute()
    }
}