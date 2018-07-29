package com.mashjulal.android.financetracker.recyclerview

import com.example.delegateadapter.delegate.diff.IComparableItem
import com.mashjulal.android.financetracker.financialcalculations.Operation

class OperationDetailsViewModel(val operation: Operation) : IComparableItem {

    override fun id(): Any = operation.amount
    override fun content(): Any = operation.toString()
}