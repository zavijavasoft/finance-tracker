package com.mashjulal.android.financetracker.domain.repository

import com.mashjulal.android.financetracker.domain.financialcalculations.Account
import com.mashjulal.android.financetracker.domain.financialcalculations.Category
import com.mashjulal.android.financetracker.domain.financialcalculations.Operation
import com.mashjulal.android.financetracker.domain.financialcalculations.OperationType
import io.reactivex.Completable
import io.reactivex.Single
import java.util.*

interface OperationRepository {

    fun getAll(): Single<List<Operation>>
    fun getById(id: Long): Single<Operation>
    fun getByCategory(category: Category): Single<List<Operation>>
    fun getAfter(date: Date): Single<List<Operation>>
    fun getByType(operationType: OperationType): Single<List<Operation>>
    fun getByAccountAfter(account: Account, date: Date): Single<List<Operation>>
    fun insert(operation: Operation): Completable
}