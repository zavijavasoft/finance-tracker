package com.mashjulal.android.financetracker.recyclerview

import com.example.delegateadapter.delegate.diff.IComparableItem
import com.mashjulal.android.financetracker.financialcalculations.Currency
import com.mashjulal.android.financetracker.financialcalculations.Operation
import java.math.BigDecimal

class OperationPreviewViewModel(val title: String, val balance: BigDecimal,
                                val currency: Currency, val operations: List<Operation>,
                                val hasMore: Boolean) : IComparableItem {

    override fun id(): Any = title

    override fun content(): Any = "$title $balance $currency $operations $hasMore"
}