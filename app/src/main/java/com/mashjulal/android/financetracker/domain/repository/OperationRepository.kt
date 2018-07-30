package com.mashjulal.android.financetracker.domain.repository

import com.mashjulal.android.financetracker.domain.financialcalculations.Category
import com.mashjulal.android.financetracker.domain.financialcalculations.Operation
import com.mashjulal.android.financetracker.domain.financialcalculations.OperationType
import java.util.*

interface OperationRepository {

    fun getById(id: Long): Operation?
    fun getByCategory(category: Category): List<Operation>
    fun getAfter(date: Date): List<Operation>
    fun getByType(operationType: OperationType): List<Operation>
    fun insert(operation: Operation): Long
}