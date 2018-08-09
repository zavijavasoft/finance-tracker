package com.mashjulal.android.financetracker.domain.repository

import com.mashjulal.android.financetracker.domain.financialcalculations.Category
import com.mashjulal.android.financetracker.domain.financialcalculations.OperationType
import io.reactivex.Completable
import io.reactivex.Single

interface CategoryRepository {

    fun create(category: Category): Completable
    fun remove(category: Category): Completable
    fun getAll(): Single<List<Category>>
    fun getByOperationType(operationType: OperationType): Single<List<Category>>
}