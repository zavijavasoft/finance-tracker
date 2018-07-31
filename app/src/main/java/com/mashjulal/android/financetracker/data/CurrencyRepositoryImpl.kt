package com.mashjulal.android.financetracker.data

import com.mashjulal.android.financetracker.data.currencyconvertapi.CurrencyService
import com.mashjulal.android.financetracker.domain.repository.CurrencyRepository
import java.math.BigDecimal
import javax.inject.Inject

class CurrencyRepositoryImpl @Inject constructor(
        private val currencyService: CurrencyService
) : CurrencyRepository {
    override fun getRate(from: String, to: String): BigDecimal {
        val q = "${from}_$to"
        return currencyService.getRate(q).blockingGet().rate
    }
}