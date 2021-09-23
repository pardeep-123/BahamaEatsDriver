package com.bahamaeatsdriver.activity

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bahamaeats.network.RestObservable
import com.bahamaeats.network.Status
import com.bahamaeatsdriver.R
import com.bahamaeatsdriver.di.App
import com.bahamaeatsdriver.helper.others.Helper
import com.bahamaeatsdriver.helper.others.Validator
import com.bahamaeatsdriver.model_class.forgot_password.ForgotPasswordResponse
import com.bahamaeatsdriver.repository.BaseViewModel
import kotlinx.android.synthetic.main.activity_forgot_password.*
import javax.inject.Inject

class Forgot_Password_Activity : AppCompatActivity(), Observer<RestObservable> {

    @Inject
    lateinit var validator: Validator
    private val viewModel: BaseViewModel by lazy { ViewModelProvider(this).get(BaseViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        App.getinstance().getmydicomponent().inject(this)
    }

    fun Button_subbmit(view: View?) {
         val email = et_email.text.toString().trim()
        if (validator.emailValid(this, email)) {
            viewModel.forgotPasswordApi(this, email, true)
            viewModel.getforgotPasswordResponse().observe(this, this)
        }
    }

    override fun onChanged(liveData: RestObservable?) {
        when (liveData!!.status) {
            Status.SUCCESS -> {
                if (liveData.data is ForgotPasswordResponse) {
                    if (liveData.data.code == 403)
                        Helper.showErrorAlert(this, liveData.data.message)
                     else {
                        Helper.showSuccessToast(this, liveData.data.message)
                        finish()
                    }
                }

            }
            Status.ERROR -> {

            }
        }
    }
}