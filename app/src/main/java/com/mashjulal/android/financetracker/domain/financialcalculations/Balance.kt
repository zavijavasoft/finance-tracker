package com.mashjulal.android.financetracker.domain.financialcalculations

import java.math.BigDecimal
import java.util.*

data class Balance(
        val account: Account = Account(),
        val amount: Money = Money(BigDecimal.ZERO, Currency.RUBLE),
        val date: Date = Date()
)