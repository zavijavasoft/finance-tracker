package com.mashjulal.android.financetracker.recyclerview

import com.example.delegateadapter.delegate.diff.IComparableItem
import com.mashjulal.android.financetracker.financialcalculations.convertRublesToDollars
import java.math.BigDecimal

class BalanceViewModel(val balanceInRubles: BigDecimal) : IComparableItem {

    override fun id(): Any = balanceInRubles

    override fun content(): Any =
            "$balanceInRubles ${convertRublesToDollars(balanceInRubles)}"
}