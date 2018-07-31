package com.mashjulal.android.financetracker

import android.app.Application
import com.mashjulal.android.financetracker.di.*

class App : Application() {

    companion object {
        lateinit var appComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()
        appComponent = buildComponent()
    }

    private fun buildComponent(): AppComponent {
        val baseUrl = "https://free.currencyconverterapi.com/"
        return DaggerAppComponent.builder()
                .networkModule(NetworkModule(baseUrl))
                .dataStorageModule(DataStorageModule())
                .interactorModule(InteractorModule())
                .build()
    }
}