package com.bahamaeatsdriver.di

import android.app.Application
import android.os.StrictMode
import com.bahamaeatsdriver.helper.MediaLoader
import com.bahamaeatsdriver.socket.SocketManager
import com.yanzhenjie.album.Album
import com.yanzhenjie.album.AlbumConfig
import java.util.*


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
        Album.initialize(
            AlbumConfig.newBuilder(this)
                .setAlbumLoader(MediaLoader())
                .setLocale(Locale.getDefault())
                .build()
        )
    }

    fun getmydicomponent(): Dicomponent {
        return dicomponent!!

    }


}