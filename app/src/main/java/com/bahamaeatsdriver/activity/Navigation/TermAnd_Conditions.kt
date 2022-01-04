package com.bahamaeatsdriver.activity.Navigation

import android.os.Bundle
import android.text.Html
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bahamaeats.network.RestObservable
import com.bahamaeats.network.Status
import com.bahamaeatsdriver.R
import com.bahamaeatsdriver.model_class.terms_and_conditions.TermsAndConditionResponse
import com.bahamaeatsdriver.repository.BaseViewModel
import kotlinx.android.synthetic.main.activity_term_and__conditions.*

class TermAnd_Conditions : AppCompatActivity(), Observer<RestObservable> {

    private val viewModel: BaseViewModel by lazy { ViewModelProvider(this).get(BaseViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_term_and__conditions)
        viewModel.termsAndConditionsApi(this, true)
        viewModel.getTermsAndConditionsResposne().observe(this, this)
    }

    fun iv_backTermcondition(view: View?) {
        onBackPressed()
    }

    override fun onChanged(liveData: RestObservable?) {
        when (liveData!!.status) {
            Status.SUCCESS -> {

                    if (liveData.data is TermsAndConditionResponse) {
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        tv_content.text = Html.fromHtml(liveData.data.body.content,
                            Html.FROM_HTML_SEPARATOR_LINE_BREAK_PARAGRAPH
                        )

                    } else {
                        tv_content.text = Html.fromHtml(liveData.data.body.content)
                    }
                }
            }

            Status.ERROR -> {

            }
        }
    }

}