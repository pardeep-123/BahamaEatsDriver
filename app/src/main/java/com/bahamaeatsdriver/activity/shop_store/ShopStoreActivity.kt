package com.bahamaeatsdriver.activity.shop_store

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bahamaeats.network.RestObservable
import com.bahamaeats.network.Status
import com.bahamaeatsdriver.R
import com.bahamaeatsdriver.helper.custom_progressbar.CustomProgress
import com.bahamaeatsdriver.model_class.driver_support.DriverSupportResponse
import com.bahamaeatsdriver.model_class.store_menu.GetStoreMenuResponse
import com.bahamaeatsdriver.repository.BaseViewModel
import kotlinx.android.synthetic.main.activity_shop_store.*

class ShopStoreActivity : AppCompatActivity(), Observer<RestObservable> {
    private val viewModel: BaseViewModel by lazy { ViewModelProvider(this).get(BaseViewModel::class.java) }
    private var mProgressDialog: CustomProgress? = null
    private  var type = "0"

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_store)
        iv_backArrow.setOnClickListener { finish() }
        if (intent != null && intent.getStringExtra("from") == "Contactus_Activity") {
            type = intent.getStringExtra("click")!!
            tv_title.text = getString(R.string.support_)
            viewModel.supportLinksApi(this, true)
            viewModel.supportLinksResposne().observe(this, this)
        } else {
            tv_title.text = getString(R.string.store)
            viewModel.getStoreLinks(this, true)
            viewModel.getStoreLinksResposne().observe(this, this)
        }

        webview.webViewClient = WebViewClient()
        webview.settings.javaScriptEnabled = true
        webview.settings.loadWithOverviewMode = false
        webview.settings.javaScriptCanOpenWindowsAutomatically = true
        webview.settings.domStorageEnabled = true
        mProgressDialog = CustomProgress.create(this, "")
        mProgressDialog!!.show()
        webview.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView,
                request: WebResourceRequest
            ): Boolean {
                Log.e("request1", request.url.toString())
                return super.shouldOverrideUrlLoading(view, request)
            }

            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                Log.e("request", url)
                return super.shouldOverrideUrlLoading(view, url)
            }

            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                if (mProgressDialog != null && mProgressDialog!!.isShowing)
                    mProgressDialog!!.dismiss()
            }
        }
    }

    override fun onChanged(liveData: RestObservable?) {
        when (liveData!!.status) {
            Status.ERROR -> {
                if (mProgressDialog != null && mProgressDialog!!.isShowing)
                    mProgressDialog!!.dismiss()
            }
            Status.SUCCESS -> {
                if (liveData.data is GetStoreMenuResponse) {
                    webview.loadUrl(liveData.data.body.driverMenuStore)
                }
                if (liveData.data is DriverSupportResponse) {
                    if (type=="0")
                    webview.loadUrl(liveData.data.body.chatTeamLink)
                    else
                    webview.loadUrl(liveData.data.body.callSupportLink)
                }

            }
            else -> {

            }
        }
    }
}