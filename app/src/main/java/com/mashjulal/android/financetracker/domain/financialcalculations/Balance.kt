package com.mashjulal.android.financetracker.domain.financialcalculations

import java.util.*

data class Balance(
        val account: Account,
        val amount: Money,
        val date: Date
)