package com.mashjulal.android.financetracker.presentation.main.recyclerview.balance

import com.example.delegateadapter.delegate.diff.IComparableItem
import com.mashjulal.android.financetracker.domain.financialcalculations.Money
import com.mashjulal.android.financetracker.domain.financialcalculations.convertRublesToDollars

class BalanceViewModel(val balanceInRubles: Money) : IComparableItem {

    override fun id(): Any = balanceInRubles

    override fun content(): Any =
            "$balanceInRubles ${convertRublesToDollars(balanceInRubles)}"
}