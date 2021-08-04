package com.bahamaeatsdriver.helper

import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.AsyncTask
import android.os.Handler
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bahamaeats.constant.Constants
import com.bahamaeatsdriver.R
import com.bahamaeatsdriver.activity.Home_Page
import com.bahamaeatsdriver.activity.login_register.Login_Activity
import com.bahamaeatsdriver.helper.extensions.getPrefrence
import com.bahamaeatsdriver.helper.extensions.launchActivity

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.MalformedURLException
import java.net.URL
import java.net.URLConnection
import java.util.regex.Pattern
import java.util.regex.PatternSyntaxException

abstract class CheckPlayStoreVersion : AppCompatActivity() {
    private var token=""
    inner class GetVersionCode : AsyncTask<Void, String, String>() {

        override fun doInBackground(vararg voids: Void): String? {

            val currentVersion_PatternSeq =
                "<div[^>]*?>Current\\sVersion</div><span[^>]*?>(.*?)><div[^>]*?>(.*?)><span[^>]*?>(.*?)</span>"
            val appVersion_PatternSeq = "htlgb\">([^<]*)</s"
            var playStoreAppVersion: String? = null

            var inReader: BufferedReader? = null
            var uc: URLConnection? = null
            val urlData = StringBuilder()

            val url: URL
            try {
                url = URL("https://play.google.com/store/apps/details?id=com.bahamaeatsdriver")
                uc = url.openConnection()
            } catch (e: MalformedURLException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }

            if (uc == null) {
                return null
            }
            uc.setRequestProperty(
                "User-Agent",
                "Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6"
            )
            try {
                inReader = BufferedReader(InputStreamReader(uc.getInputStream()))
            } catch (e: IOException) {
                e.printStackTrace()
            }

            if (null != inReader) {
                try {
                    do {
                        val line = inReader.readLine()
                        urlData.append(line)
                    } while (line != null)

                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }

            // Get the current version pattern sequence
            val versionString = getAppVersion(currentVersion_PatternSeq, urlData.toString())
            if (null == versionString) {
                return null
            } else {
                // get version from "htlgb">X.X.X</span>
                playStoreAppVersion = getAppVersion(appVersion_PatternSeq, versionString)
            }

            return playStoreAppVersion

        }


        override fun onPostExecute(onlineVersion: String?) {
            super.onPostExecute(onlineVersion)
            if (onlineVersion != null && !onlineVersion.isEmpty()) {
                try {
                    val current_version = java.lang.Float.valueOf(
                        packageManager.getPackageInfo(
                            packageName,
                            0
                        ).versionName.replace(".", "")
                    )
                    val online_version = java.lang.Float.valueOf(onlineVersion.replace(".", ""))!!
                    val diff = java.lang.Float.compare(current_version, online_version)
                    if (diff < 0) {
                        openUpdateDialog(onlineVersion)
                    } else {
                        if (getPrefrence(Constants.TOKEN, "") != null && !getPrefrence(Constants.TOKEN, "").isEmpty()) {
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
                        }, 2000)
                    }
                } catch (e: PackageManager.NameNotFoundException) {
                    e.printStackTrace()
                }
            } else {
                if (getPrefrence(Constants.TOKEN, "") != null && !getPrefrence(Constants.TOKEN, "").isEmpty()) {
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
                }, 2000)
            }
        }
    }

    private fun getAppVersion(patternString: String, inputString: String): String? {
        try {
            //Create a pattern
            val pattern = Pattern.compile(patternString) ?: return null
//Match the pattern string in provided string
            val matcher = pattern.matcher(inputString)
            if (null != matcher && matcher.find()) {
                return matcher.group(1)
            }
        } catch (ex: PatternSyntaxException) {
            ex.printStackTrace()
        }

        return null
    }


    private fun openUpdateDialog(new_version: String) {
        val dialog = Dialog(this, R.style.Theme_Dialog)
        dialog.setContentView(R.layout.update_app_dialog)
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(false)

        val update = dialog.findViewById<View>(R.id.update) as TextView
        val version_text = dialog.findViewById<View>(R.id.version_text) as TextView
        version_text.text =
            getString(R.string.youAreNotUpdatedMessage) /*+ new_version + resources.getString(R.string.now_popup)*/

        update.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName")))
            val handler = Handler()
            handler.postDelayed({ finish() }, 400)
        }

        dialog.show()
    }
}