package com.mashjulal.android.financetracker.presentation.main.recyclerview.balance

import com.example.delegateadapter.delegate.diff.IComparableItem
import com.mashjulal.android.financetracker.domain.financialcalculations.Money

class BalanceViewModel(val balanceInRubles: Money, val balanceInDollars: Money) : IComparableItem {

    override fun id(): Any = balanceInRubles

    override fun content(): Any =
            "$balanceInRubles $balanceInDollars"
}