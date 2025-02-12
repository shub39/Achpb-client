package com.shub39.achpb

import android.app.Application
import com.shub39.achpb.di.initKoin
import org.koin.android.ext.koin.androidContext

class MainApp: Application() {
    override fun onCreate() {
        super.onCreate()

        initKoin {
            androidContext(this@MainApp)
        }
    }
}