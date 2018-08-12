package com.mashjulal.android.financetracker.domain.repository

import com.mashjulal.android.financetracker.domain.financialcalculations.Currency
import io.reactivex.Single
import java.math.BigDecimal

interface CurrencyRepository {
    fun getRate(from: String, to: String): Single<BigDecimal>
    fun getCurrencyList(): Single<List<Currency>>
}