package com.mashjulal.android.financetracker

import android.app.Application
import com.mashjulal.android.financetracker.di.AppComponent
import com.mashjulal.android.financetracker.di.DaggerAppComponent

class App : Application() {

    companion object {
        @JvmStatic
        lateinit var appComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent
                .builder()
                .application(this)
                .context(this.applicationContext)
                .build()

    }

}