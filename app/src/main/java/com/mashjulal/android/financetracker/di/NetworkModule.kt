package com.mashjulal.android.financetracker.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.mashjulal.android.financetracker.data.currencyconvertapi.CurrencyRateJsonDeserializer
import com.mashjulal.android.financetracker.data.currencyconvertapi.CurrencyRateModel
import com.mashjulal.android.financetracker.data.currencyconvertapi.CurrencyService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class NetworkModule(private val baseUrl: String) {

    @Singleton
    @Provides
    fun providesGson(): Gson = GsonBuilder()
            .registerTypeAdapter(CurrencyRateModel::class.java, CurrencyRateJsonDeserializer())
            .setLenient()
            .create()

    @Singleton
    @Provides
    fun providesRetrofit(gson: Gson): Retrofit {
        return Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
    }

    @Singleton
    @Provides
    fun providesCurrencyService(retrofit: Retrofit): CurrencyService =
            retrofit.create(CurrencyService::class.java)

}