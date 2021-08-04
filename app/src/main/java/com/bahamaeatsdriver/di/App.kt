package com.bahamaeatsdriver.di

import android.app.Application
import android.os.StrictMode
import com.bahamaeatsdriver.socket.SocketManager


class App : Application(){

    companion object {
        lateinit var application: App
        var dicomponent: Dicomponent? = null
        private var mSocketManager: SocketManager? = null


        fun getinstance(): App {
            return application
        }

        fun getSocketManager(): SocketManager? {
            return mSocketManager
        }
    }
    private fun initializeSocket() {
        mSocketManager = SocketManager()
        mSocketManager!!.initializeSocket()
    }


    override fun onCreate() {
        super.onCreate()
        application = this
        dicomponent = DaggerDicomponent.builder().application(this).build()
        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
        initializeSocket()
    }

    fun getmydicomponent(): Dicomponent {
        return dicomponent!!

    }


}