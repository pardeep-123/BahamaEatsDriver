package com.bahamaeatsdriver.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bahamaeats.network.RestObservable
import com.bahamaeats.network.Status
import com.bahamaeatsdriver.R
import com.bahamaeatsdriver.adapter.FAQAdapter
import com.bahamaeatsdriver.helper.others.Helper
import com.bahamaeatsdriver.model_class.change_password.ChangePasswordResponse
import com.bahamaeatsdriver.model_class.faqs.Body
import com.bahamaeatsdriver.model_class.faqs.FaqResponse
import com.bahamaeatsdriver.repository.BaseViewModel
import kotlinx.android.synthetic.main.activity_faq.*

class FaqActivity : AppCompatActivity() , Observer<RestObservable> {


    private val viewModel: BaseViewModel by lazy { ViewModelProvider(this).get(BaseViewModel::class.java) }
    private lateinit var faqAdapter: FAQAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_faq)
        iv_backArrow_jobhistory.setOnClickListener { finish() }

        tvSearchBar.setOnTouchListener { v, _ ->
            v.isFocusable = true
            v.isFocusableInTouchMode = true
            false
        }
        tvSearchBar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                try {
                    if (faqAdapter != null) {
                        faqAdapter.filter(s.toString().trim())
                    }
                } catch (e: Exception) {
                }
            }
        })
        viewModel.getDriverFaqApi(this, true)
        viewModel.getDriverFaqResponse().observe(this, this)
    }

    override fun onChanged(liveData: RestObservable?) {
        when (liveData!!.status) {
            Status.SUCCESS -> {
                if (liveData.data is FaqResponse) {
                    if (liveData.data.body.isEmpty()) {
                        recy_faqList.visibility = View.GONE
                        tv_nodata.visibility = View.VISIBLE
                    } else {
                        recy_faqList.visibility = View.VISIBLE
                        tv_nodata.visibility = View.GONE
                        faqAdapter = FAQAdapter(this, liveData.data.body)
                        recy_faqList.adapter = faqAdapter
                    }
                }
            }


            Status.ERROR -> {

            }
            else -> {

            }
        }
    }

    fun updateViews(response: MutableList<Body>) {
        if (response.isEmpty()) {
            tv_nodata.visibility = View.VISIBLE
            tv_nodata.text = getString(R.string.no_data_available)
        } else {
            tv_nodata.visibility = View.GONE
        }
    }
}