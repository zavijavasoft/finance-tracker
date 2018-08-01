package com.mashjulal.android.financetracker.domain.repository

import com.mashjulal.android.financetracker.domain.financialcalculations.Category
import com.mashjulal.android.financetracker.domain.financialcalculations.OperationType

interface CategoryRepository {

    fun getAll(): List<Category>
    fun getByOperationType(operationType: OperationType): List<Category>
}