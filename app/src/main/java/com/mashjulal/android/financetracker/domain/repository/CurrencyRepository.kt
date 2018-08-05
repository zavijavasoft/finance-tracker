package com.mashjulal.android.financetracker.domain.repository

import io.reactivex.Single
import java.math.BigDecimal

interface CurrencyRepository {
    fun getRate(from: String, to: String): Single<BigDecimal>
}