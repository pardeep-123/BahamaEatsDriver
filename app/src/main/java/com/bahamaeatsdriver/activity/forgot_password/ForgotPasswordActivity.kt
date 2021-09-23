package com.bahamaeatsdriver.activity.forgot_password

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
import com.bahamaeatsdriver.model_class.change_password.ChangePasswordResponse
import com.bahamaeatsdriver.repository.BaseViewModel
import kotlinx.android.synthetic.main.activity_fill_old_new_password.*
import kotlinx.android.synthetic.main.toolbar_layout.*
import javax.inject.Inject

class ForgotPasswordActivity : AppCompatActivity(), View.OnClickListener, Observer<RestObservable> {

    @Inject
    lateinit var validator: Validator
    private val viewModel: BaseViewModel by lazy { ViewModelProvider(this).get(BaseViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fill_old_new_password)
        App.getinstance().getmydicomponent().inject(this)
        btn_reset.setOnClickListener(this)
        iv_backArrow.setOnClickListener(this)
    }


    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.iv_backArrow -> {
                finish()
            }

            R.id.btn_reset -> {
                val currentPassword = et_oldPassword.text.toString().trim()
                val newPassword = et_newPassword.text.toString().trim()
                if (validator.changePasswordValid(this, currentPassword, newPassword)) {
                    viewModel.changePasswordApi(this, currentPassword, newPassword, true)
                    viewModel.getChangePasswordResponse().observe(this, this)
                }
            }
        }
    }

    override fun onChanged(liveData: RestObservable?) {
        when (liveData!!.status) {
            Status.SUCCESS -> {
                if (liveData.data is ChangePasswordResponse) {
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
            else -> {

            }
        }
    }
}