package com.bahamaeatsdriver.di

import com.bahamaeats.network.RestApiInterface
import com.bahamaeatsdriver.helper.others.Validator
import com.bahamaeatsdriver.network.ServiceGenerator
import dagger.Provides
import javax.inject.Singleton


@dagger.Module
class DiModule {



    @Provides
    @Singleton
    fun validation(): Validator
    {
        return Validator()
    }
    /*  @Provides
     @Singleton
     fun sharedPref(): SharedPref
     {
         return SharedPref()
     }*/

    @Provides
    @Singleton
    fun getService(): RestApiInterface
    {
        return ServiceGenerator.createService(RestApiInterface::class.java)
    }


}