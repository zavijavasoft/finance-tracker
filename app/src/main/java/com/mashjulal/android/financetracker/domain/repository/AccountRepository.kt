package com.mashjulal.android.financetracker.domain.repository

import com.mashjulal.android.financetracker.domain.financialcalculations.Account
import com.mashjulal.android.financetracker.domain.financialcalculations.Money
import io.reactivex.Completable
import io.reactivex.Single

interface AccountRepository {
    fun getByName(title: String): Single<List<Account>>
    fun getAll(): Single<List<Account>>
    fun update(title: String, money: Money): Completable

    fun insert(title: String, money: Money): Completable
}