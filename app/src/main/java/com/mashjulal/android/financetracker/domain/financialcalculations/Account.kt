package com.mashjulal.android.financetracker.domain.financialcalculations

import java.math.BigDecimal
import java.util.*

data class Account(
        val title: String = "Dummy Account",
        val money: Money = Money(BigDecimal(0.0), Currency.RUBLE),
        val lastUpdated: Date = Date()
)
