package com.mashjulal.android.financetracker.presentation.main.recyclerview.operation

import com.example.delegateadapter.delegate.diff.IComparableItem
import com.mashjulal.android.financetracker.domain.financialcalculations.Money
import com.mashjulal.android.financetracker.domain.financialcalculations.Operation
import com.mashjulal.android.financetracker.domain.financialcalculations.OperationType

sealed class OperationPreviewViewModel(val title: String, val balance: Money,
                                       val operations: List<Operation>,
                                       val hasMore: Boolean) : IComparableItem {

    override fun id(): Any = title

    override fun content(): Any = "$title $operations $hasMore"
}

class IncomingsPreviewViewModel(balance: Money, operations: List<Operation>, hasMore: Boolean)
    : OperationPreviewViewModel(OperationType.INCOMINGS.name, balance, operations, hasMore)

class OutgoingsPreviewViewModel(balance: Money, operations: List<Operation>, hasMore: Boolean)
    : OperationPreviewViewModel(OperationType.OUTGOINGS.name, balance, operations, hasMore)