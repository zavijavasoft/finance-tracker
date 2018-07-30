package com.mashjulal.android.financetracker.data.currencyconvertapi

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyService {
    @GET("/api/v6/convert")
    fun getRate(@Query("q") query: String, @Query("compact") compact: String = "y"):
            Observable<CurrencyRateModel>
}