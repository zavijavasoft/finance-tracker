package com.mashjulal.android.financetracker.domain.repository

import java.math.BigDecimal

interface CurrencyRepository {
    fun getRate(from: String, to: String): BigDecimal
}