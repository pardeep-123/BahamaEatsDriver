package com.bahamaeatsdriver.di

import android.app.Application
import com.bahamaeatsdriver.activity.ForgotPassword.Fill_old_newPassword
import com.bahamaeatsdriver.activity.Forgot_Password_Activity
import com.bahamaeatsdriver.activity.Id_Details.Edit_Id_Details
import com.bahamaeatsdriver.activity.Id_Details.Id_details
import com.bahamaeatsdriver.activity.Licence_Detail.Edit_LicenceDetail_Activity
import com.bahamaeatsdriver.activity.Licence_Detail.Fill_LicenseDetail_Activity
import com.bahamaeatsdriver.activity.Otp.Otp_Fill_Activity
import com.bahamaeatsdriver.activity.Pofile.Edit_profile
import com.bahamaeatsdriver.activity.UpdateContactNumberActivity
import com.bahamaeatsdriver.activity.bank_details.BankDetailsActivity
import com.bahamaeatsdriver.activity.login_register.Login_Activity
import com.bahamaeatsdriver.activity.login_register.Registration_Activity
import com.bahamaeatsdriver.activity.login_register.Select_country_mobileno
import com.bahamaeatsdriver.repository.BaseViewModel
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [DiModule::class])
interface Dicomponent {


    fun inject(inject: BaseViewModel)
    fun inject(inject: Login_Activity)
    fun inject(inject: Select_country_mobileno)
    fun inject(inject: Otp_Fill_Activity)
    fun inject(inject: Forgot_Password_Activity)
    fun inject(inject: Registration_Activity)
    fun inject(inject: Fill_LicenseDetail_Activity)
    fun inject(inject: BankDetailsActivity)
    fun inject(inject: Edit_profile)
    fun inject(inject: UpdateContactNumberActivity)
    fun inject(inject: Edit_LicenceDetail_Activity)
    fun inject(inject: Fill_old_newPassword)
    fun inject(inject: Id_details)
    fun inject(inject: Edit_Id_Details)


    @Component.Builder
    interface Builder {
        fun build(): Dicomponent

        @BindsInstance
        fun application(application: Application): Builder

    }
}