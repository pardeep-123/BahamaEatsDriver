package com.bahamaeatsdriver.network

import com.bahamaeats.constant.Constants
import com.bahamaeatsdriver.helper.extensions.getPrefrence
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import okhttp3.ConnectionSpec
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit


object ServiceGenerator {
    private val httpClient = OkHttpClient.Builder()
            .readTimeout((5 * 60).toLong(), TimeUnit.SECONDS)
            .connectTimeout((5 * 60).toLong(), TimeUnit.SECONDS)
            .writeTimeout((5 * 60).toLong(), TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .addInterceptor(provideHeaderInterceptor())
            .connectionSpecs(Arrays.asList(ConnectionSpec.MODERN_TLS, ConnectionSpec.CLEARTEXT)).build()

    private val gson = GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
            .create()

    private val builder = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())


    @JvmStatic
    fun <S> createService(serviceClass: Class<S>): S {
        val retrofit = getRetrofit()
        return retrofit.create(serviceClass)
    }

    @JvmStatic
    fun getRetrofit(): Retrofit {

        return builder.client(httpClient)
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setPrettyPrinting().create()))
                .build()
    }

    private fun provideHeaderInterceptor(): Interceptor {


        return Interceptor { chain ->
            val request: Request
            if (getPrefrence(Constants.TOKEN, "") != null && !getPrefrence(Constants.TOKEN, "").isEmpty()) {
                request = chain.request().newBuilder()
                        .header(Constants.SECURITY_KEY, Constants.SECURITY_KEY_CODE)
                        .header("Authorization", "Bearer " + getPrefrence(Constants.TOKEN, ""))
                        .header("Accept", "application/json")
                        .build()
            } else {
                request = chain.request().newBuilder()
                        .header(Constants.SECURITY_KEY, Constants.SECURITY_KEY_CODE)
                        .build()
            }

            chain.proceed(request)
        }
    }
}