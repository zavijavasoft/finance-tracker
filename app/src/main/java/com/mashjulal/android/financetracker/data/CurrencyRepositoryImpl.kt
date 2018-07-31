package com.mashjulal.android.financetracker.data

import com.mashjulal.android.financetracker.data.currencyconvertapi.RetrofitHelper
import com.mashjulal.android.financetracker.domain.repository.CurrencyRepository
import java.math.BigDecimal

class CurrencyRepositoryImpl(
        private val retrofitHelper: RetrofitHelper
) : CurrencyRepository {
    override fun getRate(from: String, to: String): BigDecimal {
        val q = "${from}_$to"
        return retrofitHelper.service.getRate(q).blockingGet().rate
    }
}