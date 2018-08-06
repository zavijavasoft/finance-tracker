package com.mashjulal.android.financetracker.domain.repository

import com.mashjulal.android.financetracker.domain.financialcalculations.Account
import com.mashjulal.android.financetracker.domain.financialcalculations.Balance
import io.reactivex.Single
import java.util.*

interface BalanceRepository {
    fun getByAccount(account: Account): Single<List<Balance>>
    fun getByAccountAfter(account: Account, date: Date): Single<List<Balance>>
    fun getLastByAccount(account: Account): Single<List<Balance>>
    fun getLastByAll(): Single<List<Balance>>
}